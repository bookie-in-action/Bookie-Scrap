package com.bookie.scrap.watcha.repository;


import com.bookie.scrap.common.EntityManagerFactoryProvider;
import com.bookie.scrap.watcha.dto.WatchaBookEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class WatchaBookRepository implements WatchaRepository<WatchaBookEntity> {

    private static final WatchaBookRepository INSTANCE = new WatchaBookRepository();
    private final EntityManagerFactory emf;

    private WatchaBookRepository() {
        this.emf = EntityManagerFactoryProvider.getInstance().getEntityManagerFactory();
    }

    public static WatchaBookRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public Optional<WatchaBookEntity> select(String code) {
        try (EntityManager em = emf.createEntityManager()) {
            WatchaBookEntity entity = em.find(WatchaBookEntity.class, code);
            return Optional.ofNullable(entity);
        } catch (Exception e) {
            log.error("An error occurred while selecting an entity with code: {}", code, e);
            return Optional.empty();
        }
    }

    @Override
    public boolean insertOrUpdate(WatchaBookEntity targetEntity) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            WatchaBookEntity existingEntity = em.find(WatchaBookEntity.class, targetEntity.getSnowflakeId());

            if (existingEntity != null) {
                em.merge(targetEntity);
                log.info("update: {}", targetEntity);
            } else {
                em.persist(targetEntity);
                log.info("insert: {}", targetEntity);
            }

            em.getTransaction().commit();

            return true;
        } catch (Exception e) {
            log.error("An error occurred while inserting or updating an entity: {}", targetEntity, e);
            if (em.getTransaction().isActive()) {
                log.warn("Transaction is active, rolling back...");
                em.getTransaction().rollback();
            } else {
                log.warn("Transaction is not active, skipping rollback");
            }
            return false;
        } finally {
            em.close();
        }
    }
}
