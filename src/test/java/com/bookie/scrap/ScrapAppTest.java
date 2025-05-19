package com.bookie.scrap;

import com.bookie.scrap.common.SchedulerProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@TestPropertySource(properties = {
        "bookie.scheduler-settings[0].enabled=false"
})
class ScrapAppTest {

    @SpyBean
    private ScrapApp scrapApp;

    @Test
    void contextLoads() {
    }

    @Test
    void runMethodShouldBeCalled() throws Exception {
        verify(scrapApp, times(1)).run();
    }

}