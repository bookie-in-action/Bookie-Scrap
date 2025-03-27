package com.bookie.scrap.common.db.redis;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class RedisSetManager implements AutoCloseable{

    private final StatefulRedisConnection<String, String> connect;
    private final RedisCommands<String, String> commands ;
    private final String key;

    public RedisSetManager(StatefulRedisConnection<String, String> connect, String key) {
        this.connect = connect;
        this.commands  = connect.sync();
        this.key = key;
    }

    public Long addToSet(String... values) {
        return commands.sadd(key, values);
    }

    public Long addToSet(List<String> values) {
        return commands.sadd(key, values.toArray(String[]::new));
    }

    public Long deleteItem(String value) {
        return commands.srem(key, value);
    }

    public Long deleteItem(List<String> values) {
        return commands.srem(key, values.toArray(String[]::new));
    }

    public boolean isExist(String value) {
        return commands.sismember(key, value);
    }

    public Long size() {
        return commands.scard(key);
    }

    public String get() {
        return commands.srandmember(key);
    }

    @Override
    public void close() {
        if (connect != null) {
            this.deleteKey();
            connect.close();
        }
    }

    public void deleteKey() {
        Long deletedNum = commands.del(this.key);
        log.info("Redis key: {} deleted - {}", this.key, deletedNum);
    }
}
