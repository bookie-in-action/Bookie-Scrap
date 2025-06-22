package com.bookie.scrap.watcha.request.user.userlikepeople;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

@Slf4j
@Profile("test")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserLikePeopleCollectionServiceTest {

    @Autowired
    private UserLikePeopleCollectionService service;

    @Test
    void collect() throws Exception {
        WatchaUserLikePeopleParam requestParam = new WatchaUserLikePeopleParam(1, 10);

        service.collect("86ADvGkwevzZl", requestParam);
    }
}