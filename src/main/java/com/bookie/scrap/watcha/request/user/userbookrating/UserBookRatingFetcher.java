package com.bookie.scrap.watcha.request.user.userbookrating;

import com.bookie.scrap.common.domain.PageInfo;
import com.bookie.scrap.common.http.SpringRequest;
import com.bookie.scrap.common.http.SpringResponse;
import com.bookie.scrap.common.http.WebClientExecutor;
import com.bookie.scrap.watcha.domain.WatchaFetcher;
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
public class UserBookRatingFetcher implements WatchaFetcher<UserBookRatingResponseDto> {

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

        if (rawJson == null || rawJson.isBlank()) {
            log.warn("json 파싱 실패");
            return null;
        }

        try {
            return mapper.readValue(rawJson, UserBookRatingResponseDto.class);
        } catch (JsonProcessingException e) {
            log.warn("userCode={} userBookRating JSON 파싱 실패: {}", userCode, e.getMessage());
            return null;
        }
    }

    public String getEndpoint(String bookCode, PageInfo param) {
        return param.buildUrlWithParamInfo(String.format(HTTP_URL_PATTERN, bookCode));
    }
}

