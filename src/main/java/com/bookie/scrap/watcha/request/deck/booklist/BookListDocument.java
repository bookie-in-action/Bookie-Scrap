package com.bookie.scrap.watcha.request.deck.booklist;

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
@Document(collection = "watcha_deck_booklist")
public class BookListDocument {

    @Id
    private String id;

    private String deckCode;

    private Map<String, Object> rawJson;

    private ZonedDateTime createdAt;

    public BookListDocument() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul")); // 명확한 timezone
    }
}

