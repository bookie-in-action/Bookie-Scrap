package com.bookie.scrap.watcha.response;

import com.bookie.scrap.watcha.dto.WatchaCommentDetailDTO;
import com.bookie.scrap.watcha.dto.WatchaCommentUserContentActionDTO;
import com.bookie.scrap.watcha.dto.WatchaCommentUserDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WatchaCommentDetail {
    
    private String code;
    
    @JsonProperty("user")
    private WatchaCommentUser commentUser;
    
    private String text;
    
    @JsonProperty("likes_count")
    private String likesCount;
    
    @JsonProperty("replies_count")
    private String repliesCount;
    
    @JsonProperty("content_code")
    private String contentCode;

    @JsonProperty("user_code")
    private String userCode;

    @JsonProperty("watched_at")
    private String watchedAt;

    private String spoiler;
    
    @JsonProperty("user_content_action")
    private WatchaCommentUserContentAction commentUserContentAction;

    private WatchaCommentUserDTO setCommentUser() {
        return WatchaCommentUserDTO.builder()
                .code(code)
                .name(code)
                .watchaPlayUser(code)
                .officialUser(code)
                .build();
    }

    private WatchaCommentUserContentActionDTO setUserContentAction() {
        return WatchaCommentUserContentActionDTO.builder()
                .rating(code)
                .status(code)
                .watchedAt(code)
                .userCode(code)
                .contentCode(code)
                .build();
    }

    public WatchaCommentDetailDTO toDto() {
        return WatchaCommentDetailDTO.builder()
                .code(this.code)
                .text(this.text)
                .likesCount(this.likesCount)
                .repliesCount(this.repliesCount)
                .contentCode(this.contentCode)
                .userCode(this.userCode)
                .watchedAt(this.watchedAt)
                .spoiler(this.spoiler)
                .build();
    }
}