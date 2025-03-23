package com.bookie.scrap.watcha.repository;


import com.bookie.scrap.common.db.EntityManagerFactoryProvider;
import com.bookie.scrap.common.domain.Repository;
import com.bookie.scrap.watcha.entity.WatchaBookEntity;
import com.bookie.scrap.watcha.entity.WatchaBookcaseMetaEntity;
import com.bookie.scrap.watcha.entity.WatchaUserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NonUniqueResultException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class WatchaBookcaseMetaRepository implements Repository<WatchaBookcaseMetaEntity> {

    private static final WatchaBookcaseMetaRepository INSTANCE = new WatchaBookcaseMetaRepository();
    private final EntityManagerFactory emf;

    private WatchaBookcaseMetaRepository() {
        this.emf = EntityManagerFactoryProvider.getInstance().getEntityManagerFactory();
    }

    public static WatchaBookcaseMetaRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public List<WatchaBookcaseMetaEntity> selectWithCode(String bookcaseCode) {
        try (EntityManager em = emf.createEntityManager()) {

            String jpql = "SELECT w FROM WatchaBookcaseMetaEntity w WHERE w.bookcaseCode = :bookcaseCode";

            List<WatchaBookcaseMetaEntity> results = em.createQuery(jpql, WatchaBookcaseMetaEntity.class)
                    .setParameter("bookcaseCode", bookcaseCode)
                    .getResultList();

            return results;
        } catch (Exception e) {
            log.error("Error selecting entity with bookcaseCode: {}", bookcaseCode, e);
            throw e;
        }
    }

    @Override
    public boolean insertOrUpdate(WatchaBookcaseMetaEntity targetEntity) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            this.insertOrUpdateUserEntity(targetEntity.getUser(), em);

            String jpql = "SELECT e FROM WatchaBookcaseMetaEntity e WHERE e.bookcaseCode = :bookcaseCode";

            List<WatchaBookcaseMetaEntity> existingEntities = em.createQuery(jpql, WatchaBookcaseMetaEntity.class)
                    .setParameter("bookcaseCode", targetEntity.getBookcaseCode())
                    .getResultList();

            if (existingEntities.isEmpty()) {
                em.persist(targetEntity);
                log.info("insert: {}", targetEntity);
            } else if (existingEntities.size() == 1) {
                WatchaBookcaseMetaEntity existingEntity = existingEntities.get(0);
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

    private void insertOrUpdateUserEntity(WatchaUserEntity targetEntity, EntityManager em) {

        String jpql = "SELECT e FROM WatchaUserEntity e WHERE e.userCode = :userCode";

        List<WatchaUserEntity> existingEntities = em.createQuery(jpql, WatchaUserEntity.class)
                .setParameter("userCode", targetEntity.getUserCode())
                .getResultList();

        if (existingEntities.isEmpty()) {
            em.persist(targetEntity);
            log.info("insert: {}", targetEntity);
        } else if (existingEntities.size() == 1) {
            WatchaUserEntity existingEntity = existingEntities.get(0);
            existingEntity.updateEntity(targetEntity);
            log.info("update: {}", existingEntity);
        } else {
            throw new NonUniqueResultException("select result is multiple: " + existingEntities.size());
        }

    }
}
