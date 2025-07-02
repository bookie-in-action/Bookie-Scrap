package com.bookie.scrap.common.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@SuperBuilder
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseEntity {

    @Id
    @Column(name = "snowflake_id", nullable = false, updatable = false)
    @Getter private String snowflakeId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    private void prePersist() {
        if (this.snowflakeId == null) {
            this.snowflakeId = UUID.randomUUID().toString();
        }
        if (this.createdAt == null) {
            Instant now = Instant.now();
            this.createdAt = now;
            this.updatedAt = now;
        }

    }

    @PreUpdate
    private void preUpdate() {
        this.updatedAt = Instant.now();
    }

}
