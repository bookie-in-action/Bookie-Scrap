package com.bookie.legacy.common.properties;

import com.bookie.legacy.common.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Slf4j
public class DbProperties implements InitializableProperties {

    @RequiredArgsConstructor
    public enum Key {
        DRIVER_NAME(".driver", ""), JDBC_URL(".url", ""), USER(".user", ""),PASSWORD(".password", ""),
        MAX_POOL(".poolsize", "10"),
        SHOW_SQL(".showsql", "false"), FORMAT_SQL(".formatsql", "false"), HBM2DDL_AUTO(".ddl_auto", "none"),

        REDIS_URL(".url", "");

        private final String suffix;
        private final String defaultValue;
    }

    private final Map<Key, String> PROPERTY_MAP = new EnumMap<>(Key.class);
    private static final DbProperties INSTANCE = new DbProperties();
    private boolean initialized = false;

    private DbProperties() {}

    public static DbProperties getInstance() {
        return INSTANCE;
    }

    @Override
    public synchronized void init(String runningOption) {

        if (initialized) {
            log.debug("DbProperties is already initialized");
            return;
        }

        log.info("=> Initializing DbProperties with running option: {}", runningOption);

        try (InputStream inputStream = DbProperties.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (inputStream == null) {
                throw new RuntimeException("db.properties file not found in classpath");
            }

            Properties dbProperties = new Properties();
            dbProperties.load(inputStream);

            String prefix = String.format("db.%s", runningOption);

            log.info("============ [DB PROPERTIES: {}] ============", runningOption.toUpperCase());
            int paddingLength = 15;

            for(Key key : Key.values()) {

                if (key.equals(Key.REDIS_URL)) {
                    continue;
                }

                String fileLoadedValue = dbProperties.getProperty(prefix + key.suffix, key.defaultValue);

                if (StringUtil.hasText(fileLoadedValue)) {
                    PROPERTY_MAP.put(key, fileLoadedValue);
                }

                // 로그 길이 맞추기
                String formattedKey = String.format("%-" + paddingLength + "s", key.name());
                log.info("DB {}: {}", formattedKey, PROPERTY_MAP.get(key));
            }

            String redisPrefix = String.format("redis.%s", runningOption);
            String redisValue = dbProperties.getProperty(redisPrefix + Key.REDIS_URL.suffix);
            if (StringUtil.hasText(redisValue)) {
                PROPERTY_MAP.put(Key.REDIS_URL, redisValue);
            }

            String formattedKey = String.format("%-" + paddingLength + "s", Key.REDIS_URL.name());
            log.info("DB {}: {}", formattedKey, PROPERTY_MAP.get(Key.REDIS_URL));

            log.info("==============================================");

            // 초기화 상태 플래그 업데이트
            initialized = true;

            log.info("<= DbProperties initialized successfully");

        } catch (IOException e) {
            throw new RuntimeException("Failed to load database properties", e);
        }
    }

    @Override
    public void verify() {
        for (Key key : Key.values()) {
            if (!PROPERTY_MAP.containsKey(key)) {
                throw new IllegalStateException("Missing required key: " + key + " in db.properties");
            }
        }
    }

    public String getValue(Key key) {
        if (!initialized) {
            throw new IllegalStateException("DbProperties is not initialized. Please call init() first.");
        }

        return Optional.ofNullable(PROPERTY_MAP.get(key))
                .orElseThrow(() -> new IllegalArgumentException("Key [" + key + "] not found in properties"));
    }

}