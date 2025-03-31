package com.bookie.scrap.watcha.repository;


import com.bookie.scrap.watcha.entity.WatchaBookToBookcaseMetaEntity;
import com.bookie.scrap.watcha.entity.WatchaBookcaseToBookEntity;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class WatchaBookToBookcaseMetasRepository{

    private static final WatchaBookToBookcaseMetasRepository INSTANCE = new WatchaBookToBookcaseMetasRepository();

    public static WatchaBookToBookcaseMetasRepository getInstance() {
        return INSTANCE;
    }

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

        log.info("WatchaBookToBookcaseMetasRepository Bookcase:{}", bookCode);

        targetEntity = new ArrayList<>(
                targetEntity.stream()
                .collect(Collectors.toMap(
                        WatchaBookToBookcaseMetaEntity::getBookcaseCode,
                        Function.identity(),
                        (existing, replacement) -> existing
                ))
                .values()
        );

        List<String> bookcaseCodes = targetEntity.stream()
                .map(WatchaBookToBookcaseMetaEntity::getBookcaseCode)
                .distinct()
                .collect(Collectors.toList());

        List<WatchaBookToBookcaseMetaEntity> dbEntities = em.createQuery(
                        "SELECT e FROM WatchaBookToBookcaseMetaEntity e WHERE e.bookcaseCode IN :codes",
                        WatchaBookToBookcaseMetaEntity.class)
                .setParameter("codes", bookcaseCodes)
                .getResultList();


        Map<String, WatchaBookToBookcaseMetaEntity> dbEntityMap = dbEntities.stream()
                .collect(Collectors.toMap(WatchaBookToBookcaseMetaEntity::getBookcaseCode, Function.identity()));


        Map<String, Boolean> dbEntityVisited = new HashMap<>();
        dbEntities.forEach(dbEntity -> dbEntityVisited.put(dbEntity.getBookcaseCode(), false));

        List<String> insertedBookcaseCodes = new ArrayList<>();
        List<String> updatedBookcaseCodes = new ArrayList<>();
        for (WatchaBookToBookcaseMetaEntity newEntity : targetEntity) {

            if (!dbEntityMap.containsKey(newEntity.getBookcaseCode())) {
                insertedBookcaseCodes.add(newEntity.getBookcaseCode());
                WatchaUserRepository.getInstance().insertOrUpdate(newEntity.getUser(), em);
                em.persist(newEntity);
            } else {

                WatchaBookToBookcaseMetaEntity dbEntity = dbEntityMap.get(newEntity.getBookcaseCode());
                if (!dbEntity.isSame(newEntity)) {
                    updatedBookcaseCodes.add(newEntity.getBookcaseCode());
                    dbEntity.updateEntity(newEntity);
                }

                dbEntityVisited.put(newEntity.getBookcaseCode(), true);
            }
        }

        log.info("Insert BookcaseMeta:{}", String.join(", ", insertedBookcaseCodes));
        log.info("Update BookcaseMeta:{}", String.join(", ", updatedBookcaseCodes));

        //TODO: inactivate 처리
//        for (Map.Entry<String, Boolean> visited : dbEntityVisited.entrySet()) {
//            if (visited.getValue().equals(false)) {
//                WatchaBookToBookcaseMetaEntity dbEntity = dbEntityMap.get(visited.getKey());
//                dbEntity.inActivate();
//                log.info("Inactivate BookcaseMeta:{}", dbEntity.getBookcaseCode());
//            }
//        }

    }

}
