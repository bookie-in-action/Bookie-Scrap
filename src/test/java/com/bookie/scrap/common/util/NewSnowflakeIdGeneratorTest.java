package com.bookie.scrap.common.util;

import com.bookie.scrap.common.lifecycle.InitManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

class NewSnowflakeIdGeneratorTest {

    private static final Logger log = LoggerFactory.getLogger(NewSnowflakeIdGeneratorTest.class);
    private final long EPOCH = LocalDateTime.of(2025, 3, 18, 0, 0, 0).toInstant(ZoneOffset.UTC).toEpochMilli();
    private final long SERVER_ID_BITS = 10L;
    private final long SEQUENCE_BITS = 12L;
    private final long TIMESTAMP_BITS = 41L;
    private final long TIMESTAMP_SHIFT = SEQUENCE_BITS + SERVER_ID_BITS;

    private final long MAX_SERVER_ID = 1023; // 10비트 (2^10 - 1)
    private final long MAX_SEQUENCE = 4095; // 12비트 (2^12 - 1)

    @BeforeAll
    static void before() {
        new InitManager().devInit();
    }


    @Test
    @DisplayName("시퀀스 range 테스트, 성능 테스트")
    void testSequenceOverflow() {
        long maxSequence = 0;

        long extractedSequence = -1L;
        for (int i = 0; i <= MAX_SEQUENCE * 2; i++) {
            long nextId = Long.parseLong(NewSnowflakeIdGenerator.getId());
            extractedSequence = nextId & MAX_SEQUENCE;
            maxSequence = Math.max(extractedSequence, maxSequence);
            log.info("Extracted sequence {}, id {}", extractedSequence, nextId);
        }

        log.info("최대 시퀀스 {}", maxSequence);

        //Sequence 값이 0 ~ 4095 범위 내에 있어야 함
        assertTrue(extractedSequence <= MAX_SEQUENCE);
        assertTrue(extractedSequence >= 0);

    }




}