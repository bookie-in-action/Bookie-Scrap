package com.bookie.scrap.watcha.dto;

import com.bookie.scrap.watcha.entity.WatchaCommentEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class WatchaCommentDetailDTO {

    // WatchaCommentDetail
    private String bookCode;
    private String commentUserCode;
    private String commentText;
    private String commentLikesCount;
    private String commentRepliesCount;
    private String commentWatchedAt;
    private String commentSpoiler;
    private String commentImproper;
    private String commentReplyable;
    private String commentCreatedAt;

    // WatchaCommentUser;
    private String commentUserName;
    private String watchaPlayUser;
    private String officialUser;

    // WatchaCommentUserPhoto
    private String commentUserPhotoOriginal;
    private String commentUserPhotoLarge;
    private String commentUserPhotoSmall;

    // WatchaCommentUserContentAction
    private String commentUserContentRating;
    private String commentUserContentStatus;
    private String commentUserContentMehed;
    private String commentUserContentWatchedAt;
    //private String commentUserContentUserCode;  // commentUserCode 와 동일한 값, 필드명만 다름

    // ResultSet을 받아 DTO 객체 반환
    public static WatchaCommentDetailDTO fromResultSet(ResultSet resultSet) throws SQLException {
        return WatchaCommentDetailDTO.builder()
                .bookCode(resultSet.getString("book_code"))
                .commentUserCode(resultSet.getString("comment_user_code"))
                .commentText(resultSet.getString("comment_text"))
                .commentLikesCount(resultSet.getString("comment_likes_count"))
                .commentRepliesCount(resultSet.getString("comment_replies_count"))
                .commentWatchedAt(resultSet.getString("comment_watched_at"))
                .commentSpoiler(resultSet.getString("comment_spoiler"))
                .commentImproper(resultSet.getString("comment_improper"))
                .commentReplyable(resultSet.getString("comment_replyable"))
                .commentCreatedAt(resultSet.getString("comment_created_at"))
                .commentUserName(resultSet.getString("comment_user_name"))
                .watchaPlayUser(resultSet.getString("watcha_play_user"))
                .officialUser(resultSet.getString("official_user"))
                .commentUserPhotoOriginal(resultSet.getString("comment_user_photo_original"))
                .commentUserPhotoLarge(resultSet.getString("comment_user_photo_large"))
                .commentUserPhotoSmall(resultSet.getString("comment_user_photo_small"))
                .commentUserContentRating(resultSet.getString("comment_user_content_rating"))
                .commentUserContentStatus(resultSet.getString("comment_user_content_status"))
                .commentUserContentMehed(resultSet.getString("comment_user_content_mehed"))
                .commentUserContentWatchedAt(resultSet.getString("comment_user_content_watched_at"))
                .build();
    }
    
    // Entity 로 변환
    public static WatchaCommentEntity toEntity(WatchaCommentDetailDTO dto) {
        return WatchaCommentEntity.builder()
                .bookCode(dto.getBookCode())
                .userCode(dto.getCommentUserCode())
                .commentText(dto.getCommentText())
                .commentLikesCount(dto.getCommentLikesCount())
                .commentRepliesCount(dto.getCommentRepliesCount())
                .commentWatchedAt(dto.getCommentWatchedAt())
                .commentSpoiler(dto.getCommentSpoiler())
                .commentImproper(dto.getCommentImproper())
                .commentReplyable(dto.getCommentReplyable())
                .commentCreatedAt(dto.getCommentCreatedAt())
//                .commentUserName(dto.getCommentUserName())
//                .watchaPlayUser(dto.getWatchaPlayUser())
//                .officialUser(dto.getOfficialUser())
                .commentUserContentRating(dto.getCommentUserContentRating())
                .commentUserContentStatus(dto.getCommentUserContentStatus())
                .commentUserContentMehed(dto.getCommentUserContentMehed())
                .commentUserContentWatchedAt(dto.getCommentUserContentWatchedAt())
                .build();
    }
}
