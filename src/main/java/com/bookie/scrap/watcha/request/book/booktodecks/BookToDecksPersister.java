package com.bookie.scrap.watcha.request.book.booktodecks;

import com.bookie.scrap.common.util.JsonUtil;
import com.bookie.scrap.watcha.domain.WatchaPersistFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookToDecksPersister implements WatchaPersistFactory<BookToDecksResponseDto> {

    private final BookToDecksMongoRepository repository;

    @Override
    public void persist(BookToDecksResponseDto dto, String bookCode) throws JsonProcessingException {

        List<JsonNode> decks = dto.getResult().getDecks();

        if (decks == null) {
            return;
        }

        log.debug("size: {}",decks.size());

        for (int idx = 0; idx < decks.size(); idx++) {

            log.debug(
                    "decks idx: {}, value: {}",
                    idx,
                    JsonUtil.toPrettyJson(decks.get(idx))
            );
            log.debug("===========================");

            BookToDecksDocument document = new BookToDecksDocument();
            document.setBookCode(bookCode);
            document.setRawJson(JsonUtil.toMap(decks.get(idx)));
            repository.save(document);
        }

    }
}
