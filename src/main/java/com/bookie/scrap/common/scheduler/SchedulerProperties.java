package com.bookie.scrap.common.scheduler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.quartz.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Component
@Getter @Setter
@ConfigurationProperties(prefix = "bookie")
public class SchedulerProperties {

    private List<SchedulerSetting> schedulerSettings;

    @Getter @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SchedulerSetting {
        private String jobName;
        private String jobClass;
        private Mode mode;
        private String expression;
        private boolean enabled;

        public Function<String, ScheduleBuilder<? extends Trigger>> getScheduleBuilder() {
            return mode.getBuilder();
        }

        @AllArgsConstructor
        public enum Mode {
            CRON(CronScheduleBuilder::cronSchedule),
            INTERVAL(expr -> SimpleScheduleBuilder.simpleSchedule()
                    .withIntervalInSeconds(Integer.parseInt(expr))
                    .repeatForever());

            private final Function<String, ScheduleBuilder<? extends Trigger>> builder;

            public Function<String, ScheduleBuilder<? extends Trigger>> getBuilder() {
                return builder;
            }
        }
    }

}
