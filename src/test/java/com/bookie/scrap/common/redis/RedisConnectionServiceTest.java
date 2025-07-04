package com.bookie.scrap.common.redis;

import com.bookie.scrap.common.scheduler.SchedulerStubConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.time.Instant;


@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@Import({SchedulerStubConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RedisConnectionServiceTest {

    @Autowired
    @Qualifier("connectionService")
    private RedisConnectionService connectionService;

    @BeforeAll
    void startup() {
        connectionService.delete();
    }

    @Test
    void testSomething() {
        Assertions.assertNotNull(connectionService);
    }

    @Test
    @DisplayName("redis에서 커넥션 누적 적산 확인")
    void add() {
        int count = 100;
        String end;
        String start = Instant.now().toString();
        for (int i = 0; i < count; i++) {
            connectionService.add();
            end = Instant.now().toString();
        }

        Assertions.assertEquals(String.valueOf(count), connectionService.getCount());

        Instant startTime = Instant.parse(connectionService.getStartTime());
        Instant endTime = Instant.parse(connectionService.getEndTime());
        Assertions.assertTrue(startTime.plus(Duration.ofMillis(2)).isBefore(endTime));

    }

    @AfterAll
    void tearDown() {
        connectionService.delete();
    }

}