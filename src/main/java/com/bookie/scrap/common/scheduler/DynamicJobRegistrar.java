package com.bookie.scrap.common.scheduler;

import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DynamicJobRegistrar {

    private final Scheduler scheduler;
    private final SchedulerProperties schedulerProperties;

    public void registerJobs() throws ClassNotFoundException, SchedulerException {
        for (SchedulerProperties.SchedulerSetting schedulerSetting : schedulerProperties.getSchedulerSettings()) {
            if (!schedulerSetting.isEnabled()) {
                continue;
            }

            JobKey jobKey = JobKey.jobKey(schedulerSetting.getJobName());
            Class<? extends Job> jobClass = Class.forName(schedulerSetting.getJobClass()).asSubclass(Job.class);

            JobDetail jobDetail = JobBuilder.newJob(jobClass)
                    .withIdentity(jobKey)
                    .storeDurably()
                    .build();

            if (!scheduler.checkExists(jobKey)) {
                scheduler.addJob(jobDetail, true);
            }

            if (schedulerSetting.isRunOnStart()) {
                Trigger startNowTrigger = TriggerBuilder.newTrigger()
                        .withIdentity(schedulerSetting.getJobName() + "-startup")
                        .forJob(jobKey)
                        .startNow()
                        .build();

                scheduler.scheduleJob(startNowTrigger);
            }

            String expression = schedulerSetting.getExpression();
            ScheduleBuilder<? extends Trigger> scheduleBuilder = schedulerSetting.getScheduleBuilder().apply(expression);

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(schedulerSetting.getJobName())
                    .forJob(jobKey)
                    .withSchedule(scheduleBuilder)
                    .build();

            scheduler.scheduleJob(trigger);
        }
    }

}
