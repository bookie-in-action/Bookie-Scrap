package com.bookie.scrap.watcha.request.user.userbookrating;

import com.bookie.scrap.common.domain.PageInfo;
import com.bookie.scrap.common.exception.CollectionEx;
import com.bookie.scrap.common.exception.RetriableCollectionEx;
import com.bookie.scrap.watcha.domain.WatchaCollectorService;
import com.mongodb.MongoTimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserBookRatingCollectionService implements WatchaCollectorService {

    private final UserBookRatingFetcher fetcher;
    private final UserBookRatingPersister persister;

    @Override
    @Transactional
    public int collect(String userCode, PageInfo param) throws CollectionEx {
        try {
            UserBookRatingResponseDto response = fetcher.fetch(userCode, param);

            if (response == null || response.hasNoData()) {
                log.warn("userCode={} 의 userBookRating 수집 실패: fetch 결과가 null이거나 정보없음", userCode);
                return 0;
            }

            try {
                int savedCnt = persister.persist(response, userCode);
                log.info(
                        "userCode={} userBookRating service page={} saved={}/{} success",
                        userCode,
                        param.getPage(),
                        response.getResult().getBookRatings().size(),
                        savedCnt
                );
                return savedCnt;
            } catch (MongoTimeoutException e) {
                throw new RetriableCollectionEx("userCode=" + userCode + " userBookRating DB 연결 실패", e);
            }
        } catch (Exception e) {
            throw new CollectionEx("userCode:" + userCode + ":UserBookRatingCollectionService", e);
        }
    }
}
