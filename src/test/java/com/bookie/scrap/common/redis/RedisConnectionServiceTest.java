package com.bookie.scrap.common.redis;

import com.bookie.scrap.common.scheduler.SchedulerStubConfig;
import com.bookie.scrap.common.springconfig.RedisConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;

import java.time.Instant;


@Slf4j
@DataRedisTest
@Import({RedisConfig.class, EmbeddedRedisTestConfig.class, SchedulerStubConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RedisConnectionServiceTest {

    @Autowired
    @Qualifier("connectionService")
    private RedisConnectionService connectionService;

    @Test
    void testSomething() {
        Assertions.assertNotNull(connectionService);
    }

    @Test
    @DisplayName("redis에서 커넥션 누적 적산 확인")
    void add() {
        int count = 100;

        String start = Instant.now().toString();
        String end = Instant.now().toString();
        for (int i = 0; i < count; i++) {
            connectionService.add();
            end = Instant.now().toString();
        }

        Assertions.assertEquals(String.valueOf(count), connectionService.getCount());

        Instant startTime = Instant.parse(connectionService.getStartTime());
        Instant endTime = Instant.parse(connectionService.getEndTime());
        Assertions.assertTrue(startTime.isBefore(endTime));

    }

//    @AfterAll
//    void tearDown() {
//        connectionService.delete();
//    }



}