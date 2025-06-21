package com.bookie.scrap.common.domain.redis;

public enum RedisHashNamespace {
    SUCCESS_USER("success:usercode"),
    SUCCESS_BOOK("success:bookcode"),
    SUCCESS_DECK("success:deckcode"),
    FAILED_USER("failed:usercode"),
    FAILED_BOOK("failed:bookcode"),
    FAILED_DECK("failed:deckcode"),
    ;

    private final String prefix;

    RedisHashNamespace(String prefix) {
        this.prefix = prefix;
    }

    public String makeKey(String rawKey) {
        if (!rawKey.startsWith(":")) {
            rawKey = ":" + rawKey;
        }
        return prefix + rawKey;
    }

    public String getPrefix() {
        return prefix;
    }
}

