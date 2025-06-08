package com.bookie.scrap.watcha.request.book.bookmeta;

import com.bookie.scrap.common.domain.PageInfo;
import com.bookie.scrap.common.exception.CollectionEx;
import com.bookie.scrap.watcha.domain.WatchaCollectorService;
import com.bookie.scrap.watcha.request.book.bookmeta.rdb.BookMetaRdbPersister;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.bookie.scrap.common.exception.CollectionEx.MAKE;

@Service
@RequiredArgsConstructor
public class BookMetaCollectionService implements WatchaCollectorService {

    private final BookMetaFetcher fetcher;
    private final BookMetaNosqlPersister noSqlpersister;
    private final BookMetaRdbPersister rdbPersister;

    @Override
    @Transactional
    public int collect(String bookCode, PageInfo param) throws CollectionEx {
        try {
            BookMetaResponseDto response = fetcher.fetch(bookCode, param);
            noSqlpersister.persist(response, bookCode);
            return rdbPersister.persist(response, bookCode);
        } catch (Exception e) {
            throw MAKE("bookCode:" + bookCode + ":BookMetaCollectionService", e);
        }
    }
}
