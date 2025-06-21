package com.bookie.scrap.watcha.request.user.userbookrating;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
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