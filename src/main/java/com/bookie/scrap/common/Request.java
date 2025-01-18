package com.bookie.scrap.common;

import com.bookie.scrap.http.HttpMethod;
import com.bookie.scrap.http.HttpRequestExecutor;
import com.bookie.scrap.http.HttpResponseWrapper;
import lombok.Getter;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;

import java.util.function.Consumer;
import java.util.function.Function;

@Getter
abstract public class Request<T> {

    protected String implClassName;

    protected ClassicHttpRequest mainRequest;

    protected HttpClientContext clientContext;

    protected HttpClientResponseHandler<T> responseHandler;

    abstract public void setImplClassName();

    public void setMainRequest(HttpMethod httpMethod, String endPoint) {
        if (this.mainRequest != null) {
            return;
        }

        try {
            Class<?> methodClass = Class.forName(httpMethod.getClassName());
            this.mainRequest = (ClassicHttpRequest) methodClass.getConstructor(String.class).newInstance(endPoint);
        } catch (Exception e) {
            String exceptionMsg = String.format("[%s] Failed to create HTTP method: ", this.getClass().getSimpleName());
            throw new IllegalStateException(exceptionMsg + e.getMessage(), e);
        }
    }

    public void setClientContext(Consumer<HttpClientContext> consumer) {
        if(this.clientContext == null) {
            this.clientContext = HttpClientContext.create();
        }

        consumer.accept(this.clientContext);
    }

    public void setResponseHandler(Function<HttpResponseWrapper, T> handlerFunction) {
        this.responseHandler = httpResponse ->

        {
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
                String exceptionMsg = String.format("[%s] Unexpected status code: ", this.getClass().getSimpleName());
                throw new IllegalStateException(exceptionMsg + statusCode);
            }
        };
    }

    public T execute() {
        return HttpRequestExecutor.execute(this);
    }

//
//    public static void main(String[] args) {
//        BasicCookieStore basicCookieStore = new BasicCookieStore();
//
//        // 쿠키 생성 및 추가
//        BasicClientCookie cookie = new BasicClientCookie("testCookie", "testValue");
//        cookie.setDomain("example.com");
//        cookie.setPath("/");
//        basicCookieStore.addCookie(cookie);
//
//        // Request 객체 생성
//        Request objectRequest = new Request();
//
//        // HttpClientContext에 CookieStore 설정
//        objectRequest.setClientContext(httpClientContext ->
//                httpClientContext.setAttribute(HttpClientContext.COOKIE_STORE, basicCookieStore));
//
//        // HttpClientContext에서 CookieStore 확인
//        HttpClientContext clientContext = objectRequest.clientContext;
//        CookieStore retrievedCookieStore = (CookieStore) clientContext.getAttribute(HttpClientContext.COOKIE_STORE);
//
//        if (retrievedCookieStore != null) {
//            System.out.println("CookieStore에 저장된 쿠키 목록:");
//            retrievedCookieStore.getCookies().forEach(c ->
//                    System.out.println("Cookie name: " + c.getName() + ", value: " + c.getValue()));
//        } else {
//            System.out.println("CookieStore가 설정되지 않았습니다.");
//        }
//
//        // 인증 정보 설정
//        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
//        credentialsProvider.setCredentials(
//                new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT), // 모든 호스트 및 포트
//                new UsernamePasswordCredentials(username, password)
//        );
//        context.setCredentialsProvider(credentialsProvider);
//
//        // 인증 캐시 설정
//        AuthCache authCache = new BasicAuthCache();
//        HttpHost targetHost = new HttpHost(getHTTP_HOST(), getHTTP_PORT(), getHTTP_PROTOCOL());
//        authCache.put(targetHost, new BasicScheme()); // 기본 인증(Basic Authentication) 설정
//        context.setAuthCache(authCache);
//    }
}
