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


@Slf4j
@Service
@RequiredArgsConstructor
public class BookMetaCollectionService implements WatchaCollectorService {

    private final BookMetaFetcher fetcher;
    private final BookMetaNosqlPersister noSqlPersister;
    private final BookMetaRdbPersister rdbPersister;

    @Override
    @Transactional
    public int collect(String bookCode, PageInfo param) {
        try {
            BookMetaResponseDto response = fetcher.fetch(bookCode, param);

            if (response == null) {
                log.warn("bookCode={} 의 bookMeta 수집 실패: fetch 결과가 null", bookCode);
                return 0;
            }

            try {
                int noSqlSavedCnt = noSqlPersister.persist(response, bookCode);
                int sqlSavedCnt = rdbPersister.persist(response, bookCode);
                int savedCnt = 0;
                if (noSqlSavedCnt < sqlSavedCnt) {
                    savedCnt = noSqlSavedCnt;
                }
                savedCnt = sqlSavedCnt;

                log.info(
                        "bookCode={} bookMeta service page={} saved={}/{} success",
                        bookCode,
                        param.getPage(),
                        param.getSize(),
                        savedCnt
                );
                return savedCnt;

            } catch (RedisCommandTimeoutException | MongoTimeoutException e) {
                throw new RetriableCollectionEx("bookCode=" + bookCode + " bookMeta DB 연결 실패", e);
            }
        } catch (Exception e) {
            throw new CollectionEx("bookCode:" + bookCode + ":BookMetaCollectionService", e);
        }
    }

}
