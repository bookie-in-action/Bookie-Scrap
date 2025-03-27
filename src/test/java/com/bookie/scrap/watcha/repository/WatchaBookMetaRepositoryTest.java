package com.bookie.scrap.watcha.repository;

import com.bookie.scrap.common.db.EntityManagerFactoryProvider;
import com.bookie.scrap.common.lifecycle.InitManager;
import com.bookie.scrap.watcha.dto.WatchaBookMetaDto;
import com.bookie.scrap.watcha.entity.WatchaBookMetaEntity;
import com.bookie.scrap.watcha.request.WatchaBookMetaRequestFactory;
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
            WatchaBookMetaDto book = WatchaBookMetaRequestFactory.getInstance().createRequest("byLKj8M").execute();

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