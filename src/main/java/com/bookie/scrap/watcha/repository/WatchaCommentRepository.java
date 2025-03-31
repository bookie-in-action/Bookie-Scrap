package com.bookie.scrap.watcha.repository;


import com.bookie.scrap.watcha.entity.WatchaCommentEntity;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public void insertOnlyNewComments(List<WatchaCommentEntity> existingEntities, EntityManager em) {

        Map<String, WatchaCommentEntity> newCommentEntityMap = existingEntities.stream()
                .collect(Collectors.toMap(
                        WatchaCommentEntity::getCommentCode,
                        Function.identity())
                );

        String jpql = "SELECT e FROM WatchaCommentEntity e WHERE e.commentCode IN :commentCodes";

        Set<String> dbCommentCodes = em.createQuery(jpql, WatchaCommentEntity.class)
                .setParameter("commentCodes", newCommentEntityMap.keySet())
                .getResultList()
                .stream().map(WatchaCommentEntity::getCommentCode)
                .collect(Collectors.toSet());

        // db에 없는 comment만 insert
        for (Map.Entry<String, WatchaCommentEntity> entry : newCommentEntityMap.entrySet()) {
            if (!dbCommentCodes.contains(entry.getKey())) {
                em.persist(entry.getValue());
            }
        }

    }

}
