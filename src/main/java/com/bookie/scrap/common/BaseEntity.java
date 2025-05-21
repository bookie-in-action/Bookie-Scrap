package com.bookie.scrap.common;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@SuperBuilder
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseEntity {

    @Id
    @Column(name = "snowflake_id", nullable = false, updatable = false)
    @Getter private String snowflakeId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    private void prePersist() {
        if (this.snowflakeId == null) {
            // TODO:
            this.snowflakeId = UUID.randomUUID().toString();
        }
        if (this.createdAt == null) {
            LocalDateTime now = LocalDateTime.now();
            this.createdAt = now;
            this.updatedAt = now;
        }

    }

    @PreUpdate
    private void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
