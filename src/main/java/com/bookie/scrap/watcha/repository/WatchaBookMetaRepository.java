package com.bookie.scrap.watcha.repository;


import com.bookie.scrap.common.db.EntityManagerFactoryProvider;
import com.bookie.scrap.common.domain.Repository;
import com.bookie.scrap.watcha.entity.WatchaBookEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NonUniqueResultException;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLDataException;
import java.util.List;

@Slf4j
public class WatchaBookMetaRepository implements Repository<WatchaBookEntity> {

    private static final WatchaBookMetaRepository INSTANCE = new WatchaBookMetaRepository();
    private final EntityManagerFactory emf;

    private WatchaBookMetaRepository() {
        this.emf = EntityManagerFactoryProvider.getInstance().getEntityManagerFactory();
    }

    public static WatchaBookMetaRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public List<WatchaBookEntity> select(String bookCode) {
        try (EntityManager em = emf.createEntityManager()) {

            String jpql = "SELECT w FROM WatchaBookEntity w WHERE w.bookCode = :bookCode";

            List<WatchaBookEntity> results = em.createQuery(jpql, WatchaBookEntity.class)
                    .setParameter("bookCode", bookCode)
                    .getResultList();

            return results;
        } catch (Exception e) {
            log.error("Error selecting entity with bookCode: {}", bookCode, e);
            throw e;
        }
    }

    @Override
    public boolean insertOrUpdate(WatchaBookEntity targetEntity) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            String jpql = "SELECT e FROM WatchaBookEntity e WHERE e.bookCode = :bookCode";

            List<WatchaBookEntity> existingEntities = em.createQuery(jpql, WatchaBookEntity.class)
                    .setParameter("bookCode", targetEntity.getBookCode())
                    .getResultList();

            if (existingEntities.isEmpty()) {
                em.persist(targetEntity);
                log.info("insert: {}", targetEntity);
            } else if (existingEntities.size() == 1) {
                WatchaBookEntity existingEntity = existingEntities.get(0);
                existingEntity.updateEntity(targetEntity);
                log.info("update: {}", existingEntity);
            } else {
                throw new NonUniqueResultException("select result is multiple: " + existingEntities.size());
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
