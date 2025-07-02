package com.bookie.scrap.watcha;

import com.bookie.scrap.common.scheduler.SchedulerStubConfig;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@Tag("manual")
@SpringBootTest
@ActiveProfiles("test")
@Import(SchedulerStubConfig.class)
class ScraperJobTest {

    @Autowired
    ScraperJob scraperJob;

    @Test
    void testScraperJob() {
        scraperJob.execute();
    }

}