package com.bookie.scrap.watcha.entity;

import com.bookie.scrap.common.domain.BaseEntity;
import com.bookie.scrap.common.domain.converter.EmojiAndSymbolConverter;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@ToString
@Getter @Setter
@Entity @SuperBuilder
@Table(name = "BS_WATCHA_BOOKCASE_META")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public final class WatchaBookcaseMetaEntity extends BaseEntity {

    @Column(name = "bookcase_code")
    private String bookcaseCode;

    @Column(name = "bookcase_title")
    private String bookcaseTitle;

    @Lob
    @Column(name = "bookcase_description", columnDefinition = "MEDIUMTEXT")
    private String bookcaseDescription;

    @Column(name = "book_cnt_in_bookcase")
    private Integer bookCntInBookcase;

    @Column(name = "bookcase_likes")
    private Integer bookcaseLikes;

    @Column(name = "bookcase_replies_cnt")
    private Integer bookcaseRepliesCnt;

    @Column(name = "bookcase_created_at")
    private String bookcaseCreatedAt;

    @Column(name = "bookcase_updated_at")
    private String bookcaseUpdatedAt;

    @Column(name = "user_code")
    private String userCode;

    @Column(name = "book_code")
    private String bookCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_code", referencedColumnName = "user_code", insertable = false, updatable = false)
    private WatchaUserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_code", referencedColumnName = "book_code", insertable = false, updatable = false)
    private WatchaBookMetaEntity bookMeta;

    public void updateEntity(WatchaBookcaseMetaEntity that) {
        this.userCode = that.userCode;
        this.bookCode = that.bookCode;
        this.bookMeta = that.bookMeta;
        this.bookcaseTitle = that.bookcaseTitle;
        this.bookcaseDescription = that.bookcaseDescription;
        this.bookcaseLikes = that.bookcaseLikes;
        this.bookcaseRepliesCnt = that.bookcaseRepliesCnt;
        this.bookcaseCreatedAt = that.bookcaseCreatedAt;
        this.bookcaseUpdatedAt = that.bookcaseUpdatedAt;
    }
}
