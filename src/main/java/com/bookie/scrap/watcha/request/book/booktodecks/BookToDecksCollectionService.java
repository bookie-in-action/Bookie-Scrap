package com.bookie.scrap.watcha.request.book.booktodecks;

import com.bookie.scrap.common.domain.PageInfo;
import com.bookie.scrap.common.exception.CollectionEx;
import com.bookie.scrap.common.exception.RetriableCollectionEx;
import com.bookie.scrap.common.redis.RedisStringListService;
import com.bookie.scrap.watcha.domain.WatchaCollectorService;
import com.mongodb.MongoTimeoutException;
import io.lettuce.core.RedisCommandTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service
public class BookToDecksCollectionService  implements WatchaCollectorService{

    private final BookToDecksFetcher fetcher;
    private final BookToDecksPersister persister;
    private final RedisStringListService deckRedisService;

    public BookToDecksCollectionService(
            BookToDecksFetcher fetcher,
            BookToDecksPersister persister,
            @Qualifier("pendingDeckCode") RedisStringListService deckRedisService
    ) {
        this.fetcher = fetcher;
        this.persister = persister;
        this.deckRedisService = deckRedisService;
    }

    @Override
    @Transactional
    public int collect(String bookCode, PageInfo param) {

        try {
            BookToDecksResponseDto response = fetcher.fetch(bookCode, param);
            if (response == null || response.hasNoData()) {
                log.warn("bookCode={} 의 toDecks 수집 실패: fetch 결과가 null이거나 정보없음", bookCode);
                return 0;
            }

            try {
                int savedCnt = persister.persist(response, bookCode);
                if (savedCnt != 0) {
                    deckRedisService.add(response.getResult().getDeckCodes());
                }

                log.info(
                        "bookCode={} toDecks service page={} saved={}/{} success",
                        bookCode,
                        param.getPage(),
                        response.getResult().getDecks().size(),
                        savedCnt
                );

                return savedCnt;
            } catch (MongoTimeoutException e) {
                throw new RetriableCollectionEx("bookCode=" + bookCode + " toDeck DB 연결 실패", e);
            }

        } catch (Exception e) {
            throw new CollectionEx("bookCode:" + bookCode + ":BookToDecksCollectionService", e);
        }
    }

    @Transactional
    public List<String> collectForTest(String bookCode, PageInfo param) {
        try {
            BookToDecksResponseDto response = fetcher.fetch(bookCode, param);
            persister.persist(response, bookCode);
            return response.getResult().getDeckCodes();
        } catch (Exception e) {
            throw new CollectionEx("bookCode:" + bookCode + ":BookToDecksCollectionService", e);
        }
    }
}
