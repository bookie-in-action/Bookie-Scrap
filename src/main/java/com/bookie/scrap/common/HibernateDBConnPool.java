package com.bookie.scrap.common;

import com.bookie.scrap.properties.DbProperties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class HibernateDBConnPool {

    private static final HibernateDBConnPool INSTANCE = new HibernateDBConnPool();
    private static EntityManagerFactory emf;

    private HibernateDBConnPool() {}

    public static HibernateDBConnPool getInstance() {return INSTANCE;}

    public void init() {

        if (emf != null) {
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

        HikariDataSource dataSource = new HikariDataSource(config);
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hikari.dataSource", dataSource);
        properties.put("hibernate.dialect", "org.hibernate.dialect.MariaDBDialect");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");
        properties.put("hibernate.hbm2ddl.auto", "update");

        emf = Persistence.createEntityManagerFactory("watchaPU", properties);

        logPoolState();
        log.info("<= HiKariDataSource init complete");

    }

    public EntityManagerFactory getEntityManagerFactory() {
        if( HibernateDBConnPool.emf == null) {
            throw new IllegalStateException("HiKariDataSource not initialized");
        }
        return HibernateDBConnPool.emf;
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
