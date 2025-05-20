package com.bookie.scrap.common;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;

import java.util.Map;
import java.util.function.Function;

@Getter
@Builder
public class SpringRequest {
    private HttpMethod method;
    private String url;
    private HttpHeaders headers;
    private MultiValueMap<String, String> cookies;
    private Object body;
}