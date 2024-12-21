package com.bookie.scrap.response.watcha;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@ToString
@AllArgsConstructor
public class WatchaBookDetail {
    private final String bookCode;
    private final String bookTitle;
    private final String booSubtitle;
    private final String index;
    private final String year;

    private final String hdThumbnailUrl;
    private final String xLargeThumbnailUrl;
    private final String largeThumbnailUrl;
    private final String mediumThumbnailUrl;
    private final String smallThumbnailUrl;

    private final List<String> authors;
    private final List<String> nations;
    private final List<String> genres;
    private final Map<TYPE, String> externalUrls;

    private final String bookDescription;
    private final String publisherDescription;
    private final String authorDescription;

    private final String ratingsAvg;
    private final String ratingsCount;
    private final String wishesCount;

    public enum TYPE {ALADIN, YES24, KYOBO}
}
