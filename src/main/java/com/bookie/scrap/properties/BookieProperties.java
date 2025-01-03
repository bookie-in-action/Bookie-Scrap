package com.bookie.scrap.properties;


import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Slf4j
public class BookieProperties implements InitializableProperties {

    public enum Key {
        RETRY_COUNT
    }

    private final Map<Key, String> propertyMap = new EnumMap<>(Key.class);
    private static final BookieProperties INSTANCE = new BookieProperties();
    private boolean initialized = false;

    private BookieProperties() {}

    public static BookieProperties getInstance() {
        return INSTANCE;
    }

    @Override
    public synchronized void init(String runningOption) {
        if (initialized) {
            throw new IllegalStateException("BookieProperties is already initialized");
        }

        log.info("=> Initializing BookieProperties with running option: {}", runningOption);

        try (InputStream inputStream = BookieProperties.class.getClassLoader().getResourceAsStream("bookie.properties")) {
            if (inputStream == null) {
                throw new RuntimeException("bookie.properties file not found in classpath");
            }

            Properties bookieProperties = new Properties();
            bookieProperties.load(inputStream);

//            String prefix = String.format("bookie.%s", runningOption);

//            propertyMap.put(Key.RETRY_COUNT, bookieProperties.getProperty(prefix + ".retryCount"));

            initialized = true;
//
//            log.info("<= BookieProperties initialized successfully");
//            log.info("RETRY COUNT: {}", propertyMap.get(Key.RETRY_COUNT));

        } catch (IOException e) {
            throw new RuntimeException("Failed to load bookie properties", e);
        }
    }

    @Override
    public void verify() {
        for (Key key : Key.values()) {
            if (!propertyMap.containsKey(key)) {
                throw new IllegalStateException("Missing required key: " + key + " in bookie.properties");
            }
        }
    }

    public String getValue(Key key) {
        if (!initialized) {
            throw new IllegalStateException("BookieProperties is not initialized. Please call init() first.");
        }

        return Optional.ofNullable(propertyMap.get(key))
                .orElseThrow(() -> new IllegalArgumentException("Key [" + key + "] not found in properties"));
    }

    public String getValue(Key key, String defaultValue) {
        if (!initialized) {
            throw new IllegalStateException("BookieProperties is not initialized. Please call init() first.");
        }

        return propertyMap.getOrDefault(key, defaultValue);
    }
}