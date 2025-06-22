package com.bookie.scrap.common.redis;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

public class RedisHashService {

    private final RedisTemplate<String, RedisProcessResult> stringRedisTemplate;
    private final RedisHashNamespace namespace;

    public RedisHashService(RedisTemplate<String, RedisProcessResult> stringRedisTemplate, RedisHashNamespace hashNamespace) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.namespace = hashNamespace;
    }

    private HashOperations<String, String, RedisProcessResult> hashOps() {
        return stringRedisTemplate.opsForHash();
    }

    public int add(RedisProcessResult redisResult) {
        if (redisResult == null) {
            return 0;
        }

        hashOps().put(namespace.getPrefix(), redisResult.getCode(), redisResult);
        return 1;
    }

    public RedisProcessResult get(String code) {
        return hashOps().get(namespace.getPrefix(), code);
    }

    public List<RedisProcessResult> get(String... code) {
        return hashOps().multiGet(namespace.getPrefix(), List.of(code));
    }

    public void delete(String code) {
        hashOps().delete(namespace.getPrefix(), code);
    }

    public void delete(String... code) {
        hashOps().delete(namespace.getPrefix(), List.of(code));
    }

    public boolean exist(String code) {
        return hashOps().hasKey(namespace.getPrefix(), code);
    }
}

