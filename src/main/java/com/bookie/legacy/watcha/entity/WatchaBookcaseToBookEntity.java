package com.bookie.legacy.watcha.entity;


import com.bookie.legacy.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "BS_WATCHA_BOOKCASE")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Builder
public class WatchaBookcaseToBookEntity extends BaseEntity {

    @Column(name = "book_code")
    private String bookCode;

    @Column(name = "bookcase_code")
    private String bookcaseCode;

//    @Column(name = "content_type")
//    private String contentType;
//
//    @Column(name = "main_title")
//    private String mainTitle;
//
//    @Column(name = "publish_year")
//    private Integer publishYear;
//
//    @Embedded
//    private WatchaType.Poster poster;
//
//    @Column(name = "background_color")
//    private String backgroundColor;
//
//    @Column(name = "average_rating")
//    private Double averageRating;
//
//    @Column(name = "authors", columnDefinition = "JSON")
//    @Convert(converter = ListStringConverter.class)
//    private List<String> authors;
//
//    @Column(name = "nations", columnDefinition = "JSON")
//    @Convert(converter = ListStringConverter.class)
//    private List<String> nations;
//
//    @Column(name = "ratings_count")
//    private Long ratingsCount;
//
//    @Column(name = "wishes_count")
//    private Long wishesCount;
//
//    @Column(name = "nations", columnDefinition = "JSON")
//    @Convert(converter = ListStringConverter.class)
//    private List<String> genres;

}
