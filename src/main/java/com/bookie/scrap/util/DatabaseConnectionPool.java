package com.bookie.scrap.util;

import com.bookie.scrap.properties.DbProperties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class DatabaseConnectionPool {

    private static final DatabaseConnectionPool INSTANCE = new DatabaseConnectionPool();
    @Getter private HikariDataSource dataSource;

    private DatabaseConnectionPool() {}

    public static DatabaseConnectionPool getInstance() {return INSTANCE;}

    public void init() {

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

        logPoolState();

        log.info("<= HiKariDataSource init complete");

    }

    public void logPoolState() {
        if (dataSource != null) {
            HikariPoolMXBean hikariPoolMXBean = dataSource.getHikariPoolMXBean();
            log.info("HikariCP Pool State - Active Connections: {}, Idle Connections: {}, Total Connections: {}, Threads Awaiting: {}",
                    hikariPoolMXBean.getActiveConnections(),
                    hikariPoolMXBean.getIdleConnections(),
                    hikariPoolMXBean.getTotalConnections(),
                    hikariPoolMXBean.getThreadsAwaitingConnection()
            );
        }
    }

    public void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
