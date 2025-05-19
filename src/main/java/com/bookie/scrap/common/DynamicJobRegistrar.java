package com.bookie.scrap.common;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DynamicJobRegistrar {

    private final Scheduler scheduler;
    private final SchedulerProperties schedulerProperties;

    @PostConstruct
    public void registerJobs() throws ClassNotFoundException, SchedulerException {
        for (SchedulerProperties.SchedulerSetting schedulerSetting : schedulerProperties.getSchedulerSettings()) {
            if (!schedulerSetting.isEnabled()) {
                continue;
            }

            Class<? extends Job> jobClass = Class.forName(schedulerSetting.getJobClass()).asSubclass(Job.class);
            JobDetail jobDetail = JobBuilder.newJob(jobClass)
                    .withIdentity(schedulerSetting.getId())
                    .build();

            String expression = schedulerSetting.getExpression();
            ScheduleBuilder<? extends Trigger> scheduleBuilder = schedulerSetting.getScheduleBuilder().apply(expression);

            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(schedulerSetting.getId())
                    .withSchedule(scheduleBuilder)
                    .build();

            scheduler.scheduleJob(jobDetail, trigger);
        }
    }
}
