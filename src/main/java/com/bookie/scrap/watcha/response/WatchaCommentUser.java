package com.bookie.scrap.watcha.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WatchaCommentUser {
    
    private String code;
    private String name;
    
    @JsonProperty("watcha_play_user")
    private String watchaPlayUser;
    
    @JsonProperty("official_user")
    private String officialUser;

}
