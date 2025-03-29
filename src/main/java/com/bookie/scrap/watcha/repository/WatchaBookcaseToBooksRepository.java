package com.bookie.scrap.watcha.repository;


import com.bookie.scrap.watcha.entity.WatchaBookToBookcaseMetaEntity;
import com.bookie.scrap.watcha.entity.WatchaBookcaseToBookEntity;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
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

        int updated = 0;
        int inActivated = 0;
        int inserted = 0;

        String jpql = "SELECT e FROM WatchaBookcaseToBookEntity e WHERE e.bookcaseCode = :bookcaseCode";

        List<WatchaBookcaseToBookEntity> dbEntities = em.createQuery(jpql, WatchaBookcaseToBookEntity.class)
                .setParameter("bookcaseCode", bookcaseCode)
                .getResultList();

        Set<String> dbBookCodes = dbEntities.stream().map(WatchaBookcaseToBookEntity::getBookCode).collect(Collectors.toSet());

        dbEntities.sort(Comparator.comparing(WatchaBookcaseToBookEntity::getBookCode));
        targetEntity.sort(Comparator.comparing(WatchaBookcaseToBookEntity::getBookCode));

        Iterator<WatchaBookcaseToBookEntity> dbIter = dbEntities.iterator();
        Iterator<WatchaBookcaseToBookEntity> newIter = targetEntity.iterator();

        WatchaBookcaseToBookEntity dbItem = null;
        WatchaBookcaseToBookEntity newItem = null;

        if (dbIter.hasNext()) {
            dbItem = dbIter.next();
        }
        if (newIter.hasNext()) {
            newItem = newIter.next();
        }

        while (true) {
            if (newItem == null) {
                break;
            }

            // db에 없던 item이면 -> insert
            if (!dbBookCodes.contains(newItem.getBookCode())) {
                em.persist(newItem);

                inserted++;
                log.info("insert: {}", newItem.getBookCode());

                if (newIter.hasNext()) {
                    newItem = newIter.next();
                } else {
                    break;
                }
            }

        }

        // 삭제된 item이면 -> status inactivate
        while (dbIter.hasNext()) {
            dbItem = dbIter.next();
            dbItem.inActivate();
            inActivated++;
            log.info("inactivate: {}", dbItem.getBookCode());
        }

        log.info("inserted:{}, inactivated:{}, updated:{}", inserted, inActivated, updated);
    }

}
