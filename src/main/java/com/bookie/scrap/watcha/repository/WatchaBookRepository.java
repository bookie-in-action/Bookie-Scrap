package com.bookie.scrap.watcha.repository;


import com.bookie.scrap.common.db.EntityManagerFactoryProvider;
import com.bookie.scrap.common.Repository;
import com.bookie.scrap.watcha.entity.WatchaBookEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class WatchaBookRepository implements Repository<WatchaBookEntity> {

    private static final WatchaBookRepository INSTANCE = new WatchaBookRepository();
    private final EntityManagerFactory emf;

    private WatchaBookRepository() {
        this.emf = EntityManagerFactoryProvider.getInstance().getEntityManagerFactory();
    }

    public static WatchaBookRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public Optional<WatchaBookEntity> select(String bookCode) {
        try (EntityManager em = emf.createEntityManager()) {

            String jpql = "SELECT w FROM WatchaBookEntity w WHERE w.bookCode = :bookCode";

            WatchaBookEntity entity = em.createQuery(jpql, WatchaBookEntity.class)
                    .setParameter("bookCode", bookCode)
                    .getSingleResult();

            return Optional.ofNullable(entity);
        } catch (Exception e) {
            log.error("An error occurred while selecting an entity with code: {}", bookCode, e);
            return Optional.empty();
        }
    }

    @Override
    public boolean insertOrUpdate(WatchaBookEntity targetEntity) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            String jpql = "SELECT e FROM WatchaBookEntity e WHERE e.bookCode = :bookCode";

            WatchaBookEntity existingEntity =
                    em.createQuery(jpql, WatchaBookEntity.class)
                            .setParameter("bookCode", targetEntity.getBookCode())
                            .getSingleResult();

            if (existingEntity != null) {
                existingEntity.updateEntity(targetEntity);
                log.info("update: {}", existingEntity);
            } else {
                em.persist(targetEntity);
                log.info("insert: {}", targetEntity);
            }
            em.getTransaction().commit();

            return true;
        } catch (Exception e) {
            log.error("An error occurred while inserting or updating an entity: {}", targetEntity, e);
            if (em.getTransaction().isActive()) {
                log.warn("Transaction is active, rolling back...", e);
                em.getTransaction().rollback();
            } else {
                log.warn("Transaction is not active, skipping rollback", e);
            }

            return false;
        } finally {
            em.close();
        }
    }
}
