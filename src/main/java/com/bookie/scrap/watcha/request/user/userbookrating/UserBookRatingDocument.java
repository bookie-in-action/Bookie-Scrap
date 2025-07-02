package com.bookie.scrap.watcha.request.user.userbookrating;

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
@Document(collection = "watcha_user_book_rating")
public class UserBookRatingDocument extends BaseDocument {

    private String userCode;

    private Map<String, Object> rawJson;

}

