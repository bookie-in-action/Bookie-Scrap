package com.bookie.scrap.watcha.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Deprecated
@ToString
public class WatchaCommentUserDTO {

    @JsonProperty("code")
    private String userCode;

    @JsonProperty("name")
    private String userName;

    @JsonProperty("watcha_play_user")
    private String isWatchaPlayUser;

    @JsonProperty("official_user")
    private String isOfficialUser;

}
