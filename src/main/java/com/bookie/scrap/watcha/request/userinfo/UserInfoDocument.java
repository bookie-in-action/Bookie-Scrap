package com.bookie.scrap.watcha.request.userinfo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Getter
@Setter
@Document(collection = "watcha_userinfos")
public class UserInfoDocument {

    @Id
    private String id;

    private String userCode;

    private String rawJson;

    private LocalDateTime createdAt;

    public UserInfoDocument() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now(ZoneId.of("Asia/Seoul")); // 명확한 timezone
    }
}

