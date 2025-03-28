package com.bookie.scrap.watcha.repository;


import com.bookie.scrap.common.domain.Repository;
import com.bookie.scrap.watcha.entity.WatchaBookcaseMetaEntity;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class WatchaBookToBookcaseMetasRepository implements Repository<WatchaBookcaseMetaEntity> {

    private static final WatchaBookToBookcaseMetasRepository INSTANCE = new WatchaBookToBookcaseMetasRepository();

    public static WatchaBookToBookcaseMetasRepository getInstance() {
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

    public void insertOrUpdate(String bookCode, List<WatchaBookcaseMetaEntity> targetEntity, EntityManager em) {

        String jpql = "SELECT e FROM WatchaBookcaseMetaEntity e WHERE e.bookCode = :bookCode";

        List<WatchaBookcaseMetaEntity> dbEntities = em.createQuery(jpql, WatchaBookcaseMetaEntity.class)
                .setParameter("bookCode", bookCode)
                .getResultList();

        Set<String> dbBookcaseCode = dbEntities.stream().map(WatchaBookcaseMetaEntity::getBookcaseCode).collect(Collectors.toSet());

        dbEntities.sort(Comparator.comparing(WatchaBookcaseMetaEntity::getBookcaseCode));
        targetEntity.sort(Comparator.comparing(WatchaBookcaseMetaEntity::getBookcaseCode));

        Iterator<WatchaBookcaseMetaEntity> dbIter = dbEntities.iterator();
        Iterator<WatchaBookcaseMetaEntity> newIter = targetEntity.iterator();

        WatchaBookcaseMetaEntity dbItem = null;
        WatchaBookcaseMetaEntity newItem = null;

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

            // 있던 item인데 update 되었으면 -> update
            if (dbBookcaseCode.contains(newItem.getBookcaseCode())) {

                while (dbItem != null && !dbItem.getBookcaseCode().equals(newItem.getBookcaseCode())) {
                    if (!dbIter.hasNext()) {
                        dbItem = null;
                        break;
                    }
                    dbItem = dbIter.next();
                }

                if (dbItem != null && !dbItem.isSame(newItem)) {
                    dbItem.updateEntity(newItem);
                    log.info("update: {}", dbItem.getBookcaseCode());
                }

                if (dbIter.hasNext()) {
                    dbItem = dbIter.next();
                }
                if (newIter.hasNext()) {
                    newItem = newIter.next();
                } else {
                    break;
                }

            } else {
                // db에 없던 item이면 -> insert
                em.persist(newItem);
                WatchaUserRepository.getInstance().insertOrUpdate(newItem.getUser(), em);

                log.info("insert: {}", newItem.getBookcaseCode());

                if (!newIter.hasNext()) {
                    break;
                }
                newItem = newIter.next();
            }

        }

        // 삭제된 item이면 -> status inactivate
        if (dbItem != null) {
            dbItem.inActivate();
            log.info("inactivate: {}", dbItem.getBookcaseCode());
        }

        while (dbIter.hasNext()) {
            dbItem = dbIter.next();
            dbItem.inActivate();
            log.info("inactivate: {}", dbItem.getBookcaseCode());
        }

    }

}
