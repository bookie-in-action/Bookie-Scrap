package com.bookie.scrap.common.redis;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class RedisProcessResultTest {

    @Test
    void testCurrentTime() {
        RedisProcessResult redisProcessResult = new RedisProcessResult("123");
        System.out.println(redisProcessResult.getTimestamp());
    }

}