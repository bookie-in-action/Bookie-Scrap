package com.bookie.scrap.common.redis;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.*;


public class RedisConnectionService {

    private final RedisTemplate<String, String> redisTemplate;
    private final RedisValueNamespace namespace;

    public RedisConnectionService(RedisTemplate<String, String> redisTemplate, RedisValueNamespace namespace) {
        this.redisTemplate = redisTemplate;
        this.namespace = namespace;
    }

    private HashOperations<String, String, String> hashOps() {
        return redisTemplate.opsForHash();
    }

    public void add() {
        if (!redisTemplate.hasKey(namespace.getPrefix())) {
            hashOps().put(namespace.getPrefix(), "startTime", ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toString());
        }

        hashOps().put(namespace.getPrefix(), "endTime", ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toString());

        redisTemplate.opsForHash().increment(namespace.getPrefix(), "count", 1);
    }

    public String getStartTime() {
        return hashOps().get(namespace.getPrefix(), "startTime");
    }

    public String getEndTime() {
        return hashOps().get(namespace.getPrefix(), "endTime");
    }

    public String getCount() {
        return hashOps().get(namespace.getPrefix(), "count");
    }

    public void delete() {
        redisTemplate.delete(namespace.getPrefix());
    }

}

