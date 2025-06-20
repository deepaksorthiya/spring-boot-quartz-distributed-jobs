package com.example.sample;

import org.quartz.*;
import org.quartz.DateBuilder.IntervalUnit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Calendar;

@Configuration
public class JobConfig {
    @Bean
    public JobDetail helloJobDetail() {
        return JobBuilder.newJob(SampleJob.class)
                .withIdentity("helloJob", "samples")
                .usingJobData("name", "World")
                .storeDurably()
                .build();
    }

    @Bean
    public JobDetail anotherJobDetail() {
        return JobBuilder.newJob(SampleJob.class)
                .withIdentity("anotherJob", "samples")
                .usingJobData("name", "Everyone")
                .storeDurably()
                .build();
    }

    @Bean
    public JobDetail onDemandJobDetail() {
        return JobBuilder.newJob(SampleJob.class)
                .withIdentity("onDemandJob", "samples")
                .usingJobData("name", "On Demand Job")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger everyTwoSecTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob("helloJob", "samples")
                .withIdentity("sampleTrigger")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever())
                .build();
    }

    @Bean
    public Trigger everyDayTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob("helloJob", "samples")
                .withIdentity("every-day", "samples")
                .withSchedule(SimpleScheduleBuilder.repeatHourlyForever(24))
                .build();
    }

    @Bean
    public Trigger threeAmWeekdaysTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob("anotherJob", "samples")
                .withIdentity("3am-weekdays", "samples")
                .withSchedule(CronScheduleBuilder.atHourAndMinuteOnGivenDaysOfWeek(3, 0, 1, 2, 3, 4, 5))
                .build();
    }

    @Bean
    public Trigger onceAWeekTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob("anotherJob", "samples")
                .withIdentity("once-a-week", "samples")
                .withSchedule(CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withIntervalInWeeks(1))
                .build();
    }

    @Bean
    public Trigger everyHourWorkingHourTuesdayAndThursdayTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob("helloJob", "samples")
                .withIdentity("every-hour-tue-thu", "samples")
                .withSchedule(DailyTimeIntervalScheduleBuilder.dailyTimeIntervalSchedule()
                        .onDaysOfTheWeek(Calendar.TUESDAY, Calendar.THURSDAY)
                        .startingDailyAt(TimeOfDay.hourAndMinuteOfDay(9, 0))
                        .endingDailyAt(TimeOfDay.hourAndMinuteOfDay(18, 0))
                        .withInterval(1, IntervalUnit.HOUR))
                .build();
    }
}
