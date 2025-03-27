package com.bookie.scrap.watcha.repository;


import com.bookie.scrap.watcha.entity.WatchaCommentEntity;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class WatchaCommentRepository {

    private static final WatchaCommentRepository INSTANCE = new WatchaCommentRepository();

    public static WatchaCommentRepository getInstance() {
        return INSTANCE;
    }

    public List<WatchaCommentEntity> selectWithCode(String commentCode, EntityManager em) {

        String jpql = "SELECT w FROM WatchaCommentEntity w WHERE w.commentCode = :commentCode";

        List<WatchaCommentEntity> results = em.createQuery(jpql, WatchaCommentEntity.class)
                .setParameter("commentCode", commentCode)
                .getResultList();

        return results;

    }

    public void insertOrUpdate(String bookCode, List<WatchaCommentEntity> existingEntities, EntityManager em) {

        String jpql = "SELECT e FROM WatchaCommentEntity e WHERE e.bookCode = :bookCode";

        List<WatchaCommentEntity> dbEntities = em.createQuery(jpql, WatchaCommentEntity.class)
                .setParameter("bookCode", bookCode)
                .getResultList();



    }

}
