package com.bookie.scrap.common.http;

import com.bookie.scrap.common.domain.Request;
import com.bookie.scrap.common.properties.BookieProperties;
import com.bookie.scrap.common.util.ThreadUtil;
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
import org.apache.hc.core5.http.message.BasicHeader;

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

    public static <T> T execute(ClassicHttpRequest httpRequest, HttpClientResponseHandler<T> responseHandler) {

        ThreadUtil.sleep();

        printLog(httpRequest);

        try {
            return executeWithRetry(httpRequest, responseHandler);
        } catch (Exception e) {
            log.error("Unexpected error: " + e.getMessage());
            throw new IllegalStateException("An unexpected error occurred: " + e.getMessage(), e);
        }

    }

    public static <T> T execute(Request<T> request) {

        ClassicHttpRequest mainRequest = request.getMainRequest();
        HttpClientContext clientContext = request.getClientContext();
        HttpClientResponseHandler<T> responseHandler = request.getResponseHandler();

        if (mainRequest == null) {
            String errorMsg = String.format("[%s] httpMethod is null", request.getImplClassName());
            throw new IllegalArgumentException(errorMsg);
        }
        if (responseHandler == null) {
            String errorMsg = String.format("[%s] response hander is null", request.getImplClassName());
            throw new IllegalArgumentException(errorMsg);
        }

        printLog(
                request.getImplClassName(),
                mainRequest,
                clientContext
        );

        try {
            return executeWithRetry(mainRequest, clientContext, responseHandler);
        } catch (Exception e) {
            log.error("Unexpected error: " + e.getMessage());
            throw new IllegalStateException("An unexpected error occurred: " + e.getMessage(), e);
        }

    }

    private static <T> T executeWithRetry(ClassicHttpRequest httpRequest, HttpClientResponseHandler<T> responseHandler) throws Exception {


        for (int i = 0; i < MAX_RETRIES; i++) {
            CloseableHttpClient client = HttpClientProvider.getHttpClient();
            try {
                ThreadUtil.sleep();
                return client.execute(httpRequest, responseHandler);
            } catch (SocketTimeoutException | NoHttpResponseException e) {
                log.error("Network issue: {}. Retrying ({}/{})", e.getClass().getSimpleName(), i + 1, MAX_RETRIES);
                if (i == MAX_RETRIES - 1) {
                    throw new IllegalStateException("Max retries reached for network error: " + e.getMessage(), e);
                }
            }
        }
        throw new IllegalStateException("Unexpected state reached in retry logic.");
    }

    private static <T> T executeWithRetry(ClassicHttpRequest httpRequest,
                                          HttpClientContext classicHttpRequest,
                                          HttpClientResponseHandler<T> responseHandler) {


        for (int i = 0; i < MAX_RETRIES; i++) {
            CloseableHttpClient client = HttpClientProvider.getHttpClient();
            try {
                ThreadUtil.sleep();
                return client.execute(httpRequest, classicHttpRequest, responseHandler);
            } catch (HttpResponseException e) {
                handleHttpResponseException(e);
            } catch (SocketTimeoutException | NoHttpResponseException e) {
                log.error("Network issue: {}. Retrying ({}/{})", e.getClass().getSimpleName(), i + 1, MAX_RETRIES);
                if (i == MAX_RETRIES - 1) {
                    throw new IllegalStateException("Max retries reached for network error: " + e.getMessage(), e);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        throw new IllegalStateException("Unexpected state reached in retry logic.");
    }

    private static void handleHttpResponseException(HttpResponseException e) {
        int statusCode = e.getStatusCode();
        if (statusCode == 401) {
            log.error("Unauthorized (401): Authentication failed.");
        } else if (statusCode == 404) {
            log.error("Not Found (404): The requested resource was not found.");
        } else if (statusCode >= 500) {
            log.error("Server Error ({}): ", e.getMessage());
        } else {
            log.error("Unexpected HTTP response ({}): ", e.getMessage());
        }
    }

    private static void printLog(String implClassName,
                                     HttpHost httpHost,
                                     ClassicHttpRequest httpMethod,
                                     AbstractHttpEntity entity,
                                     HttpClientContext clientContext) {

        log.trace("==================== {} HTTP REQUEST ====================", implClassName);
        try {
            log.debug("[Request Info]");
            log.debug("   HTTP Method: " + httpMethod.getMethod());
            log.debug("   Request URL: " + httpHost.getHostName() + httpMethod.getUri());
        } catch (URISyntaxException e) {
            throw new RuntimeException("error while making request debug(); log: ", e);
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


        //TODO: 요청 바디 출력
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);
//
//        if (entity != null) {
//            log.debug();();("[Request Body]");
//            try {
//                String requestBody = EntityUtils.toString(entity);
//
//                try {
//                    JsonNode jsonNode = objectMapper.readTree(requestBody);
//                    log.debug();();("\n" + jsonNode.toPrettyString());
//                } catch (JsonParseException e) {
//                    log.debug();();("   " + requestBody);
//                }
//            } catch (Exception e) {
//                log.error("   Failed to log request body: " + e.getMessage());
//            }
//        }

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
            throw new RuntimeException("error while making request debug(); log: ", e);
        }

        log.debug("[Request Headers]");
        if(httpMethod.getHeaders().length != 0) {
            Arrays.stream(httpMethod.getHeaders())
                    .forEach(header -> log.debug("   " + header.getName() + ": " + header.getValue()));
        }

        log.debug("================================ END ================================\n\n");

    }

    private static void printLog(String implClassName,
                                 ClassicHttpRequest mainRequest,
                                 HttpClientContext clientContext) {

        log.debug("==================== {} HTTP REQUEST ====================", implClassName);

        // HTTP 메서드와 URL 출력
        try {
            log.debug("[Request Info]");
            log.debug("   HTTP Method: {}", mainRequest.getMethod());
            log.debug("   Request URL: {}", mainRequest.getUri());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        // 요청 헤더 출력
        log.debug("[Request Headers]");
        if (mainRequest.getHeaders().length != 0) {
            Arrays.stream(mainRequest.getHeaders())
                    .forEach(header -> log.debug("   {}: {}", header.getName(), header.getValue()));
        }

        // 쿠키 출력
        if (clientContext != null) {
            CookieStore cookieStore = (CookieStore) clientContext.getAttribute(HttpClientContext.COOKIE_STORE);
            if (cookieStore != null && !cookieStore.getCookies().isEmpty()) {
                log.debug("[Request Cookies]");
                cookieStore.getCookies().forEach(cookie ->
                        log.debug("   Set-Cookie: {}", cookie.toString())
                );
            }
        }

        // TODO: 요청 바디 출력
//        log.debug();("[Request Body]");
//        try {
//            if (mainRequest instanceof HttpEntityEnclosingRequest) {
//                HttpEntity entity = ((HttpEntityEnclosingRequest) mainRequest).getEntity();
//                if (entity != null) {
//                    String requestBody = EntityUtils.toString(entity);
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    objectMapper.enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);
//
//                    try {
//                        JsonNode jsonNode = objectMapper.readTree(requestBody);
//                        log.debug();("\n{}", jsonNode.toPrettyString());
//                    } catch (JsonParseException e) {
//                        log.debug();("   {}", requestBody);
//                    }
//                } else {
//                    log.debug();("   No body present");
//                }
//            } else {
//                log.debug();("   Request does not support a body");
//            }
//        } catch (Exception e) {
//            log.error("   Failed to log request body: {}", e.getMessage());
//        }

        log.debug("================================ END ================================\n\n");
    }

}
