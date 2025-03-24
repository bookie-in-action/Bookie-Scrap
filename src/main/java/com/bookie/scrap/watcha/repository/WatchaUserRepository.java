package com.bookie.scrap.watcha.repository;

import com.bookie.scrap.common.db.EntityManagerFactoryProvider;
import com.bookie.scrap.common.domain.Repository;
import com.bookie.scrap.watcha.entity.WatchaBookcaseMetaEntity;
import com.bookie.scrap.watcha.entity.WatchaUserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NonUniqueResultException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class WatchaUserRepository implements Repository<WatchaUserEntity> {

    private static final WatchaUserRepository INSTANCE = new WatchaUserRepository();
    public static WatchaUserRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public List<WatchaUserEntity> selectWithCode(String userCode, EntityManager em) {

            String jpql = "SELECT e FROM WatchaUserEntity e WHERE e.userCode = :userCode";

            List<WatchaUserEntity> results = em.createQuery(jpql, WatchaUserEntity.class)
                    .setParameter("userCode", userCode)
                    .getResultList();

            return results;
    }

    @Override
    public void insertOrUpdate(WatchaUserEntity targetEntity, EntityManager em) {

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
