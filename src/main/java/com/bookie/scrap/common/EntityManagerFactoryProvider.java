package com.bookie.scrap.common;

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

@Slf4j
public class EntityManagerFactoryProvider {

    private static final EntityManagerFactoryProvider INSTANCE = new EntityManagerFactoryProvider();
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
            hikariConfig.setJdbcUrl("jdbc:mariadb://112.144.253.56:3306/bookie_dev");
            hikariConfig.setUsername("bookie_dev");
            hikariConfig.setPassword("bookie_dev");
            hikariConfig.setMaximumPoolSize(10);




            configuration.getProperties().put(AvailableSettings.DATASOURCE, new HikariDataSource(hikariConfig));
//            configuration.setProperty(AvailableSettings.DIALECT, "org.hibernate.dialect.MariaDBDialect");

            configuration.setProperty(AvailableSettings.SHOW_SQL, "true");
            configuration.setProperty(AvailableSettings.FORMAT_SQL, "true");
            configuration.setProperty(AvailableSettings.HBM2DDL_AUTO, "none");


            // 엔티티 클래스 목록을 직접 지정
//            configuration.addAnnotatedClass(com.bookie.scrap.watcha.dto.WatchaEntity.class);
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
