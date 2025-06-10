package com.bookie.scrap.watcha.request.deck.deckinfo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Document(collection = "watcha_deck_meta")
public class DeckInfoDocument {

    @Id
    private String id;

    private String deckCode;

    private Map<String, Object> rawJson;

    private LocalDateTime createdAt;

    public DeckInfoDocument() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now(ZoneId.of("Asia/Seoul")); // 명확한 timezone
    }
}

