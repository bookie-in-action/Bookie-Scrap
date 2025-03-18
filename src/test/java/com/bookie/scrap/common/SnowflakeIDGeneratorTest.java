package com.bookie.scrap.common;

import com.bookie.scrap.common.util.SnowflakeIDGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@Slf4j
class SnowflakeIDGeneratorTest {
    private static SnowflakeIDGenerator generator;

    @BeforeAll
    public static void init() {
        // 초기화
        generator = SnowflakeIDGenerator.getInstance(1, 1);
    }

    @Test
    void generateSnowflakeID() {
        long lastId = -1;

        for(int i = 0; i < 10; i++) {
        log.debug("[ Last ID ] : {}",lastId);
            long generatedID = generator.nextId();
            log.debug("[ Generated ID ] : {}",generatedID);

            Assertions.assertTrue(generatedID > lastId, "Generated ID should be greater than the last ID");
            lastId = generatedID;
        };
    }
}
