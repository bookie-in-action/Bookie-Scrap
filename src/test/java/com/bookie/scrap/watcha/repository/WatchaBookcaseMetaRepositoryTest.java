package com.bookie.scrap.watcha.repository;

import com.bookie.scrap.common.db.EntityManagerFactoryProvider;
import com.bookie.scrap.common.domain.Repository;
import com.bookie.scrap.common.domain.Request;
import com.bookie.scrap.common.lifecycle.InitManager;
import com.bookie.scrap.watcha.domain.WatchaRequestParam;
import com.bookie.scrap.watcha.dto.WatchaBookMetaDto;
import com.bookie.scrap.watcha.dto.WatchaBookcaseMetaDto;
import com.bookie.scrap.watcha.entity.WatchaBookcaseMetaEntity;
import com.bookie.scrap.watcha.request.WatchaBookMetaRequestFactory;
import com.bookie.scrap.watcha.request.WatchaBookToBookcaseMetaRequestFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

@Slf4j
class WatchaBookcaseMetaRepositoryTest {

    private static Repository<WatchaBookcaseMetaEntity> bookcaseMetaRepository = WatchaBookcaseMetaRepository.getInstance();
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

            List<WatchaBookcaseMetaEntity> results = WatchaBookcaseMetaRepository.getInstance().selectWithCode("gcdk0WAdV9", em);
            Assertions.assertEquals(1, results.size());
            Assertions.assertEquals("민음사TV", results.get(0).getBookcaseTitle());
        }
    }

    @Test
    void insertOrUpdate() {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            WatchaRequestParam requestParam = new WatchaRequestParam(1, 12, "", "");
            String bookCode = "byLKj8M";


            List<WatchaBookcaseMetaDto> result = WatchaBookToBookcaseMetaRequestFactory.getInstance().createRequest(bookCode, requestParam).execute();

            Request<WatchaBookMetaDto> request = WatchaBookMetaRequestFactory.getInstance().createRequest(bookCode);
            WatchaBookMetaDto bookMetaDto = request.execute();

            for (WatchaBookcaseMetaDto metaDto : result) {
                bookcaseMetaRepository.insertOrUpdate(metaDto.toEntity(), em);
            }


            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                log.warn("Transaction is active, rolling back...", e);
                em.getTransaction().rollback();
            } else {
                log.warn("Transaction is not active, skipping rollback", e);
            }

        } finally {
            em.close();
        }

    }
}