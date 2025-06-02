package com.bookie.scrap.watcha.request.user.userinfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

import java.util.List;

@Getter
public class UserInfoResponseDto {

    @JsonProperty("metadata")
    private JsonNode metaData;

    @JsonProperty("result")
    private JsonNode userInfo;

}
