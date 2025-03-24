package com.bookie.scrap.watcha.repository;

import com.bookie.scrap.common.db.EntityManagerFactoryProvider;
import com.bookie.scrap.common.lifecycle.InitManager;
import com.bookie.scrap.watcha.entity.WatchaBookMetaEntity;
import com.bookie.scrap.watcha.request.WatchaBookMetaRequestFactory;
import com.bookie.scrap.watcha.dto.WatchaBookMetaDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

class WatchaBookMetaRepositoryTest {

    private static final EntityManagerFactory emf = EntityManagerFactoryProvider.getInstance().getEntityManagerFactory();

    @BeforeAll
    public static void init() {
        new InitManager().devInit();
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
            List<WatchaBookMetaEntity> results = WatchaBookMetaRepository.getInstance().selectWithCode("byLKj8M", em);
        }
    }


}