package com.bookie.scrap.watcha.request.book.booktodecks;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Getter
@Setter
@Document(collection = "watcha_book_deck")
public class BookToDecksDocument {

    @Id
    private String id;

    private String bookCode;

    private String rawJson;

    private LocalDateTime createdAt;

    public BookToDecksDocument() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now(ZoneId.of("Asia/Seoul")); // 명확한 timezone
    }
}

