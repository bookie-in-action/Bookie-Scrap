package com.bookie.scrap.watcha.entity;

import com.bookie.scrap.common.domain.BaseEntity;
import com.bookie.scrap.common.domain.Status;
import com.bookie.scrap.common.util.StringUtil;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Getter @Setter
@Entity @SuperBuilder
@Table(name = "BS_WATCHA_BOOKCASE_META")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public final class WatchaBookToBookcaseMetaEntity extends BaseEntity {

    // main pk
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

    // user는 전체 entity가 변환 되어서 들어옴 -> 별도로 저장 필요 (bookcaseMetaRepo에서 처리)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_code", referencedColumnName = "user_code", insertable = false, updatable = false)
    private WatchaUserEntity user;

    // book은 bookCode만 들어오기 때문에 해당 객체는 select 할 때만 사용 가능
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_code", referencedColumnName = "book_code", insertable = false, updatable = false)
    private WatchaBookMetaEntity bookMeta;

    public void updateEntity(WatchaBookToBookcaseMetaEntity that) {
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

    public void inActivate() {
        super.status = Status.INACTIVE;
    }

    public boolean isSame(WatchaBookToBookcaseMetaEntity that) {
        return Objects.equals(bookcaseCode, that.bookcaseCode) &&
                Objects.equals(bookcaseTitle, that.bookcaseTitle) &&
                Objects.equals(bookcaseDescription, that.bookcaseDescription) &&
                Objects.equals(bookCntInBookcase, that.bookCntInBookcase) &&
                Objects.equals(bookcaseLikes, that.bookcaseLikes) &&
                Objects.equals(bookcaseRepliesCnt, that.bookcaseRepliesCnt) &&
                Objects.equals(bookcaseCreatedAt, that.bookcaseCreatedAt) &&
                Objects.equals(bookcaseUpdatedAt, that.bookcaseUpdatedAt) &&
                Objects.equals(userCode, that.userCode);
    }

    @Override
    public String toString() {
        return "WatchaBookToBookcaseMetaEntity{" +
                "bookcaseCode='" + bookcaseCode + '\'' +
                ", userCode='" + userCode + '\'' +
                ", bookCode='" + bookCode + '\'' +
                ", bookcaseTitle='" + bookcaseTitle + '\'' +
                ", bookcaseDescription='" + bookcaseDescription + '\'' +
                ", bookCntInBookcase=" + bookCntInBookcase +
                ", bookcaseLikes=" + bookcaseLikes +
                ", bookcaseRepliesCnt=" + bookcaseRepliesCnt +
                ", bookcaseCreatedAt='" + bookcaseCreatedAt + '\'' +
                ", bookcaseUpdatedAt='" + bookcaseUpdatedAt + '\'' +
                '}';
    }
}
