package com.bookie.scrap.watcha.request.book.bookcomment;

import com.bookie.scrap.common.domain.PageInfo;
import com.bookie.scrap.common.exception.CollectionEx;
import com.bookie.scrap.common.exception.RetriableCollectionEx;
import com.bookie.scrap.common.redis.RedisStringListService;
import com.bookie.scrap.watcha.domain.WatchaCollectorService;
import com.mongodb.MongoTimeoutException;
import io.lettuce.core.RedisCommandTimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
public class BookCommentCollectionService implements WatchaCollectorService {

    private final BookCommentFetcher fetcher;
    private final BookCommentPersister persister;
    private final RedisStringListService userRedisService;

    public BookCommentCollectionService(
            BookCommentFetcher fetcher,
            BookCommentPersister persister,
            @Qualifier("pendingUserCode") RedisStringListService userRedisService
    ) {
        this.fetcher = fetcher;
        this.persister = persister;
        this.userRedisService = userRedisService;
    }

    @Override
    @Transactional
    public int collect(String bookCode, PageInfo param) {

        try {
            BookCommentResponseDto response = fetcher.fetch(bookCode, param);

            if (response == null || response.getResult() == null) {
                log.warn("bookCode={} 의 bookComment 수집 실패: fetch 결과가 null이거나 정보 없음", bookCode);
                return 0;
            }

            try {
                int savedCnt = persister.persist(response, bookCode);
                if (savedCnt != 0) {
                    userRedisService.add(response.getResult().getUserCodes());
                }

                log.info(
                        "bookCode={} bookComment service page={} saved={}/{} success",
                        bookCode,
                        param.getPage(),
                        param.getSize(),
                        savedCnt
                );

                return savedCnt;
            } catch (RedisCommandTimeoutException | MongoTimeoutException e) {
                throw new RetriableCollectionEx("bookCode=" + bookCode + " bookComment DB 연결 실패", e);
            }
        }
        catch (Exception e) {
            throw new CollectionEx("bookCode:" + bookCode + ":BookCommentCollectionService", e);
        }
    }

}

