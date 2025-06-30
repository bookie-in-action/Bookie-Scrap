package com.bookie.scrap.common.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public abstract class BaseDocument {

    @Id
    private String id;

    private Instant createdAt;

    public BaseDocument() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = Instant.now();
    }
}
