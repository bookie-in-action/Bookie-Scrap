package com.bookie.scrap.properties;


import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

@Slf4j
public class BookieProperties {
    private Map<key, String> bookieProps;
    public enum key{};

    private static final BookieProperties INSTANCE = new BookieProperties();

    private BookieProperties() {}

    public void init(String runningOption) {

        if (bookieProps != null) {
            throw new IllegalStateException("Already Initialized");
        }

        log.info("=> Bookie Properties initialize start");
        try (InputStream inputStream = BookieProperties.class.getClassLoader().getResourceAsStream("bookie.properties")) {
            Properties bookieProperties = new Properties();
            bookieProperties.load(inputStream);

            bookieProps = new HashMap<>();

            log.info("<= Bookie Properties initialize success");

        } catch (IOException e) {
            throw new RuntimeException("Failed to load bookie properties", e);
        }

    }

    public String getValue(BookieProperties.key key) {
        return Optional.ofNullable(bookieProps.get(key))
                .orElseThrow(() -> new IllegalArgumentException("[" + key + "] not found in bookie.properties"));
    }

    public String getValue(BookieProperties.key key, String defaultValue) {
        return bookieProps.getOrDefault(key, defaultValue);
    }

}
