package com.bookie.scrap.watcha.repository;

import com.bookie.scrap.common.EntityManagerFactoryProvider;
import com.bookie.scrap.domain.Initializer;
import com.bookie.scrap.http.HttpRequestExecutor;
import com.bookie.scrap.watcha.config.WatchaBook;
import com.bookie.scrap.watcha.dto.WatchaBookEntity;
import com.bookie.scrap.watcha.response.WatchaBookDetail;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;


class WatchaHibenateImplTest {
    @BeforeAll
    public static void init() {
        new Initializer().init("dev");

    }

    @Test
    public void dbTest() {
        EntityManagerFactoryProvider.getInstance().init("dev");
        WatchaBookDetail book = HttpRequestExecutor.execute(new WatchaBook("byLKj8M"));

        boolean isInserted = WatchaBookRepository.getInstance().insertOrUpdate(book.toEntity());
        Assertions.assertTrue(isInserted);

    }

    @Test
    public void selectTest() {
        EntityManagerFactoryProvider.getInstance().init("dev");

        Optional<WatchaBookEntity> select = WatchaHibenateImpl.getInstance(WatchaBookEntity.class).select("1");
        Assertions.assertTrue(select.isPresent());
        Assertions.assertEquals("byLKj8M", select.get().getBookCode());
    }
}