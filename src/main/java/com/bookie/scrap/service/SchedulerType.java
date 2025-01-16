package com.bookie.scrap.service;

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
public enum SchedulerType {

    CRON("cron", new CronSchedulerBuilderFactory()),
    INTERVAL("interval", new IntervalSchedulerBuilderFactory());

    private static final Map<String, SchedulerType> SCHEDULER_TYPES;

    private final String code;
    private final Function<String, ScheduleBuilder<? extends Trigger>> triggerBuilderFactory;

    static {
        SCHEDULER_TYPES = new HashMap<>();
        for (SchedulerType type : values()) {
            SCHEDULER_TYPES.put(type.getCode(), type);
        }
    }

    public static SchedulerType getSchedulerType(String typeName) {
        return Optional.ofNullable(SCHEDULER_TYPES.get(typeName))
                .orElseThrow(() -> new IllegalArgumentException("[" + typeName + "] not found"));
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

