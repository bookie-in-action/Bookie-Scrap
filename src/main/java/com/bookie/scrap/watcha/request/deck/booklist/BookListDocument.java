package com.bookie.scrap.watcha.request.deck.booklist;

import com.bookie.scrap.common.domain.BaseDocument;
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
public class BookListDocument extends BaseDocument {

    private String deckCode;

    private Map<String, Object> rawJson;

}

