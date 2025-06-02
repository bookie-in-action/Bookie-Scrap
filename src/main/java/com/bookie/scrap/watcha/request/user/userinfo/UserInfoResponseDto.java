package com.bookie.scrap.watcha.request.user.userinfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class UserInfoResponseDto {

    @JsonProperty("metadata")
    private Object metaData;

    @JsonProperty("result")
    private Object userInfo;

}
