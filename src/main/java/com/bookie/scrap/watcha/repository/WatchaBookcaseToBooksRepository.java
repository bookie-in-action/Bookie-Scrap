package com.bookie.scrap.watcha.repository;


import com.bookie.scrap.watcha.entity.WatchaBookToBookcaseMetaEntity;
import com.bookie.scrap.watcha.entity.WatchaBookcaseToBookEntity;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class WatchaBookcaseToBooksRepository {

    private static final WatchaBookcaseToBooksRepository INSTANCE = new WatchaBookcaseToBooksRepository();

    public static WatchaBookcaseToBooksRepository getInstance() {
        return INSTANCE;
    }

    public List<WatchaBookcaseToBookEntity> selectWithCode(String bookcaseCode, EntityManager em) {

        String jpql = "SELECT w FROM WatchaBookcaseToBookEntity w WHERE w.bookcaseCode = :bookcaseCode";

        List<WatchaBookcaseToBookEntity> results = em.createQuery(jpql, WatchaBookcaseToBookEntity.class)
                .setParameter("bookcaseCode", bookcaseCode)
                .getResultList();

        return results;
    }

    public void insertOrUpdate(String bookcaseCode, List<WatchaBookcaseToBookEntity> targetEntity, EntityManager em) {

        log.info("WatchaBookcaseToBooksRepository Bookcase:{}", bookcaseCode);

        String jpql = "SELECT e FROM WatchaBookcaseToBookEntity e WHERE e.bookcaseCode = :bookcaseCode";

        List<WatchaBookcaseToBookEntity> dbEntities = em.createQuery(jpql, WatchaBookcaseToBookEntity.class)
                .setParameter("bookcaseCode", bookcaseCode)
                .getResultList();

        Map<String, WatchaBookcaseToBookEntity> dbEntityMap = new HashMap<>();
        Map<String, Boolean> dbEntityVisited = new HashMap<>();
        dbEntities.forEach(dbEntity -> {
            dbEntityMap.put(dbEntity.getBookCode(), dbEntity);
            dbEntityVisited.put(dbEntity.getBookCode(), false);
        });

        for (WatchaBookcaseToBookEntity newEntity : targetEntity) {
            if (!dbEntityMap.containsKey(newEntity.getBookCode())) {
                log.info("Insert Book:{}", newEntity.getBookCode());
                em.persist(newEntity);
            } else {
                dbEntityVisited.put(newEntity.getBookCode(), true);
            }
        }

        for (Map.Entry<String, Boolean> visited : dbEntityVisited.entrySet()) {
            if (visited.getValue().equals(false)) {
                WatchaBookcaseToBookEntity dbEntity = dbEntityMap.get(visited.getKey());
                log.info("Inactivate Book:{}", dbEntity.getBookCode());
            }
        }
    }
}
