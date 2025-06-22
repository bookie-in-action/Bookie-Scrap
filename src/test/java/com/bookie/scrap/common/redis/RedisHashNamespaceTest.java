package com.bookie.scrap.common.redis;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Profile;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Profile("test")
class RedisHashNamespaceTest {

    private final RedisHashNamespace successBook = RedisHashNamespace.SUCCESS_BOOK;

    @Test
    void makeKey() {
        String key = successBook.makeKey("123456");
        assertEquals("success:bookcode:123456", key);
    }

    @Test
    void getPrefix() {
        assertEquals("success:bookcode", successBook.getPrefix());
    }
}