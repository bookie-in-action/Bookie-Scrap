package com.bookie.scrap.watcha.request.deck;

import com.bookie.scrap.common.util.JsonUtil;
import com.bookie.scrap.watcha.domain.WatchaPersistFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeckPersister implements WatchaPersistFactory<DeckResponseDto> {

    private final DeckMongoRepository repository;

    @Override
    public int persist(DeckResponseDto dto, String deckCode) throws JsonProcessingException {

        List<JsonNode> books = dto.getResult().getBooks();

        if (books == null) {
            return 0;
        }

        log.debug("size: {}",books.size());

        List<DeckDocument> documents = new ArrayList<>();

        for (int idx = 0; idx < books.size(); idx++) {

            log.debug(
                    "deckCode: {}, book idx: {}, value: {}",
                    deckCode,
                    idx,
                    JsonUtil.toPrettyJson(books.get(idx))
            );
            log.debug("===========================");

            DeckDocument document = new DeckDocument();
            document.setDeckCode(deckCode);
            document.setRawJson(JsonUtil.toMap(books.get(idx)));
            documents.add(document);
        }

        repository.saveAll(documents);

        return books.size();
    }
}
