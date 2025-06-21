package com.bookie.scrap.common.domain.http;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Getter
@AllArgsConstructor
public class SpringResponse<T> {
    private Function<ClientResponse, Mono<T>> handler;
}
