package com.bookie.scrap.watcha.entity;

import com.bookie.scrap.common.domain.BaseEntity;
import com.bookie.scrap.common.util.ListStringConverter;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

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
    private String createdAt;

    @Column(name = "bookcase_updated_at")
    private String updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_code", referencedColumnName = "user_code")
    private WatchaUserEntity user;

}
