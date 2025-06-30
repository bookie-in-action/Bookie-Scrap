package com.bookie.scrap.watcha.request.book.bookcomment;

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
@Document(collection = "watcha_book_comment")
public class BookCommentDocument {

    @Id
    private String id;

    private String bookCode;

    private Map<String, Object> rawJson;

    private ZonedDateTime createdAt;

    public BookCommentDocument() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul")); // 명확한 timezone
    }
}

