package com.bookie.scrap.common.domain.redis;

public enum RedisStringListNamespace {
    USER("usercode"),
    BOOK("bookcode"),
    DECK("deckcode"),
    ;

    private final String prefix;

    RedisStringListNamespace(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}

