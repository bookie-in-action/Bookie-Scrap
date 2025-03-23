package com.bookie.scrap.watcha.repository;

import com.bookie.scrap.common.domain.Request;
import com.bookie.scrap.common.lifecycle.InitManager;
import com.bookie.scrap.watcha.domain.WatchaRequestParam;
import com.bookie.scrap.watcha.dto.WatchaBookMetaDto;
import com.bookie.scrap.watcha.dto.WatchaBookcaseMetaDto;
import com.bookie.scrap.watcha.entity.WatchaBookEntity;
import com.bookie.scrap.watcha.entity.WatchaBookcaseMetaEntity;
import com.bookie.scrap.watcha.request.WatchaBookMetaRequestFactory;
import com.bookie.scrap.watcha.request.WatchaBookcaseMetaRequestFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WatchaBookcaseMetaRepositoryTest {

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
    public void selectWithCodeTest() {
        List<WatchaBookcaseMetaEntity> results = WatchaBookcaseMetaRepository.getInstance().selectWithCode("gcdk0WAdV9");
        Assertions.assertEquals(1, results.size());
        Assertions.assertEquals("민음사TV", results.get(0).getBookcaseTitle());
    }

    @Test
    void insertOrUpdate() {
        WatchaRequestParam requestParam = new WatchaRequestParam(1, 12, "", "");
        List<WatchaBookcaseMetaDto> result = WatchaBookcaseMetaRequestFactory.getInstance().createRequest("byLKj8M", requestParam).execute();

        WatchaBookcaseMetaRepository repository = WatchaBookcaseMetaRepository.getInstance();
        for (WatchaBookcaseMetaDto metaDto : result) {
            boolean isInserted = repository.insertOrUpdate(metaDto.toEntity());
            Assertions.assertTrue(isInserted);
        }

    }
}