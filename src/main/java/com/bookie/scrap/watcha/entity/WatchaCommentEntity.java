package com.bookie.scrap.watcha.entity;

import com.bookie.scrap.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter @Setter
@Table(name = "BS_WATCHA_COMMENT")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class WatchaCommentEntity extends BaseEntity {

    @Column(name = "comment_code", nullable = false)
    private String commentCode;

    @Column(name = "book_code", nullable = false)
    private String bookCode;

    @Column(name = "user_code", nullable = false)
    private String userCode;

    @Lob
    @Column(name = "comment_text", columnDefinition = "MEDIUMTEXT")
    private String commentText;

    @Column(name = "comment_likes_count")
    private Integer commentLikesCount;

    @Column(name = "comment_replies_count")
    private Integer commentRepliesCount;

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

    @Column(name = "comment_user_content_rating")
    private String commentUserContentRating;

    @Column(name = "comment_user_content_status")
    private String commentUserContentStatus;

    @Column(name = "comment_user_content_mehed")
    private String commentUserContentMehed;


    public void update(WatchaCommentEntity that) {
        this.commentText = that.commentText;
        this.commentLikesCount = that.commentLikesCount;
        this.commentRepliesCount = that.commentRepliesCount;
        this.commentWatchedAt = that.commentWatchedAt;
        this.commentSpoiler = that.commentSpoiler;
        this.commentImproper = that.commentImproper;
        this.commentReplyable = that.commentReplyable;
        this.commentCreatedAt = that.commentCreatedAt;
        this.commentUserContentRating = that.commentUserContentRating;
        this.commentUserContentStatus = that.commentUserContentStatus;
        this.commentUserContentMehed = that.commentUserContentMehed;
    }

    @Override
    public String toString() {
        return "WatchaCommentEntity{" +
                "commentCode='" + commentCode + '\'' +
                ", bookCode='" + bookCode + '\'' +
                ", userCode='" + userCode + '\'' +
                ", commentText='" + commentText + '\'' +
                ", commentLikesCount=" + commentLikesCount +
                ", commentRepliesCount=" + commentRepliesCount +
                ", commentWatchedAt='" + commentWatchedAt + '\'' +
                ", commentSpoiler='" + commentSpoiler + '\'' +
                ", commentImproper='" + commentImproper + '\'' +
                ", commentReplyable='" + commentReplyable + '\'' +
                ", commentCreatedAt='" + commentCreatedAt + '\'' +
                ", commentUserContentRating='" + commentUserContentRating + '\'' +
                ", commentUserContentStatus='" + commentUserContentStatus + '\'' +
                ", commentUserContentMehed='" + commentUserContentMehed + '\'' +
                '}';
    }
}
