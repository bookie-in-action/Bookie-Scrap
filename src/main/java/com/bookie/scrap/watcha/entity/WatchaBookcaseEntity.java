package com.bookie.scrap.watcha.entity;


import com.bookie.scrap.common.domain.BaseEntity;
import com.bookie.scrap.common.domain.converter.ListStringConverter;
import com.bookie.scrap.watcha.type.WatchaType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "BS_WATCHA_BOOKCASE")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Builder
public class WatchaBookcaseEntity extends BaseEntity {

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
