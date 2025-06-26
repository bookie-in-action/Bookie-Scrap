package com.bookie.scrap.watcha.request.deck.deckinfo;

import com.bookie.scrap.common.domain.PageInfo;
import com.bookie.scrap.common.http.SpringRequest;
import com.bookie.scrap.common.http.SpringResponse;
import com.bookie.scrap.common.http.WebClientExecutor;
import com.bookie.scrap.common.util.JsonUtil;
import com.bookie.scrap.watcha.domain.WatchaFetcherFactory;
import com.bookie.scrap.watcha.request.deck.booklist.BookListResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeckInfoFetcher implements WatchaFetcherFactory<DeckInfoResponseDto> {

    private final WebClientExecutor executor;
    private final ObjectMapper mapper;


    @Getter private final HttpMethod HTTP_METHOD = HttpMethod.GET;
    @Getter private final String HTTP_URL_PATTERN = "https://pedia.watcha.com/api/decks/%s";

    @Override
    public DeckInfoResponseDto fetch(String deckCode, PageInfo param) {

        String endpoint = getEndpoint(deckCode, param);

        SpringRequest springRequest = createSpringRequest(endpoint);
        SpringResponse<String> springResponse = createSpringResponse();

        String rawJson = executor.execute(springRequest, springResponse);
//        log.debug(JsonUtil.toPrettyJson(rawJson));

        if (rawJson == null || rawJson.isBlank()) {
            log.warn("json 파싱 실패");
            return null;
        }

        try {
            return mapper.readValue(rawJson, DeckInfoResponseDto.class);
        } catch (JsonProcessingException e) {
            log.warn("deckCode={} deckInfo JSON 파싱 실패: {}", deckCode, e.getMessage());
            return null;
        }
    }

    public String getEndpoint(String deckCode, PageInfo param) {
        return param.buildUrlWithParamInfo(String.format(HTTP_URL_PATTERN, deckCode));
    }

}

