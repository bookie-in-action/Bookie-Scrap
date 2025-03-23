package com.bookie.scrap.watcha.dto;


import com.bookie.scrap.watcha.type.WatchaType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WatchaUser {

    @JsonProperty("code")
    private String userCode;

    @JsonProperty("name")
    private String userName;

    @JsonProperty("photo")
    private WatchaType.UserPhoto userPhoto;

    @JsonProperty("watcha_play_user")
    private String isWatchaPlayUser;

    @JsonProperty("official_user")
    private String isOfficialUser;

    @JsonProperty("bio")
    private String userDescription;

    @JsonProperty("comments_count")
    private Integer commentsCnt;

    @JsonProperty("ratings_count")
    private Integer ratingsCnt;

    @JsonProperty("wishes_count")
    private Integer wishesCnt;

    @JsonProperty("decks_count")
    private Integer decksCnt;

}
