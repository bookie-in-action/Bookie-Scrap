package com.bookie.scrap.watcha.request.deck.deckinfo;

import com.bookie.scrap.common.domain.redis.RedisStringListService;
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
public class DeckInfoPersister implements WatchaPersistFactory<DeckInfoResponseDto> {

    private final RedisStringListService userRedisService;

    private final DeckInfoMongoRepository repository;

    public DeckInfoPersister(
            @Qualifier("userCodeList") RedisStringListService userRedisService,
            DeckInfoMongoRepository repository
    ) {
        this.userRedisService = userRedisService;
        this.repository = repository;
    }

    @Override
    public int persist(DeckInfoResponseDto dto, String deckCode) throws JsonProcessingException {

        DeckInfoDocument document = new DeckInfoDocument();
        document.setDeckCode(deckCode);
        document.setRawJson(JsonUtil.toMap(dto.getResult()));

        repository.save(document);
        userRedisService.add(dto.getResult().getUserCode());

        return 1;
    }
}
