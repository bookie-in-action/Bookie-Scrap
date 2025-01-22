package com.bookie.scrap.watcha.dto;

import com.bookie.scrap.common.ListStringConverter;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@ToString
@Getter @Setter
@Entity @SuperBuilder
@Table(name = "BS_WATCHA_BOOK")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public final class WatchaBookEntity extends WatchaEntity{

    @Column(name = "book_code")
    private String bookCode;

    @Column(name = "book_title")
    private String bookTitle;

    @Column(name = "book_subtitle")
    private String bookSubtitle;

    @Column(name = "book_index")
    private String bookIndex;

    @Column(name = "publish_year")
    private String publishYear;

    @Column(name = "book_description")
    private String bookDescription;

    @Column(name = "publisher_description")
    private String publisherDescription;

    @Column(name = "author_description")
    private String authorDescription;

    @Column(name = "average_rating")
    private String averageRating;

    @Column(name = "ratings_count")
    private String ratingsCount;

    @Column(name = "wishes_count")
    private String wishesCount;

    @Column(name = "poster_hd")
    private String posterHd;

    @Column(name = "poster_xlarge")
    private String posterXlarge;

    @Column(name = "poster_medium")
    private String posterMedium;

    @Column(name = "poster_large")
    private String posterLarge;

    @Column(name = "poster_small")
    private String posterSmall;

    @Column(name = "authors", columnDefinition = "JSON")
    @Convert(converter = ListStringConverter.class)
    private List<String> authors;

    @Column(name = "nations", columnDefinition = "JSON")
    @Convert(converter = ListStringConverter.class)
    private List<String> nations;

    @Column(name = "genres", columnDefinition = "JSON")
    @Convert(converter = ListStringConverter.class)
    private List<String> genres;

    @Column(name = "aladin_url")
    private String aladinUrl;

    @Column(name = "yes24_url")
    private String yes24Url;

    @Column(name = "kyobo_url")
    private String kyoboUrl;

}
