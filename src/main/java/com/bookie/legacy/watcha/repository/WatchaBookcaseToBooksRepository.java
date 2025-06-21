package com.bookie.legacy.watcha.repository;


import com.bookie.legacy.common.domain.Status;
import com.bookie.legacy.watcha.entity.WatchaBookcaseToBookEntity;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

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

        String jpql = "SELECT e FROM WatchaBookcaseToBookEntity e WHERE e.bookcaseCode = :bookcaseCode and e.status = :status";

        List<WatchaBookcaseToBookEntity> dbEntities = em.createQuery(jpql, WatchaBookcaseToBookEntity.class)
                .setParameter("bookcaseCode", bookcaseCode)
                .setParameter("status", Status.ACTIVE)
                .getResultList();

        Map<String, WatchaBookcaseToBookEntity> dbEntityMap = new HashMap<>();
        Map<String, Boolean> hasEntitiesInDb = new HashMap<>();

        dbEntities.forEach(dbEntity -> {
            dbEntityMap.put(dbEntity.getBookCode(), dbEntity);
            hasEntitiesInDb.put(dbEntity.getBookCode(), false);
        });

        List<String> insertedBookCodes = new ArrayList<>();

        for (WatchaBookcaseToBookEntity newEntity : targetEntity) {
            if (!dbEntityMap.containsKey(newEntity.getBookCode())) {
                em.persist(newEntity); // db에 bookCode가 없는 경우
                insertedBookCodes.add(newEntity.getBookCode());
            } else {
                hasEntitiesInDb.put(newEntity.getBookCode(), true); // 이미 db에 있는 경우
            }
        }

        log.info("Insert Book:{}", String.join(", ", insertedBookCodes));


        //TODO: inactivate 사용하려면 페이지네이션 한 books를 전부 들어와서 이 메서드에서 한 번에 작업해야함
//        List<String> deletedBookCodes = new ArrayList<>();
//        for (Map.Entry<String, Boolean> visited : hasEntitiesInDb.entrySet()) {
//            if (visited.getValue().equals(false)) { // bookcase에서 지워진 책인 경우
//                WatchaBookcaseToBookEntity dbEntity = dbEntityMap.get(visited.getKey());
//                dbEntity.inActivate();
//                deletedBookCodes.add(dbEntity.getBookCode());
//            }
//        }
//        log.info("Inactivate Book:{}", String.join(", ", deletedBookCodes));

    }
}
