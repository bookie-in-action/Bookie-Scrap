package com.bookie.scrap.watcha.repository;


import com.bookie.scrap.common.domain.Repository;
import com.bookie.scrap.watcha.entity.WatchaBookToBookcaseMetaEntity;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
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
        int updated = 0;
        int inActivated = 0;
        int inserted = 0;

        String jpql = "SELECT e FROM WatchaBookToBookcaseMetaEntity e WHERE e.bookCode = :bookCode";

        List<WatchaBookToBookcaseMetaEntity> dbEntities = em.createQuery(jpql, WatchaBookToBookcaseMetaEntity.class)
                .setParameter("bookCode", bookCode)
                .getResultList();

        Set<String> dbBookcaseCode = dbEntities.stream().map(WatchaBookToBookcaseMetaEntity::getBookcaseCode).collect(Collectors.toSet());

        dbEntities.sort(Comparator.comparing(WatchaBookToBookcaseMetaEntity::getBookcaseCode));
        targetEntity.sort(Comparator.comparing(WatchaBookToBookcaseMetaEntity::getBookcaseCode));

        Iterator<WatchaBookToBookcaseMetaEntity> dbIter = dbEntities.iterator();
        Iterator<WatchaBookToBookcaseMetaEntity> newIter = targetEntity.iterator();

        WatchaBookToBookcaseMetaEntity dbItem = null;
        WatchaBookToBookcaseMetaEntity newItem = null;

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
                    updated++;
                    log.info("update: {}", dbItem.getBookcaseCode());
                } else {
                    log.info("same: {} {}", dbItem.getBookcaseCode(), newItem.getBookcaseCode());
                }

                if (newIter.hasNext()) {
                    newItem = newIter.next();
                }

                if (dbIter.hasNext()) {
                    dbItem = dbIter.next();
                } else {
                    break;
                }


            } else {
                // db에 없던 item이면 -> insert
                WatchaUserRepository.getInstance().insertOrUpdate(newItem.getUser(), em);
                em.persist(newItem);

                inserted++;
                log.info("insert: {}", newItem.getBookcaseCode());

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
            log.info("inactivate: {}", dbItem.getBookcaseCode());
        }

        log.info("inserted:{}, inactivated:{}, updated:{}", inserted, inActivated, updated);
    }

}
