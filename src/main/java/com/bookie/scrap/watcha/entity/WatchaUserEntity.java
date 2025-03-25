package com.bookie.scrap.watcha.entity;

import com.bookie.scrap.common.domain.BaseEntity;
import com.bookie.scrap.common.util.ListStringConverter;
import com.bookie.scrap.watcha.type.WatchaType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@ToString
@Getter @Setter
@Entity @SuperBuilder
@Table(name = "BS_WATCHA_USER")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public final class WatchaUserEntity extends BaseEntity {

    @Column(name = "user_code")
    private String userCode;

    @Column(name = "user_name")
    private String userName;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "original", column = @Column(name = "user_photo_original")),
            @AttributeOverride(name = "large", column = @Column(name = "user_photo_large")),
            @AttributeOverride(name = "small", column = @Column(name = "user_photo_small"))
    })
    private WatchaType.UserPhoto userPhoto;

    @Column(name = "is_watcha_play_user")
    private String isWatchaPlayUser;

    @Column(name = "is_official_user")
    private String isOfficialUser;

    @Lob
    @Column(name = "user_description", columnDefinition = "MEDIUMTEXT")
    private String userDescription;

    @Column(name = "comments_count")
    private Integer commentsCnt;

    @Column(name = "ratings_count")
    private Integer ratingsCnt;

    @Column(name = "wishes_count")
    private Integer wishesCnt;

    @Column(name = "bookcase_count")
    private Integer bookcaseCnt;

    public void updateEntity(WatchaUserEntity that) {
        this.userName = that.userName;
        this.userPhoto = that.userPhoto;
        this.isWatchaPlayUser = that.isWatchaPlayUser;
        this.isOfficialUser = that.isOfficialUser;
        this.userDescription = that.userDescription;
        this.commentsCnt = that.commentsCnt;
        this.ratingsCnt = that.ratingsCnt;
        this.wishesCnt = that.wishesCnt;
        this.bookcaseCnt = that.bookcaseCnt;
    }
}
