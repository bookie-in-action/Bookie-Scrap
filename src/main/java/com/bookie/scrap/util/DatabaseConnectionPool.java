package com.bookie.scrap.util;

import com.bookie.scrap.properties.DbProperties;
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

    public static void init() {

        if (dataSource != null) {
            return;
        }
        DbProperties dbProperties = DbProperties.getInstance();
        log.info("=> HiKariDataSource init start");

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dbProperties.getValue(DbProperties.Key.JDBC_URL));
        config.setUsername(dbProperties.getValue(DbProperties.Key.USER));
        config.setPassword(dbProperties.getValue(DbProperties.Key.PASSWORD));
        config.setDriverClassName(dbProperties.getValue(DbProperties.Key.DRIVER_NAME));

        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);
        config.setConnectionTimeout(30000);
        config.setLeakDetectionThreshold(5000);

        dataSource = new HikariDataSource(config);

        log.info("<= HiKariDataSource init complete");

    }

    public static void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
