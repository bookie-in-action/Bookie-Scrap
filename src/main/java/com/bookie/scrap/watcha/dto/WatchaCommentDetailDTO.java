package com.bookie.scrap.watcha.dto;

import com.bookie.scrap.watcha.entity.WatchaCommentEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WatchaCommentDetailDTO {
    // PK
    private String snowflakeId;

    // WatchaCommentDetail
    @JsonProperty("code")
    private String commentCode;

    @JsonProperty("text")
    private String commentText;

    @JsonProperty("content_code")
    private String bookCode;

    @JsonProperty("likes_count")
    private Integer commentLikesCount;

    @JsonProperty("replies_count")
    private Integer commentRepliesCount;

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

    @JsonProperty("user")
    private WatchaCommentUserDTO watchaCommentUserDTO;

    @JsonProperty("user_content_action")
    private WatchaCommentUserContentActionDTO watchaCommentUserContentActionDTO;

    // ResultSet을 받아 DTO 객체 반환
    public static WatchaCommentDetailDTO fromResultSet(ResultSet resultSet) throws SQLException {
        WatchaCommentUserDTO userDTO = new WatchaCommentUserDTO().builder()
                .code(resultSet.getString("comment_user_code"))
                .name(resultSet.getString("comment_user_name"))
                .watchaPlayUser(resultSet.getString("watcha_play_user"))
                .officialUser(resultSet.getString("official_user"))
                .build();

        WatchaCommentUserContentActionDTO userContentActionDTO = new WatchaCommentUserContentActionDTO().builder()
                .rating(resultSet.getString("comment_user_content_rating"))
                .status(resultSet.getString("comment_user_content_status"))
                .mehed(resultSet.getString("comment_user_content_mehed"))
                .watchedAt(resultSet.getString("comment_user_content_watched_at"))
                .userCode(resultSet.getString("comment_user_code"))
                .contentCode(resultSet.getString("book_code"))
                .build();

        return WatchaCommentDetailDTO.builder()
                .bookCode(resultSet.getString("book_code"))
                .watchaCommentUserDTO(userDTO)
                .commentText(resultSet.getString("comment_text"))
                .commentLikesCount(resultSet.getInt("comment_likes_count"))
                .commentRepliesCount(resultSet.getInt("comment_replies_count"))
                .commentWatchedAt(resultSet.getString("comment_watched_at"))
                .commentSpoiler(resultSet.getString("comment_spoiler"))
                .commentImproper(resultSet.getString("comment_improper"))
                .commentReplyable(resultSet.getString("comment_replyable"))
                .commentCreatedAt(resultSet.getString("comment_created_at"))
                .watchaCommentUserContentActionDTO(userContentActionDTO)
                .build();
    }
    
    // Entity 로 변환
    public static WatchaCommentEntity toEntity(WatchaCommentDetailDTO dto) {
        return WatchaCommentEntity.builder()
                .bookCode(dto.getBookCode())
                .commentUserCode(dto.getWatchaCommentUserDTO().getCode())
                .commentText(dto.getCommentText())
                .commentLikesCount(dto.getCommentLikesCount())
                .commentRepliesCount(dto.getCommentRepliesCount())
                .commentWatchedAt(dto.getCommentWatchedAt())
                .commentSpoiler(dto.getCommentSpoiler())
                .commentImproper(dto.getCommentImproper())
                .commentReplyable(dto.getCommentReplyable())
                .commentCreatedAt(dto.getCommentCreatedAt())
                .commentUserName(dto.getWatchaCommentUserDTO().getName())
                .watchaPlayUser(dto.getWatchaCommentUserDTO().getWatchaPlayUser())
                .officialUser(dto.getWatchaCommentUserDTO().getOfficialUser())
                .commentUserContentRating(dto.getWatchaCommentUserContentActionDTO().getRating())
                .commentUserContentStatus(dto.getWatchaCommentUserContentActionDTO().getStatus())
                .commentUserContentMehed(dto.getWatchaCommentUserContentActionDTO().getMehed())
                .commentUserContentWatchedAt(dto.getWatchaCommentUserContentActionDTO().getWatchedAt())
                .build();
    }
}
