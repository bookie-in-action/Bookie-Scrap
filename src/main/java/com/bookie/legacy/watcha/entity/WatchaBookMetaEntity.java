package com.bookie.legacy.watcha.entity;

import com.bookie.legacy.common.domain.BaseEntity;
import com.bookie.legacy.common.domain.converter.ListStringConverter;
import com.bookie.legacy.watcha.type.WatchaBookPoster;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@ToString
@Getter @Setter
@Entity @Builder
@AllArgsConstructor
@Table(name = "BS_WATCHA_BOOK_META")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public final class WatchaBookMetaEntity extends BaseEntity {

    @Column(name = "book_code")
    private String bookCode;
    @Column(name = "book_title")
    private String bookTitle;

    @Column(name = "book_subtitle")
    private String bookSubtitle;

    @Column(name = "book_index", columnDefinition = "MEDIUMTEXT")
    private String bookIndex;

    @Column(name = "publish_year")
    private Integer publishYear;

    @Lob
    @Column(name = "book_description", columnDefinition = "MEDIUMTEXT")
    private String bookDescription;

    @Lob
    @Column(name = "publisher_description", columnDefinition = "MEDIUMTEXT")
    private String publisherDescription;

    @Lob
    @Column(name = "author_description", columnDefinition = "MEDIUMTEXT")
    private String authorDescription;

    @Column(name = "average_rating")
    private Double averageRating;

    @Column(name = "ratings_count", columnDefinition = "INT UNSIGNED")
    private Long ratingsCount;

    @Column(name = "wishes_count", columnDefinition = "INT UNSIGNED")
    private Long wishesCount;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "hd", column = @Column(name = "poster_hd")),
            @AttributeOverride(name = "xlarge", column = @Column(name = "poster_xlarge")),
            @AttributeOverride(name = "large", column = @Column(name = "poster_large")),
            @AttributeOverride(name = "medium", column = @Column(name = "poster_medium")),
            @AttributeOverride(name = "small", column = @Column(name = "poster_small"))
    })
    private WatchaBookPoster bookPoster;

    @Column(name = "authors")
    @Convert(converter = ListStringConverter.class)
    private List<String> authors;

    @Column(name = "nations")
    @Convert(converter = ListStringConverter.class)
    private List<String> nations;

    @Column(name = "genres")
    @Convert(converter = ListStringConverter.class)
    private List<String> genres;

    @Column(name = "aladin_url")
    private String aladinUrl;

    @Column(name = "yes24_url")
    private String yes24Url;

    @Column(name = "kyobo_url")
    private String kyoboUrl;

    public void updateEntity(WatchaBookMetaEntity that) {
        this.bookSubtitle = that.bookSubtitle;
        this.bookIndex = that.bookIndex;
        this.publishYear = that.publishYear;
        this.bookDescription = that.bookDescription;
        this.publisherDescription = that.publisherDescription;
        this.authorDescription = that.authorDescription;
        this.averageRating = that.averageRating;
        this.ratingsCount = that.ratingsCount;
        this.wishesCount = that.wishesCount;
        this.bookPoster = that.bookPoster;
        this.authors = that.authors;
        this.nations = that.nations;
        this.genres = that.genres;
        this.aladinUrl = that.aladinUrl;
        this.yes24Url = that.yes24Url;
        this.kyoboUrl = that.kyoboUrl;
    }

}
