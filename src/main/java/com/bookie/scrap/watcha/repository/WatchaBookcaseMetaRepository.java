package com.bookie.scrap.watcha.repository;


import com.bookie.scrap.common.db.EntityManagerFactoryProvider;
import com.bookie.scrap.common.domain.Repository;
import com.bookie.scrap.watcha.entity.WatchaBookMetaEntity;
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

    public static WatchaBookcaseMetaRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public List<WatchaBookcaseMetaEntity> selectWithCode(String bookcaseCode, EntityManager em) {

        String jpql = "SELECT w FROM WatchaBookcaseMetaEntity w WHERE w.bookcaseCode = :bookcaseCode";

        List<WatchaBookcaseMetaEntity> results = em.createQuery(jpql, WatchaBookcaseMetaEntity.class)
                .setParameter("bookcaseCode", bookcaseCode)
                .getResultList();

        return results;
    }

    @Override
    public void insertOrUpdate(WatchaBookcaseMetaEntity targetEntity, EntityManager em) {

        WatchaUserRepository.getInstance().insertOrUpdate(targetEntity.getUser(), em);

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

    }

}
