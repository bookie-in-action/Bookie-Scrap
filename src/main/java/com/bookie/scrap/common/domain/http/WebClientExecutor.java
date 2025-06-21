package com.bookie.scrap.common.domain.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.function.Function;


@Slf4j
@Component
public class WebClientExecutor {

    private final WebClient webClient;
    private final int MAX_RETRIES;
    private final int THREAD_SLEEP;

    public WebClientExecutor(WebClient webClient,
                             @Value("${bookie.http.limit:3}") int MAX_RETIRES,
                             @Value("${bookie.http.thread-sleep:1000}") int threadSleep) {
        this.webClient = webClient;
        this.MAX_RETRIES = MAX_RETIRES;
        this.THREAD_SLEEP = threadSleep;
    }

    public <T> T execute(SpringRequest springRequest, SpringResponse<T> springResponse) {
        try {
            Thread.sleep(this.THREAD_SLEEP);
            return executeWithRetry(springRequest, springResponse);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T executeWithRetry(SpringRequest springRequest, SpringResponse<T> springResponse) {

        try {
            WebClient.RequestBodySpec spec = webClient.method(springRequest.getMethod())
                    .uri(springRequest.getUrl())
                    .headers(httpHeaders -> {
                        if (springRequest.getHeaders() != null) {
                            httpHeaders.addAll(springRequest.getHeaders());
                        }
                    })
                    .cookies(cookies -> {
                        if (springRequest.getCookies() != null) {
                            cookies.addAll(springRequest.getCookies());
                        }
                    });

            WebClient.RequestHeadersSpec<?> requestSpec;
            if (springRequest.getBody() != null) {
                requestSpec = spec.body(BodyInserters.fromValue(springRequest.getBody()));
            } else {
                requestSpec = spec;
            }


            Function<ClientResponse, Mono<T>> responseHandler = springResponse.getHandler();
            Mono<T> responseMono = requestSpec
                    .exchangeToMono(response -> {
                        if (responseHandler != null) {
                            return responseHandler.apply(response);
                        } else {
                            if (response.statusCode().isError()) {
                                return response.bodyToMono(String.class).flatMap(body -> {
                                    log.warn("HTTP {} 오류 발생: {}", response.statusCode(), body);
                                    return Mono.error(new RuntimeException("HTTP 오류: " + response.statusCode()));
                                });
                            }
                            return Mono.error(new IllegalStateException("No responseHandler defined."));
                        }
                    })
                    .retryWhen(Retry.backoff(MAX_RETRIES, Duration.ofMillis(500))
                            .filter(throwable -> !(throwable instanceof IllegalStateException))
                            .onRetryExhaustedThrow((spec1, signal) -> signal.failure())
                    )
                    .doOnError(e -> log.error("WebClient 처리 실패", e));

            return responseMono.block();

        } catch (WebClientException e) {
            log.error("WebClientExecutor 실행 중 예외 발생", e);
            return null;
        }
    }
}
