package com.bookie.scrap.watcha.request.book.bookmeta;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Getter
@Setter
@Document(collection = "watcha_book_meta")
public class BookMetaDocument {

    @Id
    private String id;

    private String bookCode;

    private JsonNode rawJson;

    private LocalDateTime createdAt;

    public BookMetaDocument() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now(ZoneId.of("Asia/Seoul")); // 명확한 timezone
    }
}

