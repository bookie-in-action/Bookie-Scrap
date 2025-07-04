package com.bookie.scrap.watcha.request.user.userwishbook;

import com.bookie.scrap.common.domain.PageInfo;
import com.bookie.scrap.common.exception.CollectionEx;
import com.bookie.scrap.common.exception.RetriableCollectionEx;
import com.bookie.scrap.common.redis.RedisStringListService;
import com.bookie.scrap.watcha.domain.WatchaCollectorService;
import com.bookie.scrap.watcha.request.deck.booklist.BookListFetcher;
import com.bookie.scrap.watcha.request.deck.booklist.BookListPersister;
import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.MongoTimeoutException;
import io.lettuce.core.RedisCommandTimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
public class UserWishBookCollectionService implements WatchaCollectorService {

    private final UserWishBookFetcher fetcher;
    private final UserWishBookPersister persister;
    private final RedisStringListService bookRedisService;


    public UserWishBookCollectionService(
            UserWishBookFetcher fetcher,
            UserWishBookPersister persister,
            @Qualifier("pendingBookCode") RedisStringListService bookRedisService
    ) {
        this.fetcher = fetcher;
        this.persister = persister;
        this.bookRedisService = bookRedisService;
    }

    @Override
    @Transactional
    public int collect(String userCode, PageInfo param) throws CollectionEx {
        try {
            UserWishBookResponseDto response = fetcher.fetch(userCode, param);

            if (response == null) {
                log.warn("userCode={} 의 userWishBook 수집 실패: fetch 결과가 null", userCode);
                return 0;
            }

            try {
                int savedCnt = persister.persist(response, userCode);
                log.info(
                        "userCode={} userWishBook service page={} saved={}/{} success",
                        userCode,
                        param.getPage(),
                        param.getSize(),
                        savedCnt
                );
                bookRedisService.add(response.getResult().getUserWishBookCodes());
                return savedCnt;
            } catch (RedisCommandTimeoutException | MongoTimeoutException e) {
                throw new RetriableCollectionEx("userCode=" + userCode + " userWishBook DB 연결 실패", e);
            }
        } catch (Exception e) {
            throw new CollectionEx("userCode:" + userCode + ":UserWishBookCollectionService", e);
        }
    }
}
