package com.bookie.scrap.service;

import com.bookie.scrap.properties.SchedulerProperties;
import org.quartz.*;
import org.quartz.impl.DirectSchedulerFactory;
import org.quartz.simpl.RAMJobStore;
import org.quartz.simpl.SimpleThreadPool;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class SchedulerManager {
    public void init() {
        List<Map<SchedulerProperties.Key, String>> props =
                SchedulerProperties.getInstance().getProps();

        Map<SchedulerProperties.Key, String> schedulerConfig = props.get(0);
    }

    private Scheduler createScheduler(String schedulerName) throws SchedulerException, ClassNotFoundException {

        List<Map<SchedulerProperties.Key, String>> props =
                SchedulerProperties.getInstance().getProps();

        Map<SchedulerProperties.Key, String> schedulerConfig = props.get(0);

        DirectSchedulerFactory.getInstance().createScheduler(
                schedulerName,
                "AUTO",
                new SimpleThreadPool(1, Thread.NORM_PRIORITY),
                new RAMJobStore()
        );

        Scheduler scheduler = DirectSchedulerFactory.getInstance().getScheduler(schedulerName);

        Class<? extends Job> jobClass = Class.forName(schedulerConfig.get(SchedulerProperties.Key.JOB_CLASS)).asSubclass(Job.class);
        JobDetail job = JobBuilder.newJob(jobClass)
                .withIdentity(schedulerConfig.get(SchedulerProperties.Key.NAME))
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(schedulerConfig.get(SchedulerProperties.Key.NAME))
                .withSchedule(scheduleBuilder)
                .build();

        scheduler.scheduleJob(job, trigger);

        return scheduler;
    }
}
