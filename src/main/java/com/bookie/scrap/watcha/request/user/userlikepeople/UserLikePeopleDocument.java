package com.bookie.scrap.watcha.request.user.userlikepeople;

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
@Document(collection = "watcha_user_like_people")
public class UserLikePeopleDocument {

    @Id
    private String id;

    private String userCode;

    private Map<String, Object> rawJson;

    private ZonedDateTime createdAt;

    public UserLikePeopleDocument() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
    }
}

