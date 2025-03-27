package com.bookie.scrap.watcha.entity;

import com.bookie.scrap.watcha.dto.WatchaCommentDetailDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "BS_WATCHA_COMMENT")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Builder
public class WatchaCommentEntity {
    @Column(name = "book_code", nullable = false)
    private String bookCode;

    @Column(name = "comment_user_code", nullable = false)
    private String commentUserCode;

    @Lob
    @Column(name = "comment_text", columnDefinition = "MEDIUMTEXT")
    private String commentText;

    @Column(name = "comment_likes_count")
    private String commentLikesCount;

    @Column(name = "comment_replies_count")
    private String commentRepliesCount;

    @Column(name = "comment_watched_at")
    private String commentWatchedAt;

    @Column(name = "comment_spoiler")
    private String commentSpoiler;

    @Column(name = "comment_improper")
    private String commentImproper;

    @Column(name = "comment_replyable")
    private String commentReplyable;

    @Column(name = "comment_created_at")
    private String commentCreatedAt;

    @Column(name = "comment_user_name")
    private String commentUserName;

    @Column(name = "watcha_play_user")
    private String watchaPlayUser;

    @Column(name = "official_user")
    private String officialUser;

    @Column(name = "comment_user_content_rating")
    private String commentUserContentRating;

    @Column(name = "comment_user_content_status")
    private String commentUserContentStatus;

    @Column(name = "comment_user_content_mehed")
    private String commentUserContentMehed;

    @Column(name = "comment_user_content_watched_at")
    private String commentUserContentWatchedAt;

}
