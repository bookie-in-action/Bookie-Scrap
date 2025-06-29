package com.bookie.scrap.watcha.request.book.booktodecks;

import com.bookie.scrap.common.redis.RedisStringListService;
import com.bookie.scrap.common.util.JsonUtil;
import com.bookie.scrap.watcha.domain.WatchaPersistFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BookToDecksPersister implements WatchaPersistFactory<BookToDecksResponseDto> {

    private final BookToDecksMongoRepository repository;

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

        repository.saveAll(documents);

        return count;

    }
}
