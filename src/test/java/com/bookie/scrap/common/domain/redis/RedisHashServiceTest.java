package com.bookie.scrap.common.domain.redis;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RedisHashServiceTest {

    @Autowired
    @Qualifier("successBookCode")
    private RedisHashService service;

    private static final String[] TEST_CODES = {"byLKj8M", "testCode123", "ABC987xyz"};

    static Stream<String> provideTestCodes() {
        return Stream.of(TEST_CODES);
    }

    @ParameterizedTest(name = "bookCode={0} 저장 및 조회 테스트")
    @MethodSource("provideTestCodes")
    @DisplayName("Redis에 저장된 RedisProcessResult의 타임스탬프와 값 검증")
    void testSaveAndVerifyRedisProcessResult(String bookCode) {

        LocalDateTime now = LocalDateTime.now();
        RedisProcessResult redisProcessResult = new RedisProcessResult(bookCode, now);

        service.add(redisProcessResult);
        assertTrue(service.exist(bookCode), "redis에 code가 존재");

        RedisProcessResult result = service.get(bookCode);
        Assertions.assertAll(
                () -> assertEquals(now, result.getTimestamp(), "타임스탬프 불일치"),
                () -> assertEquals(bookCode, result.getCode(), "value 불일치")
        );
    }
    
    @AfterAll
    void tearDown() {
        service.delete(TEST_CODES);
    }
}