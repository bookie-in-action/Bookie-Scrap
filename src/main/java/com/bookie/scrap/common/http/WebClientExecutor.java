package com.bookie.scrap.common.http;

import com.bookie.scrap.common.redis.RedisConnectionService;
import com.bookie.scrap.common.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Objects;
import java.util.function.Function;


@Slf4j
@Component
public class WebClientExecutor {

    private final WebClient webClient;
    private final int MAX_RETRIES;
    private final int RETRY_HOUR;
    private final int THREAD_SLEEP;
    private final RedisConnectionService redisConnectionService;

    public WebClientExecutor(WebClient webClient,
                             @Value("${bookie.http.limit:3}") int MAX_RETIRES,
                             @Value("${bookie.http.retry-hour:1}") int RETRY_HOUR,
                             @Value("${bookie.http.thread-sleep:1000}") int threadSleep,
                             @Qualifier("connectionService") RedisConnectionService redisConnectionService) {
        this.webClient = webClient;
        this.MAX_RETRIES = MAX_RETIRES;
        this.RETRY_HOUR = RETRY_HOUR;
        this.THREAD_SLEEP = threadSleep;
        this.redisConnectionService = redisConnectionService;
    }

    public <T> T execute(SpringRequest springRequest, SpringResponse<T> springResponse) {
        try {
            log.info("Thread sleep: {}ms....", this.THREAD_SLEEP);
            Thread.sleep(this.THREAD_SLEEP);
            redisConnectionService.add();
            return executeWithRetry(springRequest, springResponse);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("쓰레드 인터럽트 발생", e);
            return null;
        } catch (RuntimeException e) {
            log.error("WebClient 실행 중 오류 발생 {} 이상 오류 발생", MAX_RETRIES, e);
            return null;
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
                            if (response.statusCode().isError() || response.statusCode().is4xxClientError()) {
                                return response.bodyToMono(String.class).flatMap(body -> {
                                    log.warn("HTTP {} 오류 발생: {}", response.statusCode(), body);
                                    return Mono.error(new IllegalStateException("HTTP 오류: " + response.statusCode()));
                                });
                            }
                            return Mono.error(new IllegalStateException("No responseHandler defined."));
                        }
                    })
                    .retryWhen(Retry.backoff(MAX_RETRIES, Duration.ofHours(RETRY_HOUR))
                            .filter(throwable -> !(throwable instanceof IllegalStateException))
                            .onRetryExhaustedThrow((spec1, signal) -> signal.failure())
                    )
                    .doOnError(e -> log.error("WebClient 처리 실패", e));

            log.debug("===========================");

            T webClientResponse = responseMono.block();

            try {
                log.debug("webClient Response: {}", JsonUtil.toPrettyJson(Objects.requireNonNull(webClientResponse).toString()));
            } catch (JsonProcessingException e) {
                log.debug("webClient Response: {}", Objects.requireNonNull(webClientResponse));
            }
            log.debug("===========================");

            return webClientResponse;
        } catch (WebClientException e) {
            log.error("WebClientExecutor 실행 중 예외 발생", e);
            return null;
        }
    }
}
