package com.bookie.scrap.watcha;

import com.bookie.scrap.watcha.domain.WatchaPageInfo;
import com.bookie.scrap.watcha.request.book.bookcomment.BookCommentCollectionService;
import com.bookie.scrap.watcha.request.book.bookcomment.WatchaBookCommentParam;
import com.bookie.scrap.watcha.request.book.bookmeta.BookMetaCollectionService;
import com.bookie.scrap.watcha.request.book.bookmeta.WatchaBookMetaParam;
import com.bookie.scrap.watcha.request.book.booktodecks.BookToDecksCollectionService;
import com.bookie.scrap.watcha.request.book.booktodecks.BookToDecksParam;
import com.bookie.scrap.watcha.request.deck.DeckCollectionService;
import com.bookie.scrap.watcha.request.deck.WatchaDeckParam;
import com.bookie.scrap.watcha.request.user.userbookrating.UserBookRatingCollectionService;
import com.bookie.scrap.watcha.request.user.userbookrating.WatchaUserBookRatingParam;
import com.bookie.scrap.watcha.request.user.userinfo.UserInfoCollectionService;
import com.bookie.scrap.watcha.request.user.userlikepeople.UserLikePeopleCollectionService;
import com.bookie.scrap.watcha.request.user.userlikepeople.WatchaUserLikePeopleParam;
import com.bookie.scrap.watcha.request.user.userwishbook.UserWishBookCollectionService;
import com.bookie.scrap.watcha.request.user.userwishbook.WatchaUserWishBookParam;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScraperJob implements Job {

    private final UserBookRatingCollectionService userBookRatingCollectionService;
    private final UserInfoCollectionService userInfoCollectionService;
    private final UserLikePeopleCollectionService userLikePeopleCollectionService;
    private final UserWishBookCollectionService userWishBookCollectionService;

    private final DeckCollectionService deckCollectionService;

    private final BookCommentCollectionService bookCommentCollectionService;
    private final BookMetaCollectionService bookMetaCollectionService;
    private final BookToDecksCollectionService bookToDecksCollectionService;

    @Override
    public void execute(JobExecutionContext context) {

//        userJob();
//
//        deckJob();
//
//        bookJob();

    }

    private void bookJob() throws Exception {
        WatchaBookCommentParam requestParam = new WatchaBookCommentParam(1, 10);
        requestParam.setPopularOrder();
        bookCommentCollectionService.collect("byLKj8M", requestParam);

        WatchaBookMetaParam param = new WatchaBookMetaParam(1, 10);
        bookMetaCollectionService.collect("byLKj8M", param);

        BookToDecksParam bookToDecksParam = new BookToDecksParam(1, 10);
        bookToDecksCollectionService.collect("byLKj8M", bookToDecksParam);
    }

    private void deckJob() throws Exception {
        WatchaDeckParam param = new WatchaDeckParam(1, 10);
        deckCollectionService.collect("gcdkyKnXjN", param);
    }

    private void userJob() throws Exception {
        WatchaUserBookRatingParam param = new WatchaUserBookRatingParam(1, 10);
        userBookRatingCollectionService.collect("2mwvggAE2vMa7", param);

        userInfoCollectionService.collect("ZWpqMekrDqrkn", new WatchaPageInfo(null));


        WatchaUserLikePeopleParam requestParam = new WatchaUserLikePeopleParam(1, 10);
        userLikePeopleCollectionService.collect("2mwvggAE2vMa7", requestParam);


        WatchaUserWishBookParam userWishParam = new WatchaUserWishBookParam(1, 10);
        userWishParam.setSortDirection();
        userWishParam.setSortOption();

        userWishBookCollectionService.collect("2mwvggAE2vMa7", userWishParam);
    }
}

