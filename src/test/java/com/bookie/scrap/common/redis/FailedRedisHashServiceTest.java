package com.bookie.scrap.common.redis;

import com.bookie.scrap.common.exception.WatchaCustomCollectionEx;
import com.bookie.scrap.common.scheduler.SchedulerStubConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@Import(SchedulerStubConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FailedRedisHashServiceTest {

    @Autowired
    @Qualifier("failedBookCode")
    private RedisHashService failedBookCode;

    @Test
    void find_redis_failed_result() {

        String errorMsg = "this is test";
        String code = "testtest";

        if (failedBookCode.exist(code)) {
            failedBookCode.delete(code);
        }

        try {
            throw new WatchaCustomCollectionEx(errorMsg);
        } catch (Exception e) {
            failedBookCode.add(new RedisProcessResult(code, e));
        }

        RedisProcessResult redisProcessResult = failedBookCode.get(code);
        Assertions.assertEquals(redisProcessResult.getMessage(), errorMsg);
    }
}
