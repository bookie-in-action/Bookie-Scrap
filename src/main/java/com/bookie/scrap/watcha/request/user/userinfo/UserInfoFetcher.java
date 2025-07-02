package com.bookie.scrap.watcha.request.user.userinfo;

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
public class UserInfoFetcher implements WatchaFetcher<UserInfoResponseDto> {

    private final WebClientExecutor executor;
    private final ObjectMapper mapper;


    @Getter private final HttpMethod HTTP_METHOD = HttpMethod.GET;
    @Getter private final String HTTP_URL_PATTERN = "https://pedia.watcha.com/api/users/%s";

    @Override
    public UserInfoResponseDto fetch(String userCode, PageInfo param) {

        String endpoint = String.format(HTTP_URL_PATTERN, userCode);

        SpringRequest springRequest = createSpringRequest(endpoint);
        SpringResponse<String> springResponse = createSpringResponse();

        String rawJson = executor.execute(springRequest, springResponse);

        if (rawJson == null || rawJson.isBlank()) {
            log.warn("json 파싱 실패");
            return null;
        }
        try {
            return mapper.readValue(rawJson, UserInfoResponseDto.class);
        } catch (JsonProcessingException e) {
            log.warn("userCode={} userInfo JSON 파싱 실패: {}", userCode, e.getMessage());
            return null;
        }

    }


}

