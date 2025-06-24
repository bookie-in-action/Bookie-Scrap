package com.bookie.scrap.watcha.request.user.userlikepeople;

import com.bookie.scrap.common.scheduler.SchedulerStubConfig;
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
class UserLikePeopleCollectionServiceTest {

    @Autowired
    private UserLikePeopleCollectionService service;

    @Test
    void collect() throws Exception {
        WatchaUserLikePeopleParam requestParam = new WatchaUserLikePeopleParam(1, 10);

        service.collect("86ADvGkwevzZl", requestParam);
    }
}