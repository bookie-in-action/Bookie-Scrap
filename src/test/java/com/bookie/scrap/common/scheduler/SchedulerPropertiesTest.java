package com.bookie.scrap.common.scheduler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TestSchedulerPropertiesConfig.class)
@TestPropertySource(properties = {
        "bookie.scheduler-settings[0].enabled=true",
        "bookie.scheduler-settings[0].jobName=testJob",
        "bookie.scheduler-settings[0].mode=cron",
        "bookie.scheduler-settings[0].jobClass=com.example.jobs.TestJob",
        "bookie.scheduler-settings[0].expression=0/5 * * * * ?",

        "bookie.scheduler-settings[1].enabled:true",
        "bookie.scheduler-settings[1].jobName=testJob2",
        "bookie.scheduler-settings[1].mode=interval",
        "bookie.scheduler-settings[1].jobClass=com.example.jobs.AnotherJob",
        "bookie.scheduler-settings[1].expression=5"
})
class SchedulerPropertiesTest {

    @Autowired
    private SchedulerProperties schedulerProperties;

    @Test
    void propertiesShouldBindCorrectly() {
        List<SchedulerProperties.SchedulerSetting> settings = schedulerProperties.getSchedulerSettings();
        assertThat(settings).hasSize(2);

        SchedulerProperties.SchedulerSetting cronSetting = settings.get(0);
        assertThat(cronSetting.getJobName()).isEqualTo("testJob");
        assertThat(cronSetting.getMode()).isEqualTo(SchedulerProperties.SchedulerSetting.Mode.CRON);
        assertThat(cronSetting.getExpression()).isEqualTo("0/5 * * * * ?");
        assertThat(cronSetting.getJobClass()).isEqualTo("com.example.jobs.TestJob");

        SchedulerProperties.SchedulerSetting intervalSetting = settings.get(1);
        assertThat(intervalSetting.getJobName()).isEqualTo("testJob2");
        assertThat(intervalSetting.getMode()).isEqualTo(SchedulerProperties.SchedulerSetting.Mode.INTERVAL);
        assertThat(intervalSetting.getExpression()).isEqualTo("5");
        assertThat(intervalSetting.getJobClass()).isEqualTo("com.example.jobs.AnotherJob");
    }
}

@Configuration
@EnableConfigurationProperties(SchedulerProperties.class)
class TestSchedulerPropertiesConfig {
}
