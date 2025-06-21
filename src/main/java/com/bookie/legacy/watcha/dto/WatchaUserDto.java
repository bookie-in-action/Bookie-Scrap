package com.bookie.legacy.watcha.dto;


import com.bookie.legacy.common.util.StringUtil;
import com.bookie.legacy.watcha.entity.WatchaUserEntity;
import com.bookie.legacy.watcha.type.WatchaUserPhoto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WatchaUserDto {

    @JsonProperty("code")
    private String userCode;

    @JsonProperty("name")
    private String userName;

    @JsonProperty("photo")
    private WatchaUserPhoto userPhoto;

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
    private Integer bookcaseCnt;

    public WatchaUserEntity toEntity() {
        return WatchaUserEntity.builder()
                .userCode(StringUtil.nonNull(userCode))
                .userName(StringUtil.nonNull(userName))
                .userPhoto(userPhoto.getOriginal().contains("default") ? new WatchaUserPhoto() : userPhoto)
                .isWatchaPlayUser(StringUtil.nonNull(isWatchaPlayUser))
                .isOfficialUser(StringUtil.nonNull(isOfficialUser))
                .userDescription(StringUtil.nonNull(userDescription))
                .commentsCnt(commentsCnt)
                .ratingsCnt(ratingsCnt)
                .wishesCnt(wishesCnt)
                .bookcaseCnt(bookcaseCnt)
                .build();
    }
}
