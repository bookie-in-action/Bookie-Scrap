package com.bookie.scrap.common.domain.http;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;

@Getter
@Builder
public class SpringRequest {
    private HttpMethod method;
    private String url;
    private HttpHeaders headers;
    private MultiValueMap<String, String> cookies;
    private Object body;
}