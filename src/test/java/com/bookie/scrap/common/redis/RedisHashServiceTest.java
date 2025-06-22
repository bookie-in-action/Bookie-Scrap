package com.bookie.scrap.common.redis;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Profile("test")
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

    @MethodSource("provideTestCodes")
    @ParameterizedTest(name = "bookCode={0} 저장 및 조회 테스트")
    @DisplayName("Redis에 저장된 RedisProcessResult의 타임스탬프와 값 검증")
    void testSaveAndVerifyRedisProcessResult(String bookCode) {

        service.delete(bookCode);
        assertFalse(service.exist(bookCode), "테스트 시작 전 redis에 code가 존재하지 않아야 함");

        LocalDateTime now = LocalDateTime.now();
        RedisProcessResult redisProcessResult = new RedisProcessResult(bookCode, now);

        int addResult = service.add(redisProcessResult);
        assertEquals(1, addResult, "추가 결과가 1이어야 함");
        assertTrue(service.exist(bookCode), "redis에 code가 존재해야 함");

        RedisProcessResult result = service.get(bookCode);
        assertNotNull(result, "조회된 결과가 null이 아니어야 함");
        assertAll(
                () -> assertEquals(now, result.getTimestamp(), "타임스탬프 불일치"),
                () -> assertEquals(bookCode, result.getCode(), "value 불일치")
        );
    }

    @ParameterizedTest(name = "bookCode={0} 다중 조회 테스트")
    @MethodSource("provideTestCodes")
    @DisplayName("Redis에 저장된 RedisProcessResult의 다중 조회 검증")
    void testMultiGet(String bookCode) {
        // 테스트 전 기존 데이터 삭제
        service.delete(bookCode);

        LocalDateTime now = LocalDateTime.now();
        RedisProcessResult redisProcessResult = new RedisProcessResult(bookCode, now);

        service.add(redisProcessResult);
        assertTrue(service.exist(bookCode), "redis에 code가 존재해야 함");

        // 다중 조회 메서드 사용 (가변인자로 하나만 전달)
        List<RedisProcessResult> results = service.get(new String[]{bookCode});
        assertNotNull(results, "조회된 결과 리스트가 null이 아니어야 함");
        assertEquals(1, results.size(), "결과 리스트 크기가 1이어야 함");

        RedisProcessResult result = results.get(0);
        assertNotNull(result, "조회된 결과가 null이 아니어야 함");
        assertAll(
                () -> assertEquals(now, result.getTimestamp(), "타임스탬프 불일치"),
                () -> assertEquals(bookCode, result.getCode(), "value 불일치")
        );
    }

    @ParameterizedTest(name = "bookCode={0} 삭제 테스트")
    @MethodSource("provideTestCodes")
    @DisplayName("Redis에 저장된 RedisProcessResult 삭제 검증")
    void testDelete(String bookCode) {
        LocalDateTime now = LocalDateTime.now();
        RedisProcessResult redisProcessResult = new RedisProcessResult(bookCode, now);

        service.add(redisProcessResult);
        assertTrue(service.exist(bookCode), "redis에 code가 존재해야 함");

        service.delete(bookCode);
        assertFalse(service.exist(bookCode), "삭제 후 redis에 code가 존재하지 않아야 함");
    }

    @AfterAll
    void tearDown() {
        for (String code : TEST_CODES) {
            if (service.exist(code)) {
                service.delete(code);
            }
        }
    }
}
