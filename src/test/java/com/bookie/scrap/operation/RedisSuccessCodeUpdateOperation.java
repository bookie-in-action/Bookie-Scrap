package com.bookie.scrap.operation;

import com.bookie.scrap.common.redis.RedisStringListService;
import com.bookie.scrap.common.scheduler.SchedulerStubConfig;
import com.bookie.scrap.watcha.request.book.bookmeta.BookMetaDocument;
import com.bookie.scrap.watcha.request.book.bookmeta.BookMetaMongoRepository;
import com.bookie.scrap.watcha.request.deck.deckinfo.DeckInfoDocument;
import com.bookie.scrap.watcha.request.deck.deckinfo.DeckInfoMongoRepository;
import com.bookie.scrap.watcha.request.user.userinfo.UserInfoDocument;
import com.bookie.scrap.watcha.request.user.userinfo.UserInfoMongoRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.List;

@Slf4j
@SpringBootTest
@Import(SchedulerStubConfig.class)
public class RedisSuccessCodeUpdateOperation {

    @Autowired
    private BookMetaMongoRepository bookMetaMongoRepository;
    @Autowired
    private DeckInfoMongoRepository deckInfoMongoRepository;
    @Autowired
    private UserInfoMongoRepository userInfoMongoRepository;

    @Autowired
    @Qualifier("successBookCodeList")
    private RedisStringListService successBookCodeRedisService;

    @Autowired
    @Qualifier("successDeckCodeList")
    private RedisStringListService successDeckCodeRedisService;
    @Autowired

    @Qualifier("successUserCodeList")
    private RedisStringListService successUserCodeRedisService;

    @Test
    void bookCodeUpdate() {
        List<String> successBookCodes = bookMetaMongoRepository.findAll().stream()
                .map(BookMetaDocument::getBookCode)
                .toList();

        successBookCodes.forEach(successBookCodeRedisService::add);
    }

    @Test
    void deckCodeUpdate() {
        List<String> successDeckCodes = deckInfoMongoRepository.findAll().stream()
                .map(DeckInfoDocument::getDeckCode)
                .toList();

        successDeckCodes.forEach(successDeckCodeRedisService::add);
    }

    @Test
    void userCodeUpdate() {
        List<String> successUserCodes = userInfoMongoRepository.findAll().stream()
                .map(UserInfoDocument::getUserCode)
                .toList();

        successUserCodes.forEach(successUserCodeRedisService::add);
    }


}
