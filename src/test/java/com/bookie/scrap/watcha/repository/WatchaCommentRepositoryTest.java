package com.bookie.scrap.watcha.repository;

import com.bookie.scrap.common.db.EntityManagerFactoryProvider;
import com.bookie.scrap.common.lifecycle.InitManager;
import com.bookie.scrap.watcha.domain.WatchaRequestParam;
import com.bookie.scrap.watcha.dto.WatchaCommentDto;
import com.bookie.scrap.watcha.entity.WatchaCommentEntity;
import com.bookie.scrap.watcha.request.WatchaCommentRequestFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
class WatchaCommentRepositoryTest {

    private static WatchaCommentRepository repo = WatchaCommentRepository.getInstance();
    private static EntityManagerFactory emf;

    @BeforeAll
    public static void init() {
        new InitManager().devInit();
        emf = EntityManagerFactoryProvider.getInstance().getEntityManagerFactory();
    }


    @Test
    void insertOnlyNewComments() {
        EntityManager em = emf.createEntityManager();

        Assertions.assertDoesNotThrow(() -> {
            try {
                em.getTransaction().begin();

                WatchaRequestParam requestParam = new WatchaRequestParam(1, 12, "", "");
                String bookCode = "byLKj8M";


                List<WatchaCommentDto> result = WatchaCommentRequestFactory.getInstance().createRequest(bookCode, requestParam).execute();
                List<WatchaCommentEntity> resultEntities = result.stream().map(WatchaCommentDto::toEntity).collect(Collectors.toList());
                repo.insertOnlyNewComments(resultEntities, em);

                em.getTransaction().commit();
            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    log.warn("Transaction is active, rolling back...", e);
                    em.getTransaction().rollback();
                } else {
                    log.warn("Transaction is not active, skipping rollback", e);
                }

                throw new RuntimeException(e);
            } finally {
                em.close();
            }
        });
    }


}