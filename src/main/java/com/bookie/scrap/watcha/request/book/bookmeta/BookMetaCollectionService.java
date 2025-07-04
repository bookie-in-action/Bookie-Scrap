package com.bookie.scrap.watcha.request.book.bookmeta;

import com.bookie.scrap.common.domain.PageInfo;
import com.bookie.scrap.common.exception.CollectionEx;
import com.bookie.scrap.common.exception.RetriableCollectionEx;
import com.bookie.scrap.common.exception.WatchaCustomCollectionEx;
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
    public int collect(String bookCode, PageInfo param) throws WatchaCustomCollectionEx {
        try {
            BookMetaResponseDto response = fetcher.fetch(bookCode, param);

            if (response == null || response.getBookMeta() == null || response.getBookMeta().isEmpty()) {
                log.warn("bookCode={} 의 bookMeta 수집 실패: fetch 결과가 null이거나 정보 없음", bookCode);
                return 0;
            }

            if (!response.isBook()) {
                log.info("code:{}는 content_type: {}", bookCode, response.getContentType());
                throw new WatchaCustomCollectionEx("code: " + bookCode + "는 content_type: " + response.getContentType());
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
