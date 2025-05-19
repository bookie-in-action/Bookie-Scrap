package com.bookie.scrap.watcha.repository;

import com.bookie.legacy.common.db.EntityManagerFactoryProvider;
import com.bookie.legacy.common.lifecycle.InitManager;
import com.bookie.legacy.watcha.domain.WatchaRequestParam;
import com.bookie.legacy.watcha.dto.WatchaBookMetaDto;
import com.bookie.legacy.watcha.dto.WatchaBookcaseMetaDto;
import com.bookie.legacy.watcha.entity.WatchaBookToBookcaseMetaEntity;
import com.bookie.legacy.watcha.repository.WatchaBookMetaRepository;
import com.bookie.legacy.watcha.repository.WatchaBookToBookcaseMetasRepository;
import com.bookie.legacy.watcha.request.WatchaBookMetaRequestFactory;
import com.bookie.legacy.watcha.request.WatchaBookToBookcaseMetasRequestFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
class WatchaBookToBookcaseMetasRepositoryTest {

    private static WatchaBookToBookcaseMetasRepository repo = WatchaBookToBookcaseMetasRepository.getInstance();
    private static EntityManagerFactory emf;

    @BeforeAll
    public static void init() {
        new InitManager().devInit();
        emf = EntityManagerFactoryProvider.getInstance().getEntityManagerFactory();
    }

    @Test
    public void dbTest() {
        try (EntityManager em = emf.createEntityManager()) {
            WatchaBookMetaDto book = WatchaBookMetaRequestFactory.getInstance().createRequest("byLKj8M").execute();

            WatchaBookMetaRepository.getInstance().insertOrUpdate(book.toEntity(), em);
        }
    }

    @Test
    public void selectWithCodeTest() {
        try (EntityManager em = emf.createEntityManager()) {

            List<WatchaBookToBookcaseMetaEntity> results = WatchaBookToBookcaseMetasRepository.getInstance().selectWithCode("gcdk0WAdV9", em);
            Assertions.assertEquals(1, results.size());
            Assertions.assertEquals("민음사TV", results.get(0).getBookcaseTitle());
        }
    }

    @Test
    void insertOrUpdate() {
        EntityManager em = emf.createEntityManager();

        Assertions.assertDoesNotThrow(() -> {
            try {
                em.getTransaction().begin();

                WatchaRequestParam requestParam = new WatchaRequestParam(1, 12, "", "");
                String bookCode = "byLKj8M";


                List<WatchaBookcaseMetaDto> result = WatchaBookToBookcaseMetasRequestFactory.getInstance().createRequest(bookCode, requestParam).execute();
                List<WatchaBookToBookcaseMetaEntity> resultEntities = result.stream().map(WatchaBookcaseMetaDto::toEntity).collect(Collectors.toList());
                repo.insertOrUpdate(bookCode, resultEntities, em);

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