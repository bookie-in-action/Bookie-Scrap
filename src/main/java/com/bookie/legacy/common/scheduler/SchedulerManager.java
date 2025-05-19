package com.bookie.legacy.common.scheduler;

import com.bookie.legacy.common.lifecycle.Initializable;
import com.bookie.legacy.common.lifecycle.Shutdownable;
import com.bookie.legacy.common.properties.SchedulerProperties;
import com.bookie.legacy.common.properties.SchedulerProperties.Key;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.DirectSchedulerFactory;
import org.quartz.simpl.RAMJobStore;
import org.quartz.simpl.SimpleThreadPool;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SchedulerManager implements Initializable, Shutdownable {
    private static final Map<String, Scheduler> SCHEDULERS = new ConcurrentHashMap<>();
    private static final SchedulerManager INSTANCE = new SchedulerManager();

    private SchedulerManager() {}

    public static SchedulerManager getInstance() {return INSTANCE;}

    @Override
    public void init(String runningOption) {

        SchedulerProperties.getInstance()
                .getSCHEDULER_SETTINGS_BY_PK().forEach((schedulerPk, schedulerProp) -> {
                    try {
                        SCHEDULERS.put(schedulerPk, this.createScheduler(schedulerProp));
                    } catch (SchedulerException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                });

    }

    private Scheduler createScheduler(Map<Key, String> schedulerSettings)
            throws SchedulerException, ClassNotFoundException {

        String schedulerPk = schedulerSettings.get(Key.PK);

        Collection<Scheduler> schedulers = DirectSchedulerFactory.getInstance().getAllSchedulers();

        Set<String> schedulerNames = new HashSet<>();
        if (!schedulers.isEmpty()) {
            for (Scheduler scheduler : schedulers) {
                schedulerNames.add(scheduler.getSchedulerName());
            }
        }

        if (!schedulerNames.contains(schedulerPk)){

            DirectSchedulerFactory.getInstance().createScheduler(
                    schedulerPk,
                    "AUTO",
                    new SimpleThreadPool(1, Thread.NORM_PRIORITY),
                    new RAMJobStore()
            );

            Scheduler scheduler = DirectSchedulerFactory.getInstance().getScheduler(schedulerPk);

            Class<? extends Job> jobClass = Class.forName(schedulerSettings.get(Key.JOB_CLASS)).asSubclass(Job.class);
            JobDetail job = JobBuilder.newJob(jobClass)
                    .withIdentity(schedulerPk)
                    .build();

            String schedulerExpression = schedulerSettings.get(Key.EXPRESSION);
            SchedulerMode mode = SchedulerMode.findByPk(schedulerSettings.get(Key.MODE));

            ScheduleBuilder<? extends Trigger> scheduleBuilder = mode.getScheduleBuilder(schedulerExpression);

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(schedulerPk)
                    .withSchedule(scheduleBuilder)
                    .build();

            scheduler.scheduleJob(job, trigger);

            return scheduler;
        } else {
            return DirectSchedulerFactory.getInstance().getScheduler(schedulerPk);
        }
    }

    public void startSchedulers() throws SchedulerException {

        for (Map.Entry<String, Scheduler> entry : SchedulerManager.SCHEDULERS.entrySet()) {
            String schedulerPk = entry.getKey();
            Scheduler scheduler = entry.getValue();

            String isEnabled = SchedulerProperties.getInstance().getSchedulerSettingsByPk(schedulerPk).get(Key.ENABLED);

            if ("true".equalsIgnoreCase(isEnabled)) {
                scheduler.start();
                log.info("Start Scheduler [{}]", entry.getValue().getSchedulerName());
            }
        }
    }

    @Override
    public void shutdown() {
        SCHEDULERS.values().stream().parallel().forEach(scheduler -> {
            try {
                scheduler.shutdown(true);
                log.info("Stop [{}] Scheduler successfully", scheduler.getSchedulerName());
            } catch (SchedulerException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
