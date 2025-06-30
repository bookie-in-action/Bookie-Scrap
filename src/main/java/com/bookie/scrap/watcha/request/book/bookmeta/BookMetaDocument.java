package com.bookie.scrap.watcha.request.book.bookmeta;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Document(collection = "watcha_book_meta")
public class BookMetaDocument {

    @Id
    private String id;

    private String bookCode;

    private Map<String, Object> rawJson;

    private ZonedDateTime createdAt;

    public BookMetaDocument() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
    }
}

