package com.bookie.scrap.watcha.domain;


import com.bookie.scrap.common.domain.PageInfo;
import com.bookie.scrap.common.http.SpringRequest;
import com.bookie.scrap.common.http.SpringResponse;
import org.springframework.http.HttpMethod;

public interface WatchaFetcher<T> {

    HttpMethod getHTTP_METHOD();

    default SpringRequest createSpringRequest(String endpoint) {
            return SpringRequest.builder()
                    .method(getHTTP_METHOD())
                    .url(endpoint)
                    .headers(WatchaHeaders.getHeaders())
                    .build();
    }

    default SpringResponse<String> createSpringResponse() {
        return new SpringResponse<>(response -> response.bodyToMono(String.class));
    }

    T fetch(String bookCode, PageInfo param) throws Exception;
}
