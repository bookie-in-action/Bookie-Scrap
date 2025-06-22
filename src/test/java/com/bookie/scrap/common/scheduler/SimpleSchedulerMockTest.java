package com.bookie.scrap.common.scheduler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class SimpleSchedulerMockTest {

    @Mock
    private Scheduler scheduler;
    private DynamicJobRegistrar dynamicJobRegistrar;
    private SchedulerProperties schedulerProperties;

    @BeforeEach
    void before() {
        this.schedulerProperties = new SchedulerProperties();
        List<SchedulerProperties.SchedulerSetting> props = List.of(new SchedulerProperties.SchedulerSetting(
                "scheduler-id",
                "com.bookie.scrap.watcha.ScraperJob",
                SchedulerProperties.SchedulerSetting.Mode.CRON,
                "0 0 9 1 * ?",
                true
        ));
        this.schedulerProperties.setSchedulerSettings(props);

        this.dynamicJobRegistrar = new DynamicJobRegistrar(scheduler, schedulerProperties);
    }

    @Test
    void simpleMock() throws SchedulerException, ClassNotFoundException {
        this.dynamicJobRegistrar.registerJobs();
    }
}
