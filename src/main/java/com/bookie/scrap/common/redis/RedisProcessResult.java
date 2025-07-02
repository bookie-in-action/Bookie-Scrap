package com.bookie.scrap.common.redis;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor
public class RedisProcessResult {

    private String code;
    private Instant timestamp;

    public RedisProcessResult(String code) {
        this.code = code;
        this.timestamp = Instant.now();
    }

    public RedisProcessResult(String code, Instant timestamp) {
        this.code = code;
        this.timestamp = timestamp;
    }
}
