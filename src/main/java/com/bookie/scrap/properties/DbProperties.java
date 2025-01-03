package com.bookie.scrap.properties;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

@Slf4j
public class DbProperties {
    private Map<key, String> dbProps;
    public enum key{DRIVER_NAME, USER, JDBC_URL, PASSWORD};

    private static final DbProperties INSTANCE = new DbProperties();

    private DbProperties() {}

    public void init(String runningOption) {

        if (dbProps != null) {
            throw new IllegalStateException("Already Initialized");
        }

        log.info("=> Db Properties initialize start");
        try (InputStream inputStream = DbProperties.class.getClassLoader().getResourceAsStream("db.properties")) {
            Properties dbProperties = new Properties();
            dbProperties.load(inputStream);

            String prefix = String.format("db.%s", runningOption);

            String jdbcUrl = dbProperties.getProperty(prefix + ".url");
            String user = dbProperties.getProperty(prefix + ".user");
            String password = dbProperties.getProperty(prefix + ".password");
            String driverName = dbProperties.getProperty(prefix + ".driver");

            dbProps = new HashMap<>();
            dbProps.put(key.DRIVER_NAME, driverName);
            dbProps.put(key.PASSWORD, password);
            dbProps.put(key.USER, user);
            dbProps.put(key.JDBC_URL, jdbcUrl);

            log.info("DB URL: {}", jdbcUrl);
            log.info("DB USER: {}", user);
            log.info("DB DRIVER: {}", driverName);

            log.info("<= Db Properties initialize success");

        } catch (IOException e) {
            throw new RuntimeException("Failed to load database properties", e);
        }

    }

    public String getValue(DbProperties.key key) {
        return Optional.ofNullable(dbProps.get(key))
                .orElseThrow(() -> new IllegalArgumentException("[" + key + "] not found in db.properties"));
    }

    public String getValue(DbProperties.key key, String defaultValue) {
        return dbProps.getOrDefault(key, defaultValue);
    }

}