package com.bookie.scrap.watcha.request.book.bookmeta;

import com.bookie.scrap.common.domain.PageInfo;
import com.bookie.scrap.watcha.domain.WatchaCollectorService;
import com.bookie.scrap.watcha.request.book.bookmeta.rdb.BookMetaRdbPersister;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookMetaCollectionService implements WatchaCollectorService {

    private final BookMetaFetcher fetcher;
    private final BookMetaNosqlPersister noSqlpersister;
    private final BookMetaRdbPersister rdbPersister;

    @Override
    public void collect(String bookCode, PageInfo param) throws Exception{
            BookMetaResponseDto response = fetcher.fetch(bookCode, param);
            noSqlpersister.persist(response, bookCode);
            rdbPersister.persist(response, bookCode);
    }
}

