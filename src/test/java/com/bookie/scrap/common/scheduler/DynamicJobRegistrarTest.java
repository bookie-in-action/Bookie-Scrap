package com.bookie.scrap.common.scheduler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.*;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.quartz.impl.triggers.SimpleTriggerImpl;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DynamicJobRegistrarTest {

    @Mock
    private Scheduler scheduler;

    private Map<String, Trigger> triggerMap;

    @BeforeEach
    void setUp() throws Exception {
        SchedulerProperties props = new SchedulerProperties();
        SchedulerProperties.SchedulerSetting cronSetting = new SchedulerProperties.SchedulerSetting();
        cronSetting.setEnabled(true);
        cronSetting.setJobName("cronJob");
        cronSetting.setMode(SchedulerProperties.SchedulerSetting.Mode.CRON);
        cronSetting.setJobClass(TestJob.class.getName());
        cronSetting.setExpression("0/5 * * * * ?");

        SchedulerProperties.SchedulerSetting intervalSetting = new SchedulerProperties.SchedulerSetting();
        intervalSetting.setEnabled(true);
        intervalSetting.setJobName("intervalJob");
        intervalSetting.setMode(SchedulerProperties.SchedulerSetting.Mode.INTERVAL);
        intervalSetting.setJobClass(TestJob.class.getName());
        intervalSetting.setExpression("5");

        props.setSchedulerSettings(List.of(cronSetting, intervalSetting));
        DynamicJobRegistrar dynamicJobRegistrar = new DynamicJobRegistrar(scheduler, props);

        // capture 설정
        ArgumentCaptor<Trigger> triggerCaptor = ArgumentCaptor.forClass(Trigger.class);
        dynamicJobRegistrar.registerJobs();
        verify(scheduler, times(2)).scheduleJob(any(JobDetail.class), triggerCaptor.capture());

        triggerMap = triggerCaptor.getAllValues().stream()
                .collect(Collectors.toMap(t -> t.getKey().getName(), Function.identity()));
    }


    @Test
    void cronTriggerTest() {
        Trigger cronTrigger = triggerMap.get("cronJob");
        assertThat(cronTrigger).isInstanceOf(CronTriggerImpl.class);
        assertThat(((CronTriggerImpl) cronTrigger).getCronExpression()).isEqualTo("0/5 * * * * ?");
    }

    @Test
    void intervalTriggerTest() {
        Trigger intervalTrigger = triggerMap.get("intervalJob");
        assertThat(intervalTrigger).isInstanceOf(SimpleTriggerImpl.class);
        assertThat(((SimpleTriggerImpl) intervalTrigger).getRepeatInterval()).isEqualTo(5000L);
    }


    public static class TestJob implements Job {
        @Override
        public void execute(JobExecutionContext context) {
            // no-op
        }
    }

}