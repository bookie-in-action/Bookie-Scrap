package com.bookie.scrap.watcha.request.user.userbookrating;

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
public class UserBookRatingFetcher implements WatchaFetcherFactory<UserBookRatingResponseDto> {

    private final WebClientExecutor executor;
    private final ObjectMapper mapper;


    @Getter private final HttpMethod HTTP_METHOD = HttpMethod.GET;
    @Getter private final String HTTP_URL_PATTERN = "https://pedia.watcha.com/api/users/%s/contents/books/ratings";

    @Override
    public UserBookRatingResponseDto fetch(String userCode, PageInfo param) throws JsonProcessingException {

        String endpoint = getEndpoint(userCode, param);

        SpringRequest springRequest = createSpringRequest(endpoint);
        SpringResponse<String> springResponse = createSpringResponse();

        String rawJson = executor.execute(springRequest, springResponse);

        log.debug(rawJson);
        return mapper.readValue(rawJson, UserBookRatingResponseDto.class);
    }

    public String getEndpoint(String bookCode, PageInfo param) {
        return param.buildUrlWithParamInfo(String.format(HTTP_URL_PATTERN, bookCode));
    }
}

