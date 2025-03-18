package com.bookie.scrap.common;

import com.bookie.scrap.common.util.SnowflakeIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SnowflakeIdTest {

    private static final Logger log = LoggerFactory.getLogger(SnowflakeIdTest.class);

    @Test
    public void makeSnowflakeId(){
        log.info("======================== snowflakeIdGenerator Execute =============================");
        /*
        * workerId : Snowflake ID를 생성한 서버 인스턴스의 정보
         * */

        SnowflakeIdGenerator generator = new SnowflakeIdGenerator(1); // workerId : 1로 고정

        // 5개 생성
        for(int i=0; i<5; i++) {
            log.info("[" + (i + 1)+ "] - ID : {}", generator.nextId());
        }
        log.info("======================== snowflakeIdGenerator END =============================");
    }

    private final long MAX_SEQUENCE = 4095; // 12비트 (2^12 - 1)

    @Test
    @DisplayName("시퀀스 range 테스트")
    void test1SequenceOverflow() {

        SnowflakeIdGenerator generator = new SnowflakeIdGenerator(1);

        long maxSequence = 0;
        long extractedSequence = -1L;
        for (int i = 0; i <= 100; i++) {
            long nextId = generator.nextId();
            extractedSequence = nextId & MAX_SEQUENCE;
            log.info("Extracted sequence {}, id {}", extractedSequence, nextId);
            maxSequence = Math.max(extractedSequence, maxSequence);
        }

        log.info("최대 시퀀스 {}", maxSequence);

        //Sequence 값이 0 ~ 4095 범위 내에 있어야 함
        assertTrue(extractedSequence <= MAX_SEQUENCE);
        assertTrue(extractedSequence >= 0);

    }
}
