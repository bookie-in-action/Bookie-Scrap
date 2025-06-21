package com.bookie.scrap.watcha.request.book.booktodecks;

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
public class BookToDecksFetcher implements WatchaFetcherFactory<BookToDecksResponseDto> {

    private final WebClientExecutor executor;
    private final ObjectMapper mapper;


    @Getter private final HttpMethod HTTP_METHOD = HttpMethod.GET;
    @Getter private final String HTTP_URL_PATTERN = "https://pedia.watcha.com/api/contents/%s/decks";

    @Override
    public BookToDecksResponseDto fetch(String bookCode, PageInfo param) throws JsonProcessingException {

        String endpoint = getEndpoint(bookCode, param);

        SpringRequest springRequest = createSpringRequest(endpoint);
        SpringResponse<String> springResponse = createSpringResponse();

        String rawJson = executor.execute(springRequest, springResponse);

        return mapper.readValue(rawJson, BookToDecksResponseDto.class);

    }

    public String getEndpoint(String bookCode, PageInfo param) {
        return param.buildUrlWithParamInfo(String.format(HTTP_URL_PATTERN, bookCode));
    }

}

