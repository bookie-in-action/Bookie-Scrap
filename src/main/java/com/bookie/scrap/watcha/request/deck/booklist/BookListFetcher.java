package com.bookie.scrap.watcha.request.deck.booklist;

import com.bookie.scrap.common.domain.PageInfo;
import com.bookie.scrap.common.http.SpringRequest;
import com.bookie.scrap.common.http.SpringResponse;
import com.bookie.scrap.common.http.WebClientExecutor;
import com.bookie.scrap.common.util.JsonUtil;
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
public class BookListFetcher implements WatchaFetcherFactory<BookListResponseDto> {

    private final WebClientExecutor executor;
    private final ObjectMapper mapper;


    @Getter private final HttpMethod HTTP_METHOD = HttpMethod.GET;
    @Getter private final String HTTP_URL_PATTERN = "https://pedia.watcha.com/api/decks/%s/items";

    @Override
    public BookListResponseDto fetch(String deckCode, PageInfo param) throws JsonProcessingException {

        String endpoint = getEndpoint(deckCode, param);

        SpringRequest springRequest = createSpringRequest(endpoint);
        SpringResponse<String> springResponse = createSpringResponse();

        String rawJson = executor.execute(springRequest, springResponse);
        log.debug(JsonUtil.toPrettyJson(rawJson));

        return mapper.readValue(rawJson, BookListResponseDto.class);
    }

    public String getEndpoint(String deckCode, PageInfo param) {
        return param.buildUrlWithParamInfo(String.format(HTTP_URL_PATTERN, deckCode));
    }

}

