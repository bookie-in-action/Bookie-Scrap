package com.bookie.scrap.common.properties;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static com.bookie.scrap.common.properties.BookieProperties.Key.RETRY_COUNT;
import static com.bookie.scrap.common.properties.BookieProperties.Key.SERVER_ID;

@Slf4j
public class BookieProperties implements InitializableProperties {

    @RequiredArgsConstructor
    public enum Key {
        RETRY_COUNT("retry.count", "3"),
        SERVER_ID("server.id", "0"),
        THREAD_SLEEP("thread.sleep", "500")
        ;

        private final String value;
        private final String defaultValue;
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
            log.debug("BookieProperties is already initialized");
            return;
        }

        log.info("=> Initializing BookieProperties");

        try (InputStream inputStream = BookieProperties.class.getClassLoader().getResourceAsStream("bookie.properties")) {
            if (inputStream == null) {
                throw new RuntimeException("bookie.properties file not found in classpath");
            }

            Properties bookieProperties = new Properties();
            bookieProperties.load(inputStream);

            log.info("============ [BOOKIE PROPERTIES] ============");
            propertyMap.put(RETRY_COUNT, bookieProperties.getProperty(RETRY_COUNT.value, RETRY_COUNT.defaultValue));
            String serverId = bookieProperties.getProperty(SERVER_ID.value, SERVER_ID.defaultValue);
            if (serverId.isBlank()) {
                throw new RuntimeException("server.id in bookie.properties is empty");
            }
            propertyMap.put(Key.SERVER_ID, serverId);

            initialized = true;

            log.info("RETRY MAX: {}", propertyMap.get(RETRY_COUNT));
            log.info("SERVER ID: {}", propertyMap.get(Key.SERVER_ID));
            log.info("=============================================");

            log.info("<= BookieProperties initialized successfull");


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

}