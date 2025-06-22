package com.bookie.scrap.watcha;

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

    private final RedisStringListService bookRedisService;
    private final RedisStringListService userRedisService;
    private final RedisStringListService deckRedisService;

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
            @Qualifier("bookCodeList") RedisStringListService bookRedisService,
            @Qualifier("userCodeList") RedisStringListService userRedisService,
            @Qualifier("deckCodeList") RedisStringListService deckRedisService
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
        this.bookRedisService = bookRedisService;
        this.userRedisService = userRedisService;
        this.deckRedisService = deckRedisService;
    }


    @Override
    public void execute(JobExecutionContext context) {

        if (bookRedisService.size() == 0) {
            bookRedisService.add("byLKj8M");
        }
        scrapRoutine();
    }

    private void scrapRoutine() {
        try {
            while (bookRedisService.size() > 0) {
                String bookCode = bookRedisService.pop();
                bookJob(bookCode);

                while (deckRedisService.size() > 0) {
                    deckJob(deckRedisService.pop());
                }

                while (userRedisService.size() > 0) {
                    userJob(userRedisService.pop());
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bookJob(String bookCode) throws Exception {

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
    }

    private void deckJob(String deckCode) throws Exception {

        deckInfoCollectionService.collect(deckCode, new WatchaPageInfo(null));

        WatchaBookListParam deckParam = new WatchaBookListParam(1, 10);
        int booksCnt = -1;
        while (booksCnt != 0) {
            booksCnt = booListCollectionService.collect(deckCode, deckParam);
            deckParam.nextPage();
        }
    }

    private void userJob(String userCode) throws Exception {
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
    }
}

