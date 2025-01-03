package com.bookie.scrap.config;

import com.bookie.scrap.http.HttpClientProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.AbstractHttpEntity;
import com.bookie.scrap.http.HttpMethod;
import com.bookie.scrap.http.HttpResponseWrapper;

import java.io.IOException;
import java.util.function.Function;


public abstract class BaseRequest<T> {

    @Getter @Setter private String implClassName;

    protected abstract String getHTTP_PROTOCOL();
    protected abstract String getHTTP_HOST();
    protected abstract String getHTTP_ENDPOINT();
    protected abstract int getHTTP_PORT();
    protected abstract HttpMethod getHTTP_METHOD();

    @Getter private HttpHost httpHost;
    @Getter private ClassicHttpRequest httpMethod;
    @Getter private AbstractHttpEntity entity;
    @Getter private HttpClientContext clientContext;
    @Getter private HttpClientResponseHandler<T> responseHandler;

    // TODO: ObjectMapper는 Thread Safe -> 찾아보기
    static final ObjectMapper objectMapper = new ObjectMapper();

    protected void initHttpHost() {
        httpHost = new HttpHost(getHTTP_PROTOCOL(), getHTTP_HOST(), getHTTP_PORT());
    }


    protected void initCookieContext(CookieStore cookieStore) {

        this.clientContext = HttpClientContext.create();
        this.clientContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);

        /*
        // 쿠키 저장소 설정
        if (cookieStore != null) {
            context.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
        }

        // 인증 정보 설정
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
                new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT), // 모든 호스트 및 포트
                new UsernamePasswordCredentials(username, password)
        );
        context.setCredentialsProvider(credentialsProvider);

        // 인증 캐시 설정
        AuthCache authCache = new BasicAuthCache();
        HttpHost targetHost = new HttpHost(getHTTP_HOST(), getHTTP_PORT(), getHTTP_PROTOCOL());
        authCache.put(targetHost, new BasicScheme()); // 기본 인증(Basic Authentication) 설정
        context.setAuthCache(authCache);
        */
    }

    /* ===================================== HttpMethod 관련 메서드 =====================================*/

    private ClassicHttpRequest createHttpMethod() {
        try {
            Class<?> methodClass = Class.forName(getHTTP_METHOD().getClassName());
            return (ClassicHttpRequest)
                    methodClass.getConstructor(String.class)
                            .newInstance(getHTTP_ENDPOINT());
        } catch (Exception e) {
            String exceptionMsg = String.format("[%s] Failed to create HTTP method: ", getImplClassName());
            throw new IllegalStateException(exceptionMsg + e.getMessage(), e);
        }
    }

    protected void initHttpMethod() {
        this.httpMethod = createHttpMethod();
    }

    protected void setHttpMethod(Function<ClassicHttpRequest, ClassicHttpRequest> function) {
        ClassicHttpRequest httpMethod = createHttpMethod();
        this.httpMethod = function.apply(httpMethod);
    }


    /* ===================================== Response Handler 관련 메서드 =====================================*/


    protected void setResponseHandler(Function<HttpResponseWrapper, T> handlerFunction) {
        this.responseHandler = this.createResponseHandler(handlerFunction);
    }

    /**
     * 1. 받은 response를 HttpResponseWrapper로 래핑 (entity 같은 경우 읽은 후 stream이 소모되기에 저장용)
     * 2. 2xx의 or 302(라우팅) Http Code라면 funciton 실행
     * @param handlerFunction
     */
    protected <R> HttpClientResponseHandler<R> createResponseHandler(Function<HttpResponseWrapper, R> handlerFunction) {
        return httpResponse -> {
            HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper(
                    httpResponse.getCode(),
                    httpResponse.getHeaders(),
                    httpResponse.getEntity()
            );

            httpResponseWrapper.printLog();

            int statusCode = httpResponseWrapper.getCode();
            if ((statusCode >= 200 && statusCode < 300) || statusCode == 302 || statusCode == 301) {
                return handlerFunction.apply(httpResponseWrapper);
            } else {
                String exceptionMsg = String.format("[%s] Unexpected status code: ", getImplClassName());
                throw new IllegalStateException(exceptionMsg + statusCode);
            }
        };
    }

    // 예시 핸들러
    @SuppressWarnings("unchecked")
    protected void initStringHandler() {

        Function<HttpResponseWrapper, T> stringFunction
                = responseWrapper -> (T) responseWrapper.getResponseBody();

        setResponseHandler(stringFunction);
    }

}
