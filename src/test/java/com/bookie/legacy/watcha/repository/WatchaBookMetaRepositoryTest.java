package com.bookie.legacy.watcha.repository;

import com.bookie.legacy.common.db.EntityManagerFactoryProvider;
import com.bookie.legacy.common.lifecycle.InitManager;
import com.bookie.legacy.watcha.dto.WatchaBookMetaDto;
import com.bookie.legacy.watcha.entity.WatchaBookMetaEntity;
import com.bookie.legacy.watcha.repository.WatchaBookMetaRepository;
import com.bookie.legacy.watcha.request.WatchaBookMetaRequestFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

class WatchaBookMetaRepositoryTest {

    private static EntityManagerFactory emf;

    @BeforeAll
    public static void init() {
        new InitManager().devInit();
        emf = EntityManagerFactoryProvider.getInstance().getEntityManagerFactory();
    }

    @Test
    public void dbTest() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            WatchaBookMetaDto book = WatchaBookMetaRequestFactory.getInstance().createRequest("bowenQY").execute();

            WatchaBookMetaRepository.getInstance().insertOrUpdate(book.toEntity(), em);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        }
    }

    @Test
    public void selectWithCodeTest() {
        try (EntityManager em = emf.createEntityManager()) {
            List<WatchaBookMetaEntity> results = WatchaBookMetaRepository.getInstance().selectWithCode("byLKj8M", em);
        }
    }


}