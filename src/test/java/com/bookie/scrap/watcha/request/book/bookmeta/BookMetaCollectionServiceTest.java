package com.bookie.scrap.watcha.request.book.bookmeta;

import com.bookie.scrap.common.exception.WatchaCustomCollectionEx;
import com.bookie.scrap.common.redis.RedisHashService;
import com.bookie.scrap.common.scheduler.SchedulerStubConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@Import(SchedulerStubConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookMetaCollectionServiceTest {

    @Autowired
    BookMetaCollectionService service;

    @Test
    void collect() throws Exception {
        WatchaBookMetaParam param = new WatchaBookMetaParam(1, 10);
        service.collect("byLKj8M", param);
    }

    @Test
    @DisplayName("content_type이 book이 아닌 경우")
    void getWatchaCustomException() {
        WatchaBookMetaParam param = new WatchaBookMetaParam(1, 1);
        Assertions.assertThrows(Exception.class, () -> service.collect("tEQ7A9P", param));
        Assertions.assertThrows(WatchaCustomCollectionEx.class, () -> service.collect("tEQ7A9P", param));
    }


}