package com.bookie.scrap.common.db;

import com.bookie.scrap.common.startup.Initializable;
import com.bookie.scrap.common.properties.DbProperties;
import com.bookie.scrap.common.startup.Shutdownable;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;

import static com.bookie.scrap.common.properties.DbProperties.*;

@Slf4j
public class EntityManagerFactoryProvider implements Initializable, Shutdownable {

    private static final EntityManagerFactoryProvider INSTANCE = new EntityManagerFactoryProvider();
    private static final DbProperties dbProperties = DbProperties.getInstance();

    private static EntityManagerFactory emf;

    private EntityManagerFactoryProvider() {}

    public static EntityManagerFactoryProvider getInstance() {return INSTANCE;}

    @Override
    public synchronized void init(String runningOption) {

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
            configuration.addAnnotatedClass(com.bookie.scrap.watcha.entity.WatchaBookEntity.class);


            StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();

            // EntityManagerFactory 생성
            emf = configuration.buildSessionFactory(registry);

            EntityManagerFactoryProvider.emf.createEntityManager().close();
            log.info("EntityManagerFactory created successfully and EntityManager is functional!");
            log.info("<= EntityManagerFactory initialization complete");

        } catch (Exception e) {
            log.error("An error occurred during EntityManagerFactory initialization", e);
        }

    }

    public EntityManagerFactory getEntityManagerFactory() {
        if( EntityManagerFactoryProvider.emf == null) {
            throw new IllegalStateException("EntityManagerFactory not initialized");
        }
        return EntityManagerFactoryProvider.emf;
    }

    @Override
    public void shutdown() {
        if (EntityManagerFactoryProvider.emf != null) {
            EntityManagerFactoryProvider.emf.close();
            log.info("EntityManagerFactory closed successfully");
        } else {
            log.debug("EntityManagerFactory is not initialized. Nothing to close");
        }
    }
}
