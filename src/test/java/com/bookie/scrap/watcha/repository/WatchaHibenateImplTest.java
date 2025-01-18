package com.bookie.scrap.watcha.repository;

import com.bookie.scrap.common.EntityManagerFactoryProvider;
import com.bookie.scrap.http.HttpRequestExecutor;
import com.bookie.scrap.properties.BookieProperties;
import com.bookie.scrap.properties.DbProperties;
import com.bookie.scrap.properties.InitializableProperties;
import com.bookie.scrap.properties.SchedulerProperties;
import com.bookie.scrap.watcha.config.WatchaBook;
import com.bookie.scrap.watcha.dto.WatchaBookEntity;
import com.bookie.scrap.watcha.response.WatchaBookDetail;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


class WatchaHibenateImplTest {
    @BeforeAll
    public static void init() {
        List<InitializableProperties> propertiesList = Arrays.asList(
                BookieProperties.getInstance(),
                DbProperties.getInstance(),
                SchedulerProperties.getInstance()
        );

        propertiesList.stream().forEach(properties -> {
            properties.init("dev");
            properties.verify();
        });

    }

    @Test
    public void dbTest() {
        EntityManagerFactoryProvider.getInstance().init();
        WatchaBookDetail book = HttpRequestExecutor.execute(new WatchaBook("byLKj8M"));

        boolean isInserted = WatchaBookRepository.getInstance().insertOrUpdate(book.toEntity());
        Assertions.assertTrue(isInserted);

    }

    @Test
    public void selectTest() {
        EntityManagerFactoryProvider.getInstance().init();

        Optional<WatchaBookEntity> select = WatchaHibenateImpl.getInstance(WatchaBookEntity.class).select("1");
        Assertions.assertTrue(select.isPresent());
        Assertions.assertEquals("byLKj8M", select.get().getCode());
    }
}