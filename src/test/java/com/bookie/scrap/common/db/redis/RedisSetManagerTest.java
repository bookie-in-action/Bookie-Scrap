package com.bookie.scrap.common.db.redis;

import com.bookie.scrap.common.lifecycle.InitManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class RedisSetManagerTest {

    @BeforeAll
    static void before() {
        new InitManager().devInit();
    }

    @Test
    void addToSet() {
        RedisSetManager redisSetManager = new RedisSetManager(RedisConnectionProducer.getConn(), "test");
        Long inserted = redisSetManager.addToSet("qwerty", "qwer", "str");
        Assertions.assertEquals(3, inserted);

        Long insertedZero = redisSetManager.addToSet("qwerty");
        Assertions.assertEquals(0, insertedZero);

        redisSetManager.close();
    }

    @Test
    void get() {
        RedisSetManager redisSetManager = new RedisSetManager(RedisConnectionProducer.getConn(), "test1");
        redisSetManager.addToSet("qwerty");

        String result = redisSetManager.get();
        Assertions.assertEquals("qwerty", result);
        redisSetManager.close();
    }

}