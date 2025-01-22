package com.bookie.scrap.properties;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Slf4j
public class DbProperties implements InitializableProperties {

    public enum Key {
        DRIVER_NAME, USER, JDBC_URL, PASSWORD
    }

    private final Map<Key, String> propertyMap = new EnumMap<>(Key.class);
    private static final DbProperties INSTANCE = new DbProperties();
    private boolean initialized = false;

    private DbProperties() {}

    public static DbProperties getInstance() {
        return INSTANCE;
    }

    public synchronized void init(Map<Key, String> testProperties) {
        if (initialized) {
            throw new IllegalStateException("DbProperties is already initialized");
        }
        propertyMap.putAll(testProperties);
        initialized = true;
    }

    @Override
    public synchronized void init(String runningOption) {

        if (initialized) {
            throw new IllegalStateException("DbProperties is already initialized");
        }

        log.info("=> Initializing DbProperties with running option: {}", runningOption);

        try (InputStream inputStream = DbProperties.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (inputStream == null) {
                throw new RuntimeException("db.properties file not found in classpath");
            }

            Properties dbProperties = new Properties();
            dbProperties.load(inputStream);

            String prefix = String.format("db.%s", runningOption);

            propertyMap.put(Key.DRIVER_NAME, dbProperties.getProperty(prefix + ".driver"));
            propertyMap.put(Key.USER, dbProperties.getProperty(prefix + ".user"));
            propertyMap.put(Key.JDBC_URL, dbProperties.getProperty(prefix + ".url"));
            propertyMap.put(Key.PASSWORD, dbProperties.getProperty(prefix + ".password"));

            // 초기화 상태 플래그 업데이트
            initialized = true;

            log.info("DB URL: {}", propertyMap.get(Key.JDBC_URL));
            log.info("DB USER: {}", propertyMap.get(Key.USER));
            log.info("DB DRIVER: {}", propertyMap.get(Key.DRIVER_NAME));
            log.info("DB PASSWORD: {}", propertyMap.get(Key.PASSWORD));

            log.info("<= DbProperties initialized successfully");

        } catch (IOException e) {
            throw new RuntimeException("Failed to load database properties", e);
        }
    }

    @Override
    public void verify() {
        for (Key key : Key.values()) {
            if (!propertyMap.containsKey(key)) {
                throw new IllegalStateException("Missing required key: " + key + " in db.properties");
            }
        }
    }

    public String getValue(Key key) {
        if (!initialized) {
            throw new IllegalStateException("DbProperties is not initialized. Please call init() first.");
        }

        return Optional.ofNullable(propertyMap.get(key))
                .orElseThrow(() -> new IllegalArgumentException("Key [" + key + "] not found in properties"));
    }

    public String getValue(Key key, String defaultValue) {
        if (!initialized) {
            throw new IllegalStateException("DbProperties is not initialized. Please call init() first.");
        }

        return propertyMap.getOrDefault(key, defaultValue);
    }

}