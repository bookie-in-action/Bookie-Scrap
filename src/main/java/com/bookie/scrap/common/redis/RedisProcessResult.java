package com.bookie.scrap.common.redis;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor
public class RedisProcessResult {

    private String code;
    private Instant timestamp;
    private String message;

    public RedisProcessResult(String code) {
        this.code = code;
        this.timestamp = Instant.now();
        this.message = "";
    }

    public RedisProcessResult(String code, Throwable e) {
        this.code = code;
        this.timestamp = Instant.now();
        this.message = e.getMessage();
    }

    public RedisProcessResult(String code, Instant timestamp) {
        this.code = code;
        this.timestamp = timestamp;
        this.message = "";
    }

}
