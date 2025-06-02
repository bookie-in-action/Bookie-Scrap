package com.bookie.scrap.watcha.request.comment;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Getter
@Setter
@Document(collection = "watcha_comments")
public class WatchaCommentDocument {

    @Id
    private String id;

    private String bookCode;

    private String rawJson;

    private LocalDateTime createdAt;

    public WatchaCommentDocument() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now(ZoneId.of("Asia/Seoul")); // 명확한 timezone
    }
}

