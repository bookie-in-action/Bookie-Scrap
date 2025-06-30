package com.bookie.scrap.watcha.request.deck.deckinfo;

import com.bookie.scrap.common.redis.RedisStringListService;
import com.bookie.scrap.common.util.JsonUtil;
import com.bookie.scrap.watcha.domain.WatchaPersistFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DeckInfoPersister implements WatchaPersistFactory<DeckInfoResponseDto> {

    private final DeckInfoMongoRepository repository;

    @Override
    public int persist(DeckInfoResponseDto dto, String deckCode) {

        try {
            DeckInfoDocument document = new DeckInfoDocument();
            document.setDeckCode(deckCode);
            document.setRawJson(JsonUtil.toMap(dto.getResult()));
            repository.save(document);

            log.debug(dto.getResult().toString());
            log.debug("===========================");

        } catch (JsonProcessingException e) {
            log.warn("json 파싱 실패");
             return 0;
        }

        return 1;
    }
}
