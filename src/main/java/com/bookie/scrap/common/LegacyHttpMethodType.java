package com.bookie.scrap.common;

import lombok.Getter;
import org.springframework.http.HttpMethod;

@Getter
public enum LegacyHttpMethodType {
    GET("HttpGet"), POST("HttpPost");

    private String simpleName;
    private String className;

    private LegacyHttpMethodType(String simpleName) {
        String pkg = "org.apache.hc.client5.http.classic.methods.";

        this.simpleName = simpleName;
        this.className = pkg + simpleName;
    }

    public HttpMethod toSpringHttpMethod() {
        try {
            return HttpMethod.valueOf(this.name());
        } catch (IllegalArgumentException e) {
            throw new UnsupportedOperationException("지원하지 않는 HTTP 메서드: " + this.name());
        }
    }

}

    
