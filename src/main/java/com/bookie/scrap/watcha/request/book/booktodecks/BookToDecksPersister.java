package com.bookie.scrap.watcha.request.book.booktodecks;

import com.bookie.scrap.common.redis.RedisStringListService;
import com.bookie.scrap.common.util.JsonUtil;
import com.bookie.scrap.watcha.domain.WatchaPersistFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class BookToDecksPersister implements WatchaPersistFactory<BookToDecksResponseDto> {

    private final RedisStringListService deckRedisService;
    private final BookToDecksMongoRepository repository;

    public BookToDecksPersister(
            @Qualifier("pendingDeckCode") RedisStringListService deckRedisService,
            BookToDecksMongoRepository repository
    ) {
        this.deckRedisService = deckRedisService;
        this.repository = repository;
    }

    @Override
    public int persist(BookToDecksResponseDto dto, String bookCode) {

        List<JsonNode> decks = dto.getResult().getDecks();

        if (decks == null || decks.isEmpty()) {
            return 0;
        }

        log.debug("size: {}",decks.size());

        List<BookToDecksDocument> documents = new ArrayList<>();

        int count = 0;
        for (int idx = 0; idx < decks.size(); idx++) {
            try {
                BookToDecksDocument document = new BookToDecksDocument();
                document.setBookCode(bookCode);
                document.setRawJson(JsonUtil.toMap(decks.get(idx)));
                documents.add(document);

                log.debug(
                        "decks idx: {}, value: {}",
                        idx,
                        JsonUtil.toPrettyJson(decks.get(idx))
                );
                log.debug("===========================");

                count++;
            } catch (JsonProcessingException e) {
                log.warn("json 파싱 실패");
            }
        }

        deckRedisService.add(dto.getResult().getDeckCodes());
        repository.saveAll(documents);

        return count;

    }
}
