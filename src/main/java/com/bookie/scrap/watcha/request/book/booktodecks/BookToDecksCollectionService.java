package com.bookie.scrap.watcha.request.book.booktodecks;

import com.bookie.scrap.common.domain.PageInfo;
import com.bookie.scrap.common.exception.CollectionEx;
import com.bookie.scrap.common.exception.RetriableCollectionEx;
import com.bookie.scrap.watcha.domain.WatchaCollectorService;
import com.mongodb.MongoTimeoutException;
import io.lettuce.core.RedisCommandTimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class BookToDecksCollectionService  implements WatchaCollectorService{

    private final BookToDecksFetcher fetcher;
    private final BookToDecksPersister persister;

    @Override
    @Transactional
    public int collect(String bookCode, PageInfo param) {

        try {
            BookToDecksResponseDto response = fetcher.fetch(bookCode, param);
            if (response == null) {
                log.warn("bookCode={} 의 toDecks 수집 실패: fetch 결과가 null", bookCode);
                return 0;
            }

            try {
                return persister.persist(response, bookCode);
            } catch (RedisCommandTimeoutException | MongoTimeoutException e) {
                log.warn("bookCode={} toDecks DB 연결 실패: {}", bookCode, e.getMessage());
                throw new RetriableCollectionEx("DB 연결 실패", e);
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
