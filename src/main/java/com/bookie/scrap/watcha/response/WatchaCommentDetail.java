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

    @JsonProperty("code")
    private String commentCode;
    
    @JsonProperty("user")
    private WatchaCommentUser commentUser;

    @JsonProperty("text")
    private String commentText;
    
    @JsonProperty("likes_count")
    private String commentLikesCount;
    
    @JsonProperty("replies_count")
    private String commentRepliesCount;
    
    @JsonProperty("content_code")
    private String bookCode;

    @JsonProperty("user_code")
    private String commentUserCode;

    @JsonProperty("watched_at")
    private String commentWatchedAt;

    @JsonProperty("spoiler")
    private String commentSpoiler;

    @JsonProperty("improper")
    private String commentImproper;

    @JsonProperty("replyable")
    private String commentReplyable;

    @JsonProperty("created_at")
    private String commentCreatedAt;

    @JsonProperty("user_content_action")
    private WatchaCommentUserContentAction commentUserContentAction;

}