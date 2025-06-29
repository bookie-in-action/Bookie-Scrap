package com.bookie.legacy.watcha.repository;


import com.bookie.legacy.watcha.entity.WatchaBookMetaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NonUniqueResultException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class WatchaBookMetaRepository{

    private static final WatchaBookMetaRepository INSTANCE = new WatchaBookMetaRepository();

    public static WatchaBookMetaRepository getInstance() {
        return INSTANCE;
    }

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
            log.info("Insert BookMeta BookCode: {}", targetEntity.getBookCode());
        } else if (existingEntities.size() == 1) {
            WatchaBookMetaEntity existingEntity = existingEntities.get(0);
            existingEntity.updateEntity(targetEntity);
            log.info("Update BookMeta BookCode: {}", existingEntity.getBookCode());
        } else {
            throw new NonUniqueResultException("select result is multiple: " + existingEntities.size());
        }

    }

}
