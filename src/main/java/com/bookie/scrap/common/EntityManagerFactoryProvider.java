package com.bookie.scrap.common;

import com.bookie.scrap.properties.DbProperties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.MappingSettings;
import org.hibernate.jpa.HibernatePersistenceProvider;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bookie.scrap.properties.DbProperties.*;

@Slf4j
public class EntityManagerFactoryProvider {

    private static final EntityManagerFactoryProvider INSTANCE = new EntityManagerFactoryProvider();
    private static final DbProperties dbProperties = DbProperties.getInstance();

    private static EntityManagerFactory emf;

    private EntityManagerFactoryProvider() {}

    public static EntityManagerFactoryProvider getInstance() {return INSTANCE;}

    public synchronized void init() {

        if (emf != null) {
            return;
        }

        log.info("=> EntityManagerFactory initialization start");

        try {
            Configuration configuration = new Configuration();

            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl(dbProperties.getValue(Key.JDBC_URL));
            hikariConfig.setUsername(dbProperties.getValue(Key.USER));
            hikariConfig.setPassword(dbProperties.getValue(Key.PASSWORD));
            hikariConfig.setMaximumPoolSize(Integer.parseInt(dbProperties.getValue(Key.MAX_POOL)));

            configuration.getProperties().put(AvailableSettings.DATASOURCE, new HikariDataSource(hikariConfig));

            configuration.setProperty(AvailableSettings.SHOW_SQL, dbProperties.getValue(Key.SHOW_SQL));
            configuration.setProperty(AvailableSettings.FORMAT_SQL, dbProperties.getValue(Key.FORMAT_SQL));
            configuration.setProperty(AvailableSettings.HBM2DDL_AUTO, dbProperties.getValue(Key.HBM2DDL_AUTO));

            // 엔티티 클래스 목록을 직접 지정
            configuration.addAnnotatedClass(com.bookie.scrap.watcha.dto.WatchaBookEntity.class);


            StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();

            // EntityManagerFactory 생성
            emf = configuration.buildSessionFactory(registry);

            EntityManagerFactoryProvider.emf.createEntityManager().close();
            log.info("EntityManagerFactory created successfully and EntityManager is functional!");
            log.info("<= EntityManagerFactory initialization complete");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public EntityManagerFactory getEntityManagerFactory() {
        if( EntityManagerFactoryProvider.emf == null) {
            throw new IllegalStateException("EntityManagerFactory not initialized");
        }
        return EntityManagerFactoryProvider.emf;
    }

    public void close() {
        if (EntityManagerFactoryProvider.emf != null) {
            EntityManagerFactoryProvider.emf.close();
            log.info("EntityManagerFactory closed successfully");
        } else {
            log.warn("EntityManagerFactory is not initialized. Nothing to close");
        }
    }
}
