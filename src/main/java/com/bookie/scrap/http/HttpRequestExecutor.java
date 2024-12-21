package com.bookie.scrap.http;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.AbstractHttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicHeader;
import com.bookie.scrap.config.BaseRequestConfig;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

@Slf4j
public class HttpRequestExecutor {

    public static <T> T execute(HttpHost httpHost,
                                ClassicHttpRequest httpMethod,
                                AbstractHttpEntity entity,
                                HttpClientContext httpClientContext,
                                HttpClientResponseHandler<T> responseHandler) {

        httpMethod.setEntity(entity);

        CloseableHttpClient client = HttpClientProvider.getHttpClient();
        try {
            return client.execute(httpHost, httpMethod, httpClientContext, responseHandler);
        } catch (IOException e) {
            throw new IllegalStateException("Fail to execute Http Request" + e.getMessage(), e);
        }
    }

    public static <T> T execute(HttpHost httpHost,
                                ClassicHttpRequest httpMethod,
                                HttpClientContext clientContext,
                                HttpClientResponseHandler<T> responseHandler) {

        CloseableHttpClient client = HttpClientProvider.getHttpClient();
        try {
            return client.execute(httpHost, httpMethod, clientContext, responseHandler);
        } catch (IOException e) {
            throw new IllegalStateException("Fail to execute Http Request" + e.getMessage(), e);
        }
    }

    public static <T> T execute(BaseRequestConfig<T> requestConfig) {

        ClassicHttpRequest httpMethod = requestConfig.getHttpMethod();
        AbstractHttpEntity entity = requestConfig.getEntity();
        if (entity != null) {
            httpMethod.setEntity(entity);
        }

        HttpHost httpHost = requestConfig.getHttpHost();
        HttpClientContext clientContext = requestConfig.getClientContext();

        HttpClientResponseHandler<T> responseHandler = requestConfig.getResponseHandler();

        if (httpHost == null) {
            String errorMsg = String.format("[%s] httpHost is null", requestConfig.getImplClassName());
            throw new IllegalArgumentException(errorMsg);
        }
        if (httpMethod == null) {
            String errorMsg = String.format("[%s] httpMethod is null", requestConfig.getImplClassName());
            throw new IllegalArgumentException(errorMsg);
        }
        if (responseHandler == null) {
            String errorMsg = String.format("[%s] response hander is null", requestConfig.getImplClassName());
            throw new IllegalArgumentException(errorMsg);
        }

        printLog(
                requestConfig.getImplClassName(),
                httpHost,
                httpMethod,
                entity,
                clientContext
        );

        CloseableHttpClient client = HttpClientProvider.getHttpClient();
        try {
            return client.execute(httpHost, httpMethod, clientContext, responseHandler);
        } catch (IOException e) {
            String errorMsg = String.format("[%s] Fail to execute Http Request", requestConfig.getImplClassName());
            throw new RuntimeException(errorMsg + e.getMessage(), e);
        }

    }

    private static <T> void printLog(String implClassName,
                                     HttpHost httpHost,
                                     ClassicHttpRequest httpMethod,
                                     AbstractHttpEntity entity,
                                     HttpClientContext clientContext) {

        log.debug("==================== {} HTTP REQUEST ====================", implClassName);
        try {
            log.debug("[Request Info]");
            log.debug("   HTTP Method: " + httpMethod.getMethod());
            log.debug("   Request URL: " + httpHost.getHostName() + httpMethod.getUri());
        } catch (URISyntaxException e) {
            throw new RuntimeException("error while making request debug log");
        }

        // 요청 헤더 출력
        log.debug("[Request Headers]");
        if(httpMethod.getHeaders().length != 0) {
            Arrays.stream(httpMethod.getHeaders())
                    .forEach(header -> log.debug("   " + header.getName() + ": " + header.getValue()));
        }

        if (clientContext != null) {
            CookieStore cookieStore = (CookieStore) clientContext.getAttribute(HttpClientContext.COOKIE_STORE);
            if (!cookieStore.getCookies().isEmpty()) {
                cookieStore.getCookies().stream()
                        .map(cookie -> new BasicHeader("Set-Cookie", cookie.toString()))
                        .forEach(header -> log.debug("   " + header.getName() + ": " + header.getValue()));
            }
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);

        if (entity != null) {
            log.debug("[Request Body]");
            try {
                String requestBody = EntityUtils.toString(entity);

                try {
                    JsonNode jsonNode = objectMapper.readTree(requestBody);
                    log.debug("\n" + jsonNode.toPrettyString());
                } catch (JsonParseException e) {
                    log.debug("   " + requestBody);
                }
            } catch (Exception e) {
                log.error("   Failed to log request body: " + e.getMessage());
            }
        }

        log.debug("================================ END ================================\n\n");

    }
}
