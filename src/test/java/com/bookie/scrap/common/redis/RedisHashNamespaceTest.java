package com.bookie.scrap.common.redis;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
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