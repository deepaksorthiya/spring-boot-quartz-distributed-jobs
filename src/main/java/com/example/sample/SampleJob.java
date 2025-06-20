package com.example.sample;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.LocalDateTime;

public class SampleJob extends QuartzJobBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(SampleJob.class);
    private String name;

    // Invoked if a Job data map entry with that name
    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        LOGGER.info("{} -- {} -- Hello {}! -- Job Data :: {}", Thread.currentThread(), LocalDateTime.now(), this.name, context.getJobDetail().getJobDataMap());
    }

}
