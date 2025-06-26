package com.bookie.scrap.watcha.request.book.bookmeta;

import com.bookie.scrap.common.domain.PageInfo;
import com.bookie.scrap.common.exception.CollectionEx;
import com.bookie.scrap.common.exception.RetriableCollectionEx;
import com.bookie.scrap.watcha.domain.WatchaCollectorService;
import com.bookie.scrap.watcha.request.book.bookmeta.rdb.BookMetaRdbPersister;
import com.mongodb.MongoTimeoutException;
import io.lettuce.core.RedisCommandTimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLNonTransientConnectionException;


@Slf4j
@Service
@RequiredArgsConstructor
public class BookMetaCollectionService implements WatchaCollectorService {

    private final BookMetaFetcher fetcher;
    private final BookMetaNosqlPersister noSqlpersister;
    private final BookMetaRdbPersister rdbPersister;

    @Override
    @Transactional
    public int collect(String bookCode, PageInfo param) {
        try {
            BookMetaResponseDto response = fetcher.fetch(bookCode, param);

            if (response == null) {
                log.warn("bookCode={} 의 comment 수집 실패: fetch 결과가 null", bookCode);
                return 0;
            }

            try {
                noSqlpersister.persist(response, bookCode);
                return rdbPersister.persist(response, bookCode);
            } catch (RedisCommandTimeoutException | MongoTimeoutException e) {
                log.warn("bookCode={} comment DB 연결 실패: {}", bookCode, e.getMessage());
                throw new RetriableCollectionEx("DB 연결 실패", e);
            }
        } catch (Exception e) {
            throw new CollectionEx("bookCode:" + bookCode + ":BookMetaCollectionService", e);
        }
    }

}
