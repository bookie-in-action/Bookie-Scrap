package com.bookie.scrap.watcha.request.user.userbookrating;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Profile("test")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserBookRatingCollectionServiceTest {

    @Autowired UserBookRatingCollectionService service;

    @Test
    void collect() throws Exception {
        WatchaUserBookRatingParam param = new WatchaUserBookRatingParam(1, 10);
        service.collect("2mwvggAE2vMa7", param);
    }
}