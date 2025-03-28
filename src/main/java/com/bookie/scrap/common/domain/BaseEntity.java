package com.bookie.scrap.common.domain;

import com.bookie.scrap.common.domain.converter.StatusConverter;
import com.bookie.scrap.common.util.SnowflakeIdGenerator;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    @Column(name = "status", nullable = false)
    @Convert(converter = StatusConverter.class)
    private Status status;

    @PrePersist
    private void prePersist() {
        if (this.snowflakeId == null) {
            this.snowflakeId = SnowflakeIdGenerator.getId();
        }
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
            this.updatedAt = LocalDateTime.now();
        }

        if (this.status == null) {
            this.status = Status.ACTIVE;
        }
    }

    @PreUpdate
    private void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void inActivate() {
        this.status = Status.INACTIVE;
    }

}
