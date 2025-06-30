package com.bookie.scrap.watcha.request.book.booktodecks;

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
@Document(collection = "watcha_book_deck")
public class BookToDecksDocument extends BaseDocument {

    private String bookCode;

    private Map<String, Object> rawJson;

    public BookToDecksDocument() {
        super();
    }
}

