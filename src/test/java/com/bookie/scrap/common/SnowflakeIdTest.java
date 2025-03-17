package com.bookie.scrap.common;

import com.bookie.scrap.common.util.SnowflakeIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
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
}
