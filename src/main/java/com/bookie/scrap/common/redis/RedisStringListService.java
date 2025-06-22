package com.bookie.scrap.common.redis;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;


public class RedisStringListService {

    private final RedisTemplate<String, String> stringRedisTemplate;
    private final RedisStringListNamespace namespace;

    public RedisStringListService(RedisTemplate<String, String> stringRedisTemplate, RedisStringListNamespace namespace) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.namespace = namespace;
    }

    private ListOperations<String, String> ops() {
        return stringRedisTemplate.opsForList();
    }

    public int add(String code) {
        ops().leftPush(namespace.getPrefix(), code);
        return 1;
    }

    public int add(List<String> code) {
        ops().leftPushAll(namespace.getPrefix(), code);
        return 1;
    }

    public String pop() {
        return ops().rightPop(namespace.getPrefix());
    }

    public long size() {
        Long size = ops().size(namespace.getPrefix());

        if (size == null) {
            return 0;
        }

        return size;
    }

}

