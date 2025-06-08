package com.bookie.scrap.common.springconfig;

import com.bookie.scrap.watcha.request.book.bookcomment.BookCommentMongoRepository;
import com.bookie.scrap.watcha.request.book.bookmeta.BookMetaMongoRepository;
import com.bookie.scrap.watcha.request.book.booktodecks.BookToDecksMongoRepository;
import com.bookie.scrap.watcha.request.deck.DeckMongoRepository;
import com.bookie.scrap.watcha.request.user.userbookrating.UserBookRatingMongoRepository;
import com.bookie.scrap.watcha.request.user.userinfo.UserInfoMongoRepository;
import com.bookie.scrap.watcha.request.user.userlikepeople.UserLikePeopleMongoRepository;
import com.bookie.scrap.watcha.request.user.userwishbook.UserWishBookMongoRepository;
import com.bookie.scrap.watcha.request.user.withlogin_userlikedeck.UserLikeDeckMongoRepository;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(
        basePackages = "com.bookie.scrap", // MongoDB Repository가 포함된 상위 패키지
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {
                        BookCommentMongoRepository.class,
                        BookMetaMongoRepository.class,
                        BookToDecksMongoRepository.class,
                        DeckMongoRepository.class,
                        UserBookRatingMongoRepository.class,
                        UserInfoMongoRepository.class,
                        UserLikePeopleMongoRepository.class,
                        UserWishBookMongoRepository.class,
                        UserLikeDeckMongoRepository.class
                }
        )
)
public class MongoConfig {
}