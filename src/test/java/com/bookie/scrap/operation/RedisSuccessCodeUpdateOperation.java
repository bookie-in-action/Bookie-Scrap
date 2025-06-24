package com.bookie.scrap.operation;

import com.bookie.scrap.common.redis.RedisHashService;
import com.bookie.scrap.common.redis.RedisProcessResult;
import com.bookie.scrap.common.scheduler.SchedulerStubConfig;
import com.bookie.scrap.watcha.request.book.bookmeta.BookMetaDocument;
import com.bookie.scrap.watcha.request.book.bookmeta.BookMetaMongoRepository;
import com.bookie.scrap.watcha.request.deck.deckinfo.DeckInfoDocument;
import com.bookie.scrap.watcha.request.deck.deckinfo.DeckInfoMongoRepository;
import com.bookie.scrap.watcha.request.user.userinfo.UserInfoDocument;
import com.bookie.scrap.watcha.request.user.userinfo.UserInfoMongoRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.List;

@Slf4j
@Tag("manual")
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
    @Qualifier("successBookCode")
    private RedisHashService successBookCodeRedisService;

    @Autowired
    @Qualifier("successDeckCode")
    private RedisHashService successDeckCodeRedisService;

    @Autowired
    @Qualifier("successUserCode")
    private RedisHashService successUserCodeRedisService;

    @Test
    void bookCodeUpdate() {
        List<String> successBookCodes = bookMetaMongoRepository.findAll().stream()
                .map(BookMetaDocument::getBookCode)
                .toList();

        successBookCodes.forEach(bookCode -> successBookCodeRedisService.add(new RedisProcessResult(bookCode)));
    }

    @Test
    void deckCodeUpdate() {
        List<String> successDeckCodes = deckInfoMongoRepository.findAll().stream()
                .map(DeckInfoDocument::getDeckCode)
                .toList();

        successDeckCodes.forEach(deckCode -> successDeckCodeRedisService.add(new RedisProcessResult(deckCode)));

    }

    @Test
    void userCodeUpdate() {
        List<String> successUserCodes = userInfoMongoRepository.findAll().stream()
                .map(UserInfoDocument::getUserCode)
                .toList();

        successUserCodes.forEach(userCode -> successUserCodeRedisService.add(new RedisProcessResult(userCode)));

    }


}
