package com.bookie.scrap.watcha.dto;

import com.bookie.scrap.common.util.StringUtil;
import com.bookie.scrap.watcha.domain.deserializer.WatchaParseDateTimeDeserializer;
import com.bookie.scrap.watcha.entity.WatchaCommentEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WatchaCommentDto {

    // WatchaCommentDetail
    @JsonProperty("code")
    private String commentCode;

    @JsonProperty("text")
    private String commentText;

    @JsonProperty("likes_count")
    private Integer commentLikesCount;

    @JsonProperty("replies_count")
    private Integer commentRepliesCount;

    @JsonProperty("content_code")
    private String bookCode;

    @JsonProperty("user_code")
    private String userCode;

    @JsonProperty("watched_at")
    @JsonDeserialize(using = WatchaParseDateTimeDeserializer.class)
    private String commentWatchedAt;

    @JsonProperty("spoiler")
    private String isCommentSpoiler;

    @JsonProperty("improper")
    private String isCommentImproper;

    @JsonProperty("replyable")
    private String isCommentReplyable;

    @JsonProperty("created_at")
    @JsonDeserialize(using = WatchaParseDateTimeDeserializer.class)
    private String commentCreatedAt;

//    @JsonProperty("user")
//    private WatchaCommentUserDTO watchaCommentUserDTO;

    @JsonProperty("user_content_action")
    private userCommentActionDto userCommentActionDto;

    @Getter
    @ToString
    private static class userCommentActionDto {

        @JsonProperty("rating")
        private String rating;

        @JsonProperty("status")
        private String status;

        @JsonProperty("mehed")
        private String mehed;

    }

    public WatchaCommentEntity toEntity() {
        return WatchaCommentEntity.builder()
                .commentCode(StringUtil.nonNull(commentCode))
                .bookCode(StringUtil.nonNull(bookCode))
                .userCode(StringUtil.nonNull(userCode))
                .commentText(StringUtil.nonNull(commentText))
                .commentLikesCount(commentLikesCount)
                .commentRepliesCount(commentRepliesCount)
                .commentWatchedAt(StringUtil.nonNull(commentWatchedAt))
                .commentCreatedAt(StringUtil.nonNull(commentCreatedAt))
                .commentSpoiler(StringUtil.nonNull(isCommentSpoiler))
                .commentImproper(StringUtil.nonNull(isCommentImproper))
                .commentReplyable(StringUtil.nonNull(isCommentReplyable))
                .commentUserContentRating(StringUtil.nonNull(userCommentActionDto != null ? userCommentActionDto.rating : null))
                .commentUserContentStatus(StringUtil.nonNull(userCommentActionDto != null ? userCommentActionDto.status : null))
                .commentUserContentMehed(StringUtil.nonNull(userCommentActionDto != null ? userCommentActionDto.mehed : null))
                .build();
    }

}