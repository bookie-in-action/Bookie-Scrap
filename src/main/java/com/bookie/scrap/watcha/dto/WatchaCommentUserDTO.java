package com.bookie.scrap.watcha.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class WatchaCommentUserDTO {


    @JsonProperty("code")
    private String code;

    @JsonProperty("name")
    private String name;

    @JsonProperty("watcha_play_user")
    private String watchaPlayUser;

    @JsonProperty("official_user")
    private String officialUser;

}
