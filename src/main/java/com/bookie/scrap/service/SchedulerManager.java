package com.bookie.scrap.service;

import com.bookie.scrap.properties.SchedulerProperties;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.DirectSchedulerFactory;
import org.quartz.simpl.RAMJobStore;
import org.quartz.simpl.SimpleThreadPool;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SchedulerManager {
    private static final Map<String, Scheduler> SCHEDULERS = new ConcurrentHashMap<>();

    public void init() {
        Map<String, Map<SchedulerProperties.Key, String>> props = SchedulerProperties.getInstance().getSCHEDULER_PROPS();

        props.forEach((schedulerName, schedulerProp) -> {
            try {
                SCHEDULERS.put(schedulerName, this.createScheduler(schedulerProp));
            } catch (SchedulerException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private Scheduler createScheduler(Map<SchedulerProperties.Key, String> schedulerConfig) throws SchedulerException, ClassNotFoundException {

        DirectSchedulerFactory.getInstance().createScheduler(
                schedulerConfig.get(SchedulerProperties.Key.NAME),
                "AUTO",
                new SimpleThreadPool(1, Thread.NORM_PRIORITY),
                new RAMJobStore()
        );

        Scheduler scheduler = DirectSchedulerFactory.getInstance().getScheduler(schedulerConfig.get(SchedulerProperties.Key.NAME));

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

    public void startSchedulers() throws SchedulerException {
        for (Map.Entry<String, Scheduler> entry : SCHEDULERS.entrySet()) {
            if ("true".equalsIgnoreCase(hometaxProperties.getValue(entry.getKey() + SCHEDULER_ENABLE.getKey()))) {
                entry.getValue().start();
                log.info("Start Scheduler [{}]", entry.getValue().getSchedulerName());
            }
        }
    }

    public void stopSchedulers() {
        SCHEDULERS.values().stream().parallel().forEach(scheduler -> {
            try {
                log.info("Start to Stop Scheduler [{}]", scheduler.getSchedulerName());
                scheduler.shutdown(true);
                log.info("End to Stop Scheduler [{}]", scheduler.getSchedulerName());
            } catch (SchedulerException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
