package com.bookie.scrap.watcha.request.deck.booklist;

import com.bookie.scrap.common.domain.PageInfo;
import com.bookie.scrap.common.exception.CollectionEx;
import com.bookie.scrap.common.exception.RetriableCollectionEx;
import com.bookie.scrap.common.redis.RedisStringListService;
import com.bookie.scrap.watcha.domain.WatchaCollectorService;
import com.bookie.scrap.watcha.request.book.booktodecks.BookToDecksResponseDto;
import com.mongodb.MongoTimeoutException;
import io.lettuce.core.RedisCommandTimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class BooListCollectionService implements WatchaCollectorService {

    private final BookListFetcher fetcher;
    private final BookListPersister persister;
    private final RedisStringListService bookRedisService;


    public BooListCollectionService(
            BookListFetcher fetcher,
            BookListPersister persister,
            @Qualifier("pendingBookCode") RedisStringListService bookRedisService
    ) {
        this.fetcher = fetcher;
        this.persister = persister;
        this.bookRedisService = bookRedisService;
    }

    @Override
    @Transactional
    public int collect(String deckCode, PageInfo param) {
        try {
            BookListResponseDto response = fetcher.fetch(deckCode, param);

            if (response == null) {
                log.warn("deckCode={} 의 bookList 수집 실패: fetch 결과가 null", deckCode);
                return 0;
            }
            try {
                bookRedisService.add(response.getResult().getBookCodes());
                int savedCnt = persister.persist(response, deckCode);
                log.info(
                        "deckCode={} bookList service page={} saved={}/{} success",
                        deckCode,
                        param.getPage(),
                        param.getSize(),
                        savedCnt
                );

                return savedCnt;
            } catch (RedisCommandTimeoutException | MongoTimeoutException e) {
                throw new RetriableCollectionEx("deckCode=" + deckCode + " bookList DB 연결 실패", e);
            }
        } catch (Exception e) {
            throw new CollectionEx("deckCode:" + deckCode + ":DeckCollectionService", e);
        }
    }

    @Transactional
    public List<String> collectForTest(String deckCode, PageInfo param) throws CollectionEx {
        try {
            BookListResponseDto response = fetcher.fetch(deckCode, param);
            persister.persist(response, deckCode);
            return response.getResult().getBookCodes();
        } catch (Exception e) {
            throw new CollectionEx("deckCode:" + deckCode + ":DeckCollectionService", e);
        }
    }
}
