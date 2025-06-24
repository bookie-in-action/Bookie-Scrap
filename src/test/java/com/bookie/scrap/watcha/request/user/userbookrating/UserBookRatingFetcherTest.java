package com.bookie.scrap.watcha.request.user.userbookrating;

import com.bookie.scrap.common.scheduler.SchedulerStubConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@Import(SchedulerStubConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserBookRatingFetcherTest {

    @Autowired
    UserBookRatingFetcher fetcher;

    @Test
    void fetch() throws JsonProcessingException {
        WatchaUserBookRatingParam param = new WatchaUserBookRatingParam(1, 10);
        fetcher.fetch("2mwvggAE2vMa7", param);
    }
}