package com.bookie.scrap.common.redis;

public enum RedisValueNamespace {
    Connection("connect:stat"),
    ;

    private final String prefix;

    RedisValueNamespace(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}

