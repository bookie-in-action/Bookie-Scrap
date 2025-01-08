package com.bookie.scrap.watcha.dto;

import com.bookie.scrap.watcha.type.WatchaBookType;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

@Getter
@ToString
@SuperBuilder
public class WatchaBookEntity extends WatchaEntity{

    // TODO: 엔티티
    private String title;
    private String subtitle;
    private String index;
    private String year;
    private WatchaBookType.Poster poster;
    private List<String> authors;
    private List<String> nations;
    private List<String> genres;
    private String description;
    private String publisherDescription;
    private String authorDescription;
    private String averageRating;
    private String ratingsCount;
    private String wishesCount;
    private Map<WatchaBookType.EXTERNAL_SERVICE, String> urlMap;

}
