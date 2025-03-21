package com.bookie.scrap.watcha.repository;

import com.bookie.scrap.common.lifecycle.InitManager;
import com.bookie.scrap.watcha.entity.WatchaBookEntity;
import com.bookie.scrap.watcha.request.WatchaBookMetaRequestFactory;
import com.bookie.scrap.watcha.dto.WatchaBookMetaDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

class WatchaBookMetaRepositoryTest {

    @BeforeAll
    public static void init() {
        new InitManager().devInit();
    }

    @Test
    public void dbTest() {
        WatchaBookMetaDto book = WatchaBookMetaRequestFactory.getInstance().createRequest("byLKj8M").execute();

        boolean isInserted = WatchaBookMetaRepository.getInstance().insertOrUpdate(book.toEntity());
        Assertions.assertTrue(isInserted);

    }

    @Test
    public void selectTest() {
        List<WatchaBookEntity> results = WatchaBookMetaRepository.getInstance().select("byLKj8M");
    }


}