package com.bookie.legacy.watcha.repository;

import com.bookie.legacy.common.db.EntityManagerFactoryProvider;
import com.bookie.legacy.common.lifecycle.InitManager;
import com.bookie.legacy.watcha.domain.WatchaRequestParam;
import com.bookie.legacy.watcha.dto.WatchaBookcaseToBookDto;
import com.bookie.legacy.watcha.entity.WatchaBookcaseToBookEntity;
import com.bookie.legacy.watcha.repository.WatchaBookcaseToBooksRepository;
import com.bookie.legacy.watcha.request.WatchaBookcaseToBooksRequestFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
class WatchaBookcaseToBooksRepositoryTest {

    private static WatchaBookcaseToBooksRepository repo = WatchaBookcaseToBooksRepository.getInstance();
    private static EntityManagerFactory emf;

    @BeforeAll
    public static void init() {
        new InitManager().devInit();
        emf = EntityManagerFactoryProvider.getInstance().getEntityManagerFactory();
    }


    @Test
    void insertOrUpdate() {
        EntityManager em = emf.createEntityManager();

        Assertions.assertDoesNotThrow(() -> {
            try {
                em.getTransaction().begin();

                WatchaRequestParam requestParam = new WatchaRequestParam(1, 12, "", "");
                String bookcaseCode = "gcdk0WAdV9";


                List<WatchaBookcaseToBookDto> result = WatchaBookcaseToBooksRequestFactory.getInstance().createRequest(bookcaseCode, requestParam).execute();
                List<WatchaBookcaseToBookEntity> resultEntities = result.stream().map(WatchaBookcaseToBookDto::toEntity).collect(Collectors.toList());
                repo.insertOrUpdate(bookcaseCode, resultEntities, em);

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