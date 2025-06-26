package com.bookie.scrap.watcha;

import com.bookie.scrap.common.exception.CollectionEx;
import com.bookie.scrap.common.exception.RetriableCollectionEx;
import com.bookie.scrap.common.redis.RedisHashService;
import com.bookie.scrap.common.redis.RedisProcessResult;
import com.bookie.scrap.common.redis.RedisStringListService;
import com.bookie.scrap.watcha.domain.WatchaPageInfo;
import com.bookie.scrap.watcha.request.book.bookcomment.BookCommentCollectionService;
import com.bookie.scrap.watcha.request.book.bookcomment.WatchaBookCommentParam;
import com.bookie.scrap.watcha.request.book.bookmeta.BookMetaCollectionService;
import com.bookie.scrap.watcha.request.book.booktodecks.BookToDecksCollectionService;
import com.bookie.scrap.watcha.request.book.booktodecks.BookToDecksParam;
import com.bookie.scrap.watcha.request.deck.booklist.BooListCollectionService;
import com.bookie.scrap.watcha.request.deck.booklist.WatchaBookListParam;
import com.bookie.scrap.watcha.request.deck.deckinfo.DeckInfoCollectionService;
import com.bookie.scrap.watcha.request.user.userbookrating.UserBookRatingCollectionService;
import com.bookie.scrap.watcha.request.user.userbookrating.WatchaUserBookRatingParam;
import com.bookie.scrap.watcha.request.user.userinfo.UserInfoCollectionService;
import com.bookie.scrap.watcha.request.user.userlikepeople.UserLikePeopleCollectionService;
import com.bookie.scrap.watcha.request.user.userlikepeople.WatchaUserLikePeopleParam;
import com.bookie.scrap.watcha.request.user.userwishbook.UserWishBookCollectionService;
import com.bookie.scrap.watcha.request.user.userwishbook.WatchaUserWishBookParam;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ScraperJob implements Job {

    private final UserBookRatingCollectionService userBookRatingCollectionService;
    private final UserInfoCollectionService userInfoCollectionService;
    private final UserLikePeopleCollectionService userLikePeopleCollectionService;
    private final UserWishBookCollectionService userWishBookCollectionService;

    private final BooListCollectionService booListCollectionService;
    private final DeckInfoCollectionService deckInfoCollectionService;

    private final BookCommentCollectionService bookCommentCollectionService;
    private final BookMetaCollectionService bookMetaCollectionService;
    private final BookToDecksCollectionService bookToDecksCollectionService;

    private final RedisStringListService pendingBookRedisService;
    private final RedisStringListService pendingUserRedisService;
    private final RedisStringListService pendingDeckRedisService;

    private final RedisHashService successBookCodeRedisService;
    private final RedisHashService successDeckCodeRedisService;
    private final RedisHashService successUserCodeRedisService;
    private final RedisHashService failedBookCodeRedisService;
    private final RedisHashService failedDeckCodeRedisService;
    private final RedisHashService failedUserCodeRedisService;

    public ScraperJob(
            UserBookRatingCollectionService userBookRatingCollectionService,
            UserInfoCollectionService userInfoCollectionService,
            UserLikePeopleCollectionService userLikePeopleCollectionService,
            UserWishBookCollectionService userWishBookCollectionService,
            BooListCollectionService booListCollectionService,
            DeckInfoCollectionService deckInfoCollectionService,
            BookCommentCollectionService bookCommentCollectionService,
            BookMetaCollectionService bookMetaCollectionService,
            BookToDecksCollectionService bookToDecksCollectionService,
            @Qualifier("pendingBookCode") RedisStringListService pendingBookRedisService,
            @Qualifier("pendingUserCode") RedisStringListService pendingUserRedisService,
            @Qualifier("pendingDeckCode") RedisStringListService pendingDeckRedisService,
            @Qualifier("successBookCode") RedisHashService successBookCodeRedisService,
            @Qualifier("successDeckCode") RedisHashService successDeckCodeRedisService,
            @Qualifier("successUserCode") RedisHashService successUserCodeRedisService,
            @Qualifier("failedBookCode") RedisHashService failedBookCodeRedisService,
            @Qualifier("failedDeckCode") RedisHashService failedDeckCodeRedisService,
            @Qualifier("failedUserCode") RedisHashService failedUserCodeRedisService
    ) {
        this.userBookRatingCollectionService = userBookRatingCollectionService;
        this.userInfoCollectionService = userInfoCollectionService;
        this.userLikePeopleCollectionService = userLikePeopleCollectionService;
        this.userWishBookCollectionService = userWishBookCollectionService;
        this.booListCollectionService = booListCollectionService;
        this.deckInfoCollectionService = deckInfoCollectionService;
        this.bookCommentCollectionService = bookCommentCollectionService;
        this.bookMetaCollectionService = bookMetaCollectionService;
        this.bookToDecksCollectionService = bookToDecksCollectionService;
        this.pendingBookRedisService = pendingBookRedisService;
        this.pendingUserRedisService = pendingUserRedisService;
        this.pendingDeckRedisService = pendingDeckRedisService;
        this.successBookCodeRedisService = successBookCodeRedisService;
        this.successDeckCodeRedisService = successDeckCodeRedisService;
        this.successUserCodeRedisService = successUserCodeRedisService;
        this.failedBookCodeRedisService = failedBookCodeRedisService;
        this.failedDeckCodeRedisService = failedDeckCodeRedisService;
        this.failedUserCodeRedisService = failedUserCodeRedisService;
    }


    @Override
    public void execute(JobExecutionContext context) {

        if (pendingBookRedisService.size() == 0) {
            pendingBookRedisService.add("byLKj8M");
        }
        scrapRoutine();
    }

    private void scrapRoutine() {
        while (pendingBookRedisService.size() > 0) {
            String bookCode = pendingBookRedisService.pop();
            bookJob(bookCode);

            while (pendingDeckRedisService.size() > 0) {
                deckJob(pendingDeckRedisService.pop());
            }

            while (pendingUserRedisService.size() > 0) {
                userJob(pendingUserRedisService.pop());
            }
        }
    }

    private void bookJob(String bookCode) {
        try {
            if (successBookCodeRedisService.exist(bookCode)) {
                log.info("bookJob: {} already exist", bookCode);
                return;
            }

            log.info("bookJob: {} start", bookCode);
            WatchaPageInfo param = new WatchaPageInfo(null);
            bookMetaCollectionService.collect(bookCode, param);

            WatchaBookCommentParam commentParam = new WatchaBookCommentParam(1, 10);
            commentParam.setPopularOrder();
            int commentCnt = -1;
            while (commentCnt != 0) {
                commentCnt = bookCommentCollectionService.collect(bookCode, commentParam);
                commentParam.nextPage();
            }

            BookToDecksParam bookToDecksParam = new BookToDecksParam(1, 10);
            int bookToDecksCnt = -1;
            while (bookToDecksCnt != 0) {
                bookToDecksCnt = bookToDecksCollectionService.collect(bookCode, bookToDecksParam);
                bookToDecksParam.nextPage();
            }

            successBookCodeRedisService.add(new RedisProcessResult(bookCode));
            log.info("bookJob: {}, success", bookCode);
        } catch (RetriableCollectionEx e) {
            pendingBookRedisService.add(bookCode);
            log.warn("bookJob: {}, 재시도 대상 예외 발생: {}", bookCode, e.getMessage());
//            retryBookCodeRedisService.add(new RedisProcessResult(bookCode));
        } catch (CollectionEx e) {
            failedBookCodeRedisService.add(new RedisProcessResult(bookCode));
            log.error("bookJob: {}, error: {}", bookCode, e.getMessage());
        }
    }

    private void deckJob(String deckCode) {
        try {
            if (successDeckCodeRedisService.exist(deckCode)) {
                log.info("deckJob: {} already exist",deckCode);
                return;
            }

            deckInfoCollectionService.collect(deckCode, new WatchaPageInfo(null));

            WatchaBookListParam deckParam = new WatchaBookListParam(1, 10);
            int booksCnt = -1;
            while (booksCnt != 0) {
                booksCnt = booListCollectionService.collect(deckCode, deckParam);
                deckParam.nextPage();
            }

            successDeckCodeRedisService.add(new RedisProcessResult(deckCode));
            log.info("deckJob: {}, success", deckCode);
        } catch (RetriableCollectionEx e) {
            pendingDeckRedisService.add(deckCode);
            log.warn("deckJob: {}, 재시도 대상 예외 발생: {}", deckCode, e.getMessage());
        } catch (CollectionEx e) {
            failedDeckCodeRedisService.add(new RedisProcessResult(deckCode));
            log.error("deckJob: {}, error: {}",deckCode, e.getMessage());
        }
    }

    private void userJob(String userCode) {
        try {
            if (successUserCodeRedisService.exist(userCode)) {
                log.info("userJob: {} already exist",userCode);
                return;
            }
            userInfoCollectionService.collect(userCode, new WatchaPageInfo(null));

            WatchaUserBookRatingParam bookRatingParam = new WatchaUserBookRatingParam(1, 10);
            int bookRatingCnt = -1;
            while (bookRatingCnt != 0) {
                bookRatingCnt = userBookRatingCollectionService.collect(userCode, bookRatingParam);
                bookRatingParam.nextPage();
            }

            WatchaUserLikePeopleParam userLikePeopleParam = new WatchaUserLikePeopleParam(1, 10);
            int userLikePeopleCnt = -1;
            while (userLikePeopleCnt != 0) {
                userLikePeopleCnt = userLikePeopleCollectionService.collect(userCode, userLikePeopleParam);
                userLikePeopleParam.nextPage();
            }

            WatchaUserWishBookParam userWishParam = new WatchaUserWishBookParam(1, 10);
            userWishParam.setSortDirection();
            userWishParam.setSortOption();

            userWishBookCollectionService.collect(userCode, userWishParam);
            int userWishBookCnt = -1;
            while (userWishBookCnt != 0) {
                userWishBookCnt = userWishBookCollectionService.collect(userCode, userWishParam);
                userWishParam.nextPage();
            }

            successUserCodeRedisService.add(new RedisProcessResult(userCode));
            log.info("userJob: {}, success", userCode);
        } catch (RetriableCollectionEx e) {
            pendingUserRedisService.add(userCode);
            log.warn("userJob: {}, 재시도 대상 예외 발생: {}", userCode, e.getMessage());
        } catch (CollectionEx e) {
            failedUserCodeRedisService.add(new RedisProcessResult(userCode));
            log.error("userJob: {}, error: {}",userCode, e.getMessage());
        }
    }
}

