package com.bookie.scrap.watcha.request.book.booktodecks;

import com.bookie.scrap.common.domain.PageInfo;
import com.bookie.scrap.common.http.SpringRequest;
import com.bookie.scrap.common.http.SpringResponse;
import com.bookie.scrap.common.http.WebClientExecutor;
import com.bookie.scrap.watcha.domain.WatchaFetcherFactory;
import com.bookie.scrap.watcha.request.book.bookmeta.BookMetaResponseDto;
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
    public BookToDecksResponseDto fetch(String bookCode, PageInfo param) {

        String endpoint = getEndpoint(bookCode, param);

        SpringRequest springRequest = createSpringRequest(endpoint);
        SpringResponse<String> springResponse = createSpringResponse();

        String rawJson = executor.execute(springRequest, springResponse);

        if (rawJson == null || rawJson.isBlank()) {
            log.warn("bookCode={} toDecks fetch 실패: 응답이 null 또는 빈 문자열", bookCode);
            return null;
        }

        try {
            return mapper.readValue(rawJson, BookToDecksResponseDto.class);
        } catch (JsonProcessingException e) {
            log.warn("bookCode={} toDecks JSON 파싱 실패: {}", bookCode, e.getMessage());
            return null;
        }

    }

    public String getEndpoint(String bookCode, PageInfo param) {
        return param.buildUrlWithParamInfo(String.format(HTTP_URL_PATTERN, bookCode));
    }

}

