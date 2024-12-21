package com.bookie.scrap.config;

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

import static com.bookie.scrap.util.CookieManager.*;


public abstract class BaseRequestConfig<T> {

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

//    TODO: ObjectMapper는 Thread Safe -> 찾아보기
    static final ObjectMapper objectMapper = new ObjectMapper();

    protected void createHttpHost() {
        httpHost = new HttpHost(getHTTP_PROTOCOL(), getHTTP_HOST(), getHTTP_PORT());
    }

    protected void initCookieContext(CookieStore cookieStore) {

        HttpClientContext httpClientContext = new HttpClientContext();
        httpClientContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);

        this.clientContext = httpClientContext;
    }

    /* ===================================== HttpMethod 관련 메서드 =====================================*/


    /**
     * 선언해놓은 HTTP_ENDPOINT만으로 ClassicHttpRequest 만드는 메서드
     */
    private void createHttpMethod() {
        try {
            Class<?> methodClass = Class.forName(getHTTP_METHOD().getClassName());
            httpMethod = (ClassicHttpRequest)
                    methodClass.getConstructor(String.class)
                    .newInstance(getHTTP_ENDPOINT());
        } catch (Exception e) {
            String exceptionMsg = String.format("[%s] Failed to create HTTP method: ", getImplClassName());
            throw new IllegalStateException(exceptionMsg + e.getMessage(), e);
        }
    }

    /**
     * 1. 선언한 HTTP_ENDPOINT로 ClassicHttpRequest 생성
     * 2. 생성된 HttpRequest에 function으로 추가 설정
     * @param function
     */
    private void createHttpMethod(Function<ClassicHttpRequest, ClassicHttpRequest> function) {
        try {
            Class<?> methodClass = Class.forName(getHTTP_METHOD().getClassName());
            ClassicHttpRequest httpMethod =
                    (ClassicHttpRequest) methodClass
                            .getConstructor(String.class)
                            .newInstance(getHTTP_ENDPOINT());
            this.httpMethod = function.apply(httpMethod);
        } catch (Exception e) {
            String exceptionMsg = String.format("[%s] Failed to create HTTP method: ", getImplClassName());
            throw new IllegalStateException(exceptionMsg + e.getMessage(), e);
        }
    }

    /**
     * 1. 선언한 HTTP_ENDPOINT+endpoint로 ClassicHttpRequest 생성
     * 2. 생성된 HttpRequest에 function으로 추가 설정
     *
     * @param function
     * @param endpoint - HTTP_ENDPOINT 뒤에 와야 하는 endpoint
     */
    private void createHttpMethod(Function<ClassicHttpRequest, ClassicHttpRequest> function, String endpoint) {
        try {
            Class<?> methodClass = Class.forName(getHTTP_METHOD().getClassName());
            ClassicHttpRequest httpMethod =
                    (ClassicHttpRequest) methodClass
                            .getConstructor(String.class)
                            .newInstance(getHTTP_ENDPOINT() + endpoint);
            this.httpMethod = function.apply(httpMethod);
        } catch (Exception e) {
            String exceptionMsg = String.format("[%s] Failed to create HTTP method: ", getImplClassName());
            throw new IllegalStateException(exceptionMsg + e.getMessage(), e);
        }
    }

    /**
     * 1. 왓차 api 요청에 필요한 헤더들을 붙이는 function 생성
     * 2. HTTP_ENDPOINT 사용하여 HttpRequest 생성 후 function 실행
     */
    protected void initWatchaHttpMethod() {

        Function<ClassicHttpRequest, ClassicHttpRequest> headerFunction = httpMethod -> {

            httpMethod.addHeader("Referer", "https://pedia.watcha.com");
            httpMethod.addHeader("X-Frograms-App-Code", "Galaxy");
            httpMethod.addHeader("X-Frograms-Client", "Galaxy-Web-App");
            httpMethod.addHeader("X-Frograms-Galaxy-Language", "ko");
            httpMethod.addHeader("X-Frograms-Galaxy-Region", "KR");
            httpMethod.addHeader("X-Frograms-Version", "2.1.0");

            return httpMethod;
        };

        createHttpMethod(headerFunction);
    }

    /**
     * 1. 왓차 api 요청에 필요한 헤더들을 붙이는 function 생성
     * 2. HTTP_ENDPOINT+endpoint 사용하여 HttpRequest 생성 후 function 실행
     * @param endPoint
     */
    protected void initWatchaHttpMethod(String endPoint) {

        Function<ClassicHttpRequest, ClassicHttpRequest> headerFunction = httpMethod -> {

            httpMethod.addHeader("Referer", "https://pedia.watcha.com");
            httpMethod.addHeader("X-Frograms-App-Code", "Galaxy");
            httpMethod.addHeader("X-Frograms-Client", "Galaxy-Web-App");
            httpMethod.addHeader("X-Frograms-Galaxy-Language", "ko");
            httpMethod.addHeader("X-Frograms-Galaxy-Region", "KR");
            httpMethod.addHeader("X-Frograms-Version", "2.1.0");

            return httpMethod;
        };

        createHttpMethod(headerFunction, endPoint);
    }

    /* ===================================== Response Handler 관련 메서드 =====================================*/

    /**
     * 1. 받은 response를 HttpResponseWrapper로 래핑 (entity 같은 경우 읽은 후 stream이 소모되기에 저장용)
     * 2. 2xx의 or 302(라우팅) Http Code라면 funciton 실행
     * @param handlerFunction
     */
    protected void createResponseHandler(Function<HttpResponseWrapper, T> handlerFunction) {
        this.responseHandler = httpResponse -> {

            HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper(
                    httpResponse.getCode(),
                    httpResponse.getHeaders(),
                    httpResponse.getEntity()
            );

            httpResponseWrapper.printLog();

            int statusCode = httpResponseWrapper.getCode();
            if ((statusCode >= 200 && statusCode < 300) || statusCode == 302) {
                return handlerFunction.apply(httpResponseWrapper);
            } else {
                String exceptionMsg = String.format("[%s] Unexpected status code : ", getImplClassName());
                throw new IllegalStateException(exceptionMsg + statusCode);
            }
        };
    }


    /**
     * 1. Response Body(String)를 반환하는 Function 생성
     * 2. 해당 function 사용하는 Response Handler 생성
     */
    protected void initStringHandler() {

        Function<HttpResponseWrapper, T> stringFunction
                = responseWrapper -> (T) responseWrapper.getResponseBody();

        createResponseHandler(stringFunction);
    }

    /**
     * 1. Response Body(Json)를 반환하는 Function 생성
     * 2. 해당 function 사용하는 Response Handler 생성
     */
    protected void initJsonHandler() {

        Function<HttpResponseWrapper, T> jsonFunction = responseWrapper -> {
            try {
                return (T) objectMapper.readTree(responseWrapper.getResponseBody());
            } catch (IOException e) {
                String exceptionMsg = String.format("[%s] Failed to parse JSON response: ", getImplClassName());
                throw new IllegalStateException(exceptionMsg + e.getMessage(), e);
            }
        };

        createResponseHandler(jsonFunction);

    }

    /**
     * 1. Response Cookie를 반환하는 Function 생성
     * 2. 해당 function 사용하는 Response Handler 생성
     */
    protected void initCookieHandler() {

        Function<HttpResponseWrapper, T> cookieResponse
                = responseWrapper -> (T) createCookieStoreFromHttpHeaders(responseWrapper.getHeaders());

        createResponseHandler(cookieResponse);

    }

}
