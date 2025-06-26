package com.bookie.scrap;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
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