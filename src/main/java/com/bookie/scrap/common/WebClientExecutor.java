package com.bookie.scrap.common;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Slf4j
@Component
public class WebClientExecutor {

    private static WebClient webClient;
    private static int MAX_RETRIES;

    private final WebClient injectedWebClient;
    private final int injectedMaxRetries;

    public WebClientExecutor(WebClient webClient, @Value("${bookie.http.limit:3}") int maxRetries) {
        this.injectedWebClient = webClient;
        this.injectedMaxRetries = maxRetries;
    }

    @PostConstruct
    public void init() {
        WebClientExecutor.webClient = injectedWebClient;
        WebClientExecutor.MAX_RETRIES = injectedMaxRetries;
    }

    public static <T> T execute(
            SpringRequest request,
            Class<T> clazz
            ) {
        return executeWithRetry(request, clazz, MAX_RETRIES);
    }

    private static <T> T executeWithRetry(
            SpringRequest request, 
            Class<T> clazz,
            int retriesLeft
    ) {

        try {
            return webClient.method(request.getMethod())
                    .uri(request.getUrl())
                    .headers(httpHeaders -> {
                        if (request.getHeaders() != null) {
                            httpHeaders.addAll(request.getHeaders());
                        }
                    })
                    .cookies(cookies -> {
                        if (request.getCookies() != null) {
                            cookies.addAll(request.getCookies());
                        }
                    })
                    .body((outputMessage, context) -> {
                        Object body = request.getBody();
                        if (body == null) {
                            return BodyInserters.empty().insert(outputMessage, context);
                        }
                        return BodyInserters.fromValue(body).insert(outputMessage, context);
                    })
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::is4xxClientError,
                            response -> {
                                return response
                                        .bodyToMono(String.class)
                                        .flatMap(body -> {
                                            log.warn("4xx 오류 발생: {}", body);
                                            return Mono.error(new IllegalArgumentException("잘못된 요청"));
                                        });
                            })
                    .onStatus(
                            // 5xx
                            HttpStatusCode::isError,
                            clientResponse -> clientResponse.createException().flatMap(Mono::error)
                    )
                    .bodyToMono(clazz)
                    .block();

        } catch (WebClientException e) {
            if (retriesLeft > 0) {
                return executeWithRetry(request, clazz, retriesLeft - 1);
            }
            throw new IllegalStateException("WebClient request failed after max retries", e);
        }
    }
}
