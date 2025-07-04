package com.bookie.legacy.common.db.redis;

import com.bookie.legacy.common.lifecycle.Shutdownable;
import com.bookie.legacy.common.properties.DbProperties;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;


public class RedisConnectionProducer implements Shutdownable {

    private static final DbProperties dbProperties = DbProperties.getInstance();

    private static final RedisClient redisClient;

    static {
        redisClient = RedisClient.create(dbProperties.getValue(DbProperties.Key.REDIS_URL));
    }
    
    public static StatefulRedisConnection<String, String> getConn() {
        return redisClient.connect();
    }

    @Override
    public void shutdown() throws Exception {
        redisClient.shutdown();
    }
}
