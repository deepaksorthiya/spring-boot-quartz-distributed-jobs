package com.example;

import com.example.sample.SampleJob;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.assertj.core.api.InstanceOfAssertFactory;
import org.assertj.core.api.MapAssert;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

/**
 * Web tests for {@link SampleQuartzApplication}.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(OutputCaptureExtension.class)
class SampleQuartzApplicationWebTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void quartzGroupNames() {
        Map<String, Object> content = getContent("/actuator/quartz");
        assertThat(content).containsOnlyKeys("jobs", "triggers");
    }

    @Test
    void quartzJobGroups() {
        Map<String, Object> content = getContent("/actuator/quartz/jobs");
        assertThat(content).containsOnlyKeys("groups");
        assertThat(content).extractingByKey("groups", nestedMap()).containsOnlyKeys("samples");
    }

    @Test
    void quartzTriggerGroups() {
        Map<String, Object> content = getContent("/actuator/quartz/triggers");
        assertThat(content).containsOnlyKeys("groups");
        assertThat(content).extractingByKey("groups", nestedMap()).containsOnlyKeys("DEFAULT", "samples");
    }

    @Test
    void quartzJobDetail() {
        Map<String, Object> content = getContent("/actuator/quartz/jobs/samples/helloJob");
        assertThat(content).containsEntry("name", "helloJob").containsEntry("group", "samples");
    }

    @Test
    void quartzJobDetailWhenNameDoesNotExistReturns404() {
        ResponseEntity<String> response = this.restTemplate.getForEntity("/actuator/quartz/jobs/samples/does-not-exist",
                String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void quartzTriggerDetail() {
        Map<String, Object> content = getContent("/actuator/quartz/triggers/samples/3am-weekdays");
        assertThat(content).contains(entry("group", "samples"), entry("name", "3am-weekdays"), entry("state", "NORMAL"),
                entry("type", "cron"));
    }

    @Test
    void quartzTriggerDetailWhenNameDoesNotExistReturns404() {
        ResponseEntity<String> response = this.restTemplate
                .getForEntity("/actuator/quartz/triggers/samples/does-not-exist", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void quartzJobTriggeredManually(CapturedOutput output) {
        ResponseEntity<Map<String, Object>> result = asMapEntity(this.restTemplate.postForEntity(
                "/actuator/quartz/jobs/samples/onDemandJob", new HttpEntity<>(Map.of("state", "running")), Map.class));
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        Map<String, Object> content = result.getBody();
        assertThat(content).contains(entry("group", "samples"), entry("name", "onDemandJob"),
                entry("className", SampleJob.class.getName()));
        assertThat(content).extractingByKey("triggerTime", InstanceOfAssertFactories.STRING)
                .satisfies((triggerTime) -> assertThat(Instant.parse(triggerTime)).isCloseTo(Instant.now(),
                        within(10, ChronoUnit.SECONDS)));
        Awaitility.await()
                .atMost(Duration.ofSeconds(30))
                .untilAsserted(() -> assertThat(output).contains("Hello On Demand Job"));
    }

    private Map<String, Object> getContent(String path) {
        ResponseEntity<Map<String, Object>> entity = asMapEntity(this.restTemplate.getForEntity(path, Map.class));
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        return entity.getBody();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static <K, V> ResponseEntity<Map<K, V>> asMapEntity(ResponseEntity<Map> entity) {
        return (ResponseEntity) entity;
    }

    @SuppressWarnings("rawtypes")
    private static InstanceOfAssertFactory<Map, MapAssert<String, Object>> nestedMap() {
        return InstanceOfAssertFactories.map(String.class, Object.class);
    }

}
