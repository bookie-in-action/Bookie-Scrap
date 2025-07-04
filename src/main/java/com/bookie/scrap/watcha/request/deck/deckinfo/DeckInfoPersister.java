package com.bookie.scrap.watcha.request.deck.deckinfo;

import com.bookie.scrap.common.redis.RedisStringListService;
import com.bookie.scrap.common.util.JsonUtil;
import com.bookie.scrap.watcha.domain.WatchaPersistor;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DeckInfoPersister implements WatchaPersistor<DeckInfoResponseDto> {

    private final DeckInfoMongoRepository repository;

    @Override
    public int persist(DeckInfoResponseDto dto, String deckCode) {

        try {
            log.info("DeckMeta size: 1");

            DeckInfoDocument document = new DeckInfoDocument();
            document.setDeckCode(deckCode);
            document.setRawJson(JsonUtil.toMap(dto.getResult()));
            repository.save(document);


            log.info("deckCode: {} meta saved", deckCode);
            log.debug(dto.getResult().toString());

        } catch (JsonProcessingException e) {
            log.error("json 파싱 실패");
             return 0;
        }

        return 1;
    }
}
