package com.bookie.scrap.common.scheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.quartz.CronScheduleBuilder;
import org.quartz.ScheduleBuilder;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;

@Getter
@RequiredArgsConstructor
public enum SchedulerMode {

    CRON("cron", new CronSchedulerBuilderFactory()),
    INTERVAL("interval", new IntervalSchedulerBuilderFactory());

    private static final Map<String, SchedulerMode> SCHEDULER_MODES;

    private final String pk;
    private final Function<String, ScheduleBuilder<? extends Trigger>> triggerBuilderFactory;

    static {
        SCHEDULER_MODES = new HashMap<>();
        for (SchedulerMode mode : values()) {
            SCHEDULER_MODES.put(mode.getPk(), mode);
        }
    }

    public static SchedulerMode findByPk(String modeName) {
        return Optional.ofNullable(SCHEDULER_MODES.get(modeName))
                .orElseThrow(() -> new IllegalArgumentException("[" + modeName + "] not found"));
    }

    public ScheduleBuilder<? extends Trigger> getScheduleBuilder(String expression) {
        return triggerBuilderFactory.apply(expression);
    }

    private static class IntervalSchedulerBuilderFactory
            implements Function<String, ScheduleBuilder<? extends Trigger>> {

        @Override
        public SimpleScheduleBuilder apply(String interval) {
            return SimpleScheduleBuilder
                    .simpleSchedule()
                    .withIntervalInSeconds(Integer.parseInt(interval))
                    .repeatForever();
        }
    }

    private static class CronSchedulerBuilderFactory
            implements Function<String, ScheduleBuilder<? extends Trigger>> {

        @Override
        public CronScheduleBuilder apply(String cronExpression) {
            return CronScheduleBuilder
                    .cronSchedule(cronExpression);
        }
    }
}

