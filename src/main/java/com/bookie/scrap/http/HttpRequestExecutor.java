package com.bookie.scrap.http;

import com.bookie.scrap.properties.BookieProperties;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.HttpResponseException;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.NoHttpResponseException;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.AbstractHttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicHeader;
import com.bookie.scrap.config.BaseRequest;

import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.util.Arrays;

@Slf4j
public class HttpRequestExecutor {

    private final static int MAX_RETRIES = Integer.parseInt(
            BookieProperties.getInstance().getValue(BookieProperties.Key.RETRY_COUNT)
    );

    public static <T> T execute(HttpHost httpHost,
                                ClassicHttpRequest httpMethod,
                                AbstractHttpEntity entity,
                                HttpClientContext clientContext,
                                HttpClientResponseHandler<T> responseHandler) {

        httpMethod.setEntity(entity);

        try {
            return executeWithRetry(httpHost, httpMethod, clientContext, responseHandler);
        } catch (HttpResponseException e) {
            handleHttpResponseException(e);
            throw new IllegalStateException("This line is unreachable, but added for completeness.");
        } catch (SSLHandshakeException e) {
            log.error("SSL handshake failed: " + e.getMessage());
            throw new IllegalStateException("SSL error: " + e.getMessage(), e);
        } catch (IOException e) {
            log.error("I/O error: " + e.getMessage());
            throw new IllegalStateException("Unexpected I/O error: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error: " + e.getMessage());
            throw new IllegalStateException("An unexpected error occurred: " + e.getMessage(), e);
        }

    }

    public static <T> T execute(HttpHost httpHost,
                                ClassicHttpRequest httpMethod,
                                HttpClientContext clientContext,
                                HttpClientResponseHandler<T> responseHandler) {

        try {
            return executeWithRetry(httpHost, httpMethod, clientContext, responseHandler);
        } catch (HttpResponseException e) {
            handleHttpResponseException(e);
            throw new IllegalStateException("This line is unreachable, but added for completeness.");
        } catch (SSLHandshakeException e) {
            log.error("SSL handshake failed: " + e.getMessage());
            throw new IllegalStateException("SSL error: " + e.getMessage(), e);
        } catch (IOException e) {
            log.error("I/O error: " + e.getMessage());
            throw new IllegalStateException("Unexpected I/O error: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error: " + e.getMessage());
            throw new IllegalStateException("An unexpected error occurred: " + e.getMessage(), e);
        }
    }

    public static <T> T execute(ClassicHttpRequest httpRequest, HttpClientResponseHandler<T> responseHandler) {

        printLog(httpRequest);

        try {
            return executeWithRetry(httpRequest, responseHandler);
        } catch (HttpResponseException e) {
            handleHttpResponseException(e);
            throw new IllegalStateException("This line is unreachable, but added for completeness.");
        } catch (SSLHandshakeException e) {
            log.error("SSL handshake failed: " + e.getMessage());
            throw new IllegalStateException("SSL error: " + e.getMessage(), e);
        } catch (IOException e) {
            log.error("I/O error: " + e.getMessage());
            throw new IllegalStateException("Unexpected I/O error: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error: " + e.getMessage());
            throw new IllegalStateException("An unexpected error occurred: " + e.getMessage(), e);
        }

    }

    public static <T> T execute(BaseRequest<T> requestConfig) {

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

        try {
            return executeWithRetry(httpHost, httpMethod, clientContext, responseHandler);
        } catch (HttpResponseException e) {
            handleHttpResponseException(e);
            throw new IllegalStateException("This line is unreachable, but added for completeness.");
        } catch (SSLHandshakeException e) {
            log.error("SSL handshake failed: " + e.getMessage());
            throw new IllegalStateException("SSL error: " + e.getMessage(), e);
        } catch (IOException e) {
            log.error("I/O error: " + e.getMessage());
            throw new IllegalStateException("Unexpected I/O error: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error: " + e.getMessage());
            throw new IllegalStateException("An unexpected error occurred: " + e.getMessage(), e);
        }

    }

    private static <T> T executeWithRetry(ClassicHttpRequest httpRequest, HttpClientResponseHandler<T> responseHandler) throws Exception {

        for (int i = 0; i < MAX_RETRIES; i++) {
            CloseableHttpClient client = HttpClientProvider.getHttpClient();
            try {
                return client.execute(httpRequest, responseHandler);
            } catch (SocketTimeoutException | NoHttpResponseException e) {

                log.error("Network issue: {}. Retrying ({}/{})", e.getClass().getSimpleName(), i + 1, MAX_RETRIES);
                if (i == MAX_RETRIES - 1) {
                    throw new IllegalStateException("Max retries reached for network error: " + e.getMessage(), e);
                }

                Thread.sleep(1000);
            }
        }
        throw new IllegalStateException("Unexpected state reached in retry logic.");
    }

    private static <T> T executeWithRetry(HttpHost httpHost, ClassicHttpRequest httpMethod, HttpClientContext clientContext, HttpClientResponseHandler<T> responseHandler) throws Exception {

        for (int i = 0; i < MAX_RETRIES; i++) {
            CloseableHttpClient client = HttpClientProvider.getHttpClient();
            try {
                return client.execute(httpHost, httpMethod, clientContext, responseHandler);
            } catch (SocketTimeoutException | NoHttpResponseException e) {
                log.error("Network issue: " + e.getClass().getSimpleName() + ". Retrying (" + (i + 1) + "/" + MAX_RETRIES + ").");
                if (i == MAX_RETRIES - 1) {
                    throw new IllegalStateException("Max retries reached for network error: " + e.getMessage(), e);
                }
                Thread.sleep(1000);
            }
        }
        throw new IllegalStateException("Unexpected state reached in retry logic.");
    }

    private static void handleHttpResponseException(HttpResponseException e) throws IllegalStateException {
        int statusCode = e.getStatusCode();
        if (statusCode == 401) {
            log.error("Unauthorized (401): Authentication failed.");
            throw new IllegalStateException("Unauthorized access: " + e.getMessage(), e);
        } else if (statusCode == 404) {
            log.error("Not Found (404): The requested resource was not found.");
            throw new IllegalStateException("Resource not found: " + e.getMessage(), e);
        } else if (statusCode >= 500) {
            log.error("Server Error ({}): ", e.getMessage());
            throw new IllegalStateException("Server error occurred: " + e.getMessage(), e);
        } else {
            log.error("Unexpected HTTP response ({}): ", e.getMessage());
            throw new IllegalStateException("Unexpected HTTP response: " + e.getMessage(), e);
        }
    }

    private static void printLog(String implClassName,
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
            throw new RuntimeException("error while making request debug log: ", e);
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


    private static void printLog(ClassicHttpRequest httpMethod) {

        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
        String executeClass = stackTraceElement.toString().split("\\.")[5];

        log.debug("==================== {} HTTP REQUEST ====================", executeClass);
        try {
            log.debug("[Request Info]");
            log.debug("   HTTP Method: " + httpMethod.getMethod());
            log.debug("   Request URL: " + httpMethod.getUri());
        } catch (URISyntaxException e) {
            throw new RuntimeException("error while making request debug log: ", e);
        }

        log.debug("[Request Headers]");
        if(httpMethod.getHeaders().length != 0) {
            Arrays.stream(httpMethod.getHeaders())
                    .forEach(header -> log.debug("   " + header.getName() + ": " + header.getValue()));
        }

        log.debug("================================ END ================================\n\n");

    }
}
