package com.bookie.scrap.watcha.repository;


import com.bookie.scrap.common.db.EntityManagerFactoryProvider;
import com.bookie.scrap.common.domain.Repository;
import com.bookie.scrap.watcha.entity.WatchaBookMetaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NonUniqueResultException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class WatchaBookMetaRepository implements Repository<WatchaBookMetaEntity> {

    private static final WatchaBookMetaRepository INSTANCE = new WatchaBookMetaRepository();

    public static WatchaBookMetaRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public List<WatchaBookMetaEntity> selectWithCode(String bookCode, EntityManager em) {

        String jpql = "SELECT w FROM WatchaBookMetaEntity w WHERE w.bookCode = :bookCode";

        List<WatchaBookMetaEntity> results = em.createQuery(jpql, WatchaBookMetaEntity.class)
                .setParameter("bookCode", bookCode)
                .getResultList();

        return results;

    }

    public void insertOrUpdate(WatchaBookMetaEntity targetEntity, EntityManager em) {

        String jpql = "SELECT e FROM WatchaBookMetaEntity e WHERE e.bookCode = :bookCode";

        List<WatchaBookMetaEntity> existingEntities = em.createQuery(jpql, WatchaBookMetaEntity.class)
                .setParameter("bookCode", targetEntity.getBookCode())
                .getResultList();

        if (existingEntities.isEmpty()) {
            em.persist(targetEntity);
            log.info("insert: {}", targetEntity);
        } else if (existingEntities.size() == 1) {
            WatchaBookMetaEntity existingEntity = existingEntities.get(0);
            existingEntity.updateEntity(targetEntity);
            log.info("update: {}", existingEntity);
        } else {
            throw new NonUniqueResultException("select result is multiple: " + existingEntities.size());
        }

    }

}
