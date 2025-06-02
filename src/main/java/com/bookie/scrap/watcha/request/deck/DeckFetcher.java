package com.bookie.scrap.watcha.request.deck;

import com.bookie.scrap.common.domain.PageInfo;
import com.bookie.scrap.common.domain.http.SpringRequest;
import com.bookie.scrap.common.domain.http.SpringResponse;
import com.bookie.scrap.common.domain.http.WebClientExecutor;
import com.bookie.scrap.watcha.domain.WatchaFetcherFactory;
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
public class DeckFetcher implements WatchaFetcherFactory<DeckResponseDto> {

    private final WebClientExecutor executor;
    private final ObjectMapper mapper;


    @Getter private final HttpMethod HTTP_METHOD = HttpMethod.GET;
    @Getter private final String HTTP_URL_PATTERN = "https://pedia.watcha.com/api/decks/%s/items";

    @Override
    public DeckResponseDto fetch(String deckCode, PageInfo param) throws JsonProcessingException {

        String endpoint = getEndpoint(deckCode, param);

        SpringRequest springRequest = createSpringRequest(endpoint);
        SpringResponse<String> springResponse = createSpringResponse();

        String rawJson = executor.execute(springRequest, springResponse);

        return mapper.readValue(rawJson, DeckResponseDto.class);
    }

    public String getEndpoint(String deckCode, PageInfo param) {
        return param.buildUrlWithParamInfo(String.format(HTTP_URL_PATTERN, deckCode));
    }

}

