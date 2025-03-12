package com.bookie.scrap.watcha.repository;

import com.bookie.scrap.common.startup.Initializer;
import com.bookie.scrap.watcha.entity.WatchaBookEntity;
import com.bookie.scrap.watcha.request.WatchaBookRequestFactory;
import com.bookie.scrap.watcha.dto.WatchaBookDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;

class WatchaBookRepositoryTest {

    @BeforeAll
    public static void init() {
        new Initializer().devInit();
    }

    @Test
    public void dbTest() {
        WatchaBookDto book = WatchaBookRequestFactory.getInstance().createRequest("byLKj8M").execute();

        boolean isInserted = WatchaBookRepository.getInstance().insertOrUpdate(book.toEntity());
        Assertions.assertTrue(isInserted);

    }

    @Test
    public void selectTest() {
        Optional<WatchaBookEntity> select = WatchaBookRepository.getInstance().select("byLKj8M");
        Assertions.assertTrue(select.isPresent());
        Assertions.assertEquals("byLKj8M", select.get().getBookCode());
    }

}