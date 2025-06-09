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


import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RedisStringListServiceTest {

    @Autowired
    @Qualifier("bookCodeList")
    private RedisStringListService service;

    private static final String[] TEST_CODES = {"byLKj8M", "testCode123", "ABC987xyz"};

    static Stream<String> provideTestCodes() {
        return Stream.of(TEST_CODES);
    }

    @ParameterizedTest(name = "bookCode={0} 추가 및 조회 테스트")
    @MethodSource("provideTestCodes")
    @DisplayName("Redis에 문자열 리스트 추가 및 크기 검증")
    void testAddAndVerifySize(String bookCode) {
        long initialSize = service.size();

        service.add(bookCode);

        assertEquals(initialSize + 1, service.size(), "리스트 크기가 1 증가해야 함");
    }

    @ParameterizedTest(name = "bookCode={0} 추가 및 팝 테스트")
    @MethodSource("provideTestCodes")
    @DisplayName("Redis에 문자열 리스트 추가 후 팝 검증")
    void testAddAndPop(String bookCode) {
        service.add(bookCode);

        long initialSize = service.size();

        String poppedCode = service.pop();
        Assertions.assertAll(
                () -> assertNotNull(poppedCode, "팝된 값이 null이 아니어야 함"),
                () -> assertEquals(poppedCode, bookCode, "팝된 값과 넣은 값이 같아야 함"),
                () -> assertEquals(initialSize - 1, service.size(), "리스트 크기가 1 감소해야 함")
        );
    }

    @AfterAll
    void tearDown() {
        while (service.size() > 0) {
            service.pop();
        }
    }
}
