package com.bookie.scrap.watcha.repository;


import com.bookie.scrap.common.domain.Repository;
import com.bookie.scrap.watcha.entity.WatchaBookToBookcaseMetaEntity;
import com.bookie.scrap.watcha.entity.WatchaBookcaseToBookEntity;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class WatchaBookToBookcaseMetasRepository implements Repository<WatchaBookToBookcaseMetaEntity> {

    private static final WatchaBookToBookcaseMetasRepository INSTANCE = new WatchaBookToBookcaseMetasRepository();

    public static WatchaBookToBookcaseMetasRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public List<WatchaBookToBookcaseMetaEntity> selectWithCode(String bookcaseCode, EntityManager em) {

        String jpql = "SELECT w FROM WatchaBookToBookcaseMetaEntity w WHERE w.bookcaseCode = :bookcaseCode";

        List<WatchaBookToBookcaseMetaEntity> results = em.createQuery(jpql, WatchaBookToBookcaseMetaEntity.class)
                .setParameter("bookcaseCode", bookcaseCode)
                .getResultList();

        return results;
    }

    /**
     * 메서드에 WatchaUserRepository 사용하여 user insertOrUpdate 하는 프로세스 존재
     * @param bookCode
     * @param targetEntity
     * @param em
     */
    public void insertOrUpdate(String bookCode, List<WatchaBookToBookcaseMetaEntity> targetEntity, EntityManager em) {

        String jpql = "SELECT e FROM WatchaBookToBookcaseMetaEntity e WHERE e.bookCode = :bookCode";

        List<WatchaBookToBookcaseMetaEntity> dbEntities = em.createQuery(jpql, WatchaBookToBookcaseMetaEntity.class)
                .setParameter("bookCode", bookCode)
                .getResultList();


        Map<String, WatchaBookToBookcaseMetaEntity> dbEntityMap = new HashMap<>();
        Map<String, Boolean> dbEntityVisited = new HashMap<>();
        dbEntities.forEach(dbEntity -> {
            dbEntityMap.put(dbEntity.getBookcaseCode(), dbEntity);
            dbEntityVisited.put(dbEntity.getBookcaseCode(), false);
        });

        for (WatchaBookToBookcaseMetaEntity newEntity : targetEntity) {

            if (!dbEntityMap.containsKey(newEntity.getBookcaseCode())) {
                log.info("Insert BookcaseMeta:{}", newEntity.getBookcaseCode());
                WatchaUserRepository.getInstance().insertOrUpdate(newEntity.getUser(), em);
                em.merge(newEntity);
            } else {

                WatchaBookToBookcaseMetaEntity dbEntity = dbEntityMap.get(newEntity.getBookcaseCode());
                if (!dbEntity.isSame(newEntity)) {
                    log.info("Update BookcaseMeta:{}", newEntity.getBookcaseCode());
                    dbEntity.updateEntity(newEntity);
                }

                dbEntityVisited.put(newEntity.getBookCode(), true);
            }
        }

        for (Map.Entry<String, Boolean> visited : dbEntityVisited.entrySet()) {
            if (visited.getValue().equals(false)) {
                WatchaBookToBookcaseMetaEntity dbEntity = dbEntityMap.get(visited.getKey());
                log.info("Inactivate BookcaseMeta:{}", dbEntity.getBookcaseCode());
            }
        }

    }

}
