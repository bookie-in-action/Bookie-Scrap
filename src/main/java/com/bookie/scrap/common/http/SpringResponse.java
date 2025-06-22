package com.bookie.scrap.common.http;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Getter
@AllArgsConstructor
public class SpringResponse<T> {
    private Function<ClientResponse, Mono<T>> handler;
}
