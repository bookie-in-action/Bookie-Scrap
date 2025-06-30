package com.bookie.scrap.watcha.request.user.withlogin_userlikedeck;

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
@Document(collection = "watcha_user_like_deck")
public class UserLikeDeckDocument extends BaseDocument {

    private String userCode;

    private Map<String, Object> rawJson;

}

