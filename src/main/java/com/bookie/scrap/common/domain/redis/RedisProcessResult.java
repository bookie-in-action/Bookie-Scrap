package com.bookie.scrap.common.domain.redis;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@NoArgsConstructor
public class RedisProcessResult {

    private String code;
    private LocalDateTime timestamp;

    public RedisProcessResult(String code) {
        this.code = code;
        this.timestamp = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }

    public RedisProcessResult(String code, LocalDateTime timestamp) {
        this.code = code;
        this.timestamp = timestamp;
    }
}
