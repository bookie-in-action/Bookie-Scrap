package com.bookie.scrap.common.http;

import lombok.Getter;

@Getter
public enum HttpMethod {
    GET("HttpGet"), POST("HttpPost");

    private String simpleName;
    private String className;

    private HttpMethod(String simpleName) {
        String pkg = "org.apache.hc.client5.http.classic.methods.";

        this.simpleName = simpleName;
        this.className = pkg + simpleName;
    }
}

class HttpMethodTest {
    public static void main(String[] args) {
        System.out.println(HttpMethod.GET.getSimpleName());
        System.out.println(HttpMethod.POST.getClassName());
    }
}
    
