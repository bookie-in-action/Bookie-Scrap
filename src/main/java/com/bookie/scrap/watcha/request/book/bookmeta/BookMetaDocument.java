package com.bookie.scrap.watcha.request.book.bookmeta;

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
@Document(collection = "watcha_book_meta")
public class BookMetaDocument extends BaseDocument {

    private String bookCode;

    private Map<String, Object> rawJson;

    public BookMetaDocument() {
        super();
    }
}

