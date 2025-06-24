package com.bookie.scrap.common.redis;

public enum RedisStringListNamespace {
    USER("usercode"),
    BOOK("bookcode"),
    DECK("deckcode"),
    SUCCESS_USER("success:usercode"),
    SUCCESS_BOOK("success:bookcode"),
    SUCCESS_DECK("success:deckcode"),
    ;

    private final String prefix;

    RedisStringListNamespace(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}

