package com.bookie.scrap.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class DatabaseConnectionPool {

    @Getter
    private static HikariDataSource dataSource;

    public static void init(String runningOption) {

        if (dataSource != null) {
            return;
        }

        log.info("=> HiKariDataSource init start");

        try (InputStream inputStream = DatabaseConnectionPool.class.getClassLoader().getResourceAsStream("db.properties")) {
            Properties dbProperties = new Properties();
            dbProperties.load(inputStream);

            String prefix = String.format("db.%s", runningOption);

            String jdbcUrl = dbProperties.getProperty(prefix + ".url");
            String user = dbProperties.getProperty(prefix + ".user");
            String password = dbProperties.getProperty(prefix + ".password");
            String driverName = dbProperties.getProperty(prefix + ".driver");

            log.info("DB URL: {}", jdbcUrl);
            log.info("DB USER: {}", user);
            log.info("DB DRIVER: {}", driverName);

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(jdbcUrl);
            config.setUsername(user);
            config.setPassword(password);
            config.setDriverClassName(driverName);

            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setIdleTimeout(30000);
            config.setConnectionTimeout(30000);
            config.setLeakDetectionThreshold(5000);

            dataSource = new HikariDataSource(config);

            log.info("=> HiKariDataSource init complete");

        } catch (IOException e) {
            throw new RuntimeException("Failed to load database properties", e);
        }


    }

    public static void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
