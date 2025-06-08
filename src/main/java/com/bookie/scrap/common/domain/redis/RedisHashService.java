package com.bookie.scrap.common.domain.redis;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

public class RedisHashService {

    private final RedisTemplate<String, RedisProcessResult> stringRedisTemplate;
    private final RedisHashNamespace listPrefix;

    public RedisHashService(RedisTemplate<String, RedisProcessResult> stringRedisTemplate, RedisHashNamespace listPrefix) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.listPrefix = listPrefix;
    }

    private HashOperations<String, String, RedisProcessResult> hashOps() {
        return stringRedisTemplate.opsForHash();
    }

    public int add(RedisProcessResult redisResult) {
        if (redisResult == null) {
            return 0;
        }

        hashOps().put(listPrefix.getPrefix(), redisResult.getCode(), redisResult);
        return 1;
    }

    public RedisProcessResult get(String code) {
        return hashOps().get(listPrefix.getPrefix(), code);
    }

    public List<RedisProcessResult> get(String... code) {
        return hashOps().multiGet(listPrefix.getPrefix(), List.of(code));
    }

    public void delete(String code) {
        hashOps().delete(listPrefix.getPrefix(), code);
    }

    public void delete(String... code) {
        hashOps().delete(listPrefix.getPrefix(), List.of(code));
    }

    public boolean exist(String code) {
        return hashOps().hasKey(listPrefix.getPrefix(), code);
    }
}

