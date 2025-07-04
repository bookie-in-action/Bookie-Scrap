package com.bookie.legacy.watcha.dto;

import com.bookie.legacy.watcha.entity.WatchaBookcaseToBookEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WatchaBookcaseToBookDto {

    @JsonProperty("code")
    private String bookCode;

    @Setter @JsonIgnore
    private String bookcaseCode;

//    @JsonProperty("content_type")
//    private String contentType;
//
//    @JsonProperty("title")
//    private String mainTitle;
//
//    @JsonProperty("year")
//    private Integer publishYear;
//
//    @JsonProperty("poster")
//    private WatchaType.Poster poster;
//
//    @JsonProperty("background_color")
//    private String backgroundColor;
//
//    @JsonProperty("ratings_avg")
//    private Double averageRating;
//
//    @JsonProperty("author_names")
//    private List<String> authors;
//    private List<String> nations;
//
//    @JsonProperty("ratings_count")
//    private Long ratingsCount;
//
//    @JsonProperty("wishes_count")
//    private Long wishesCount;
//
//    @JsonProperty("genres")
//    private List<String> genres;
//
//    @JsonProperty("nations")
//    protected void setNations(List<JsonNode> nationsNode) {
//        if (nationsNode == null) {
//            this.nations = Collections.emptyList();
//            return;
//        }
//
//        this.nations = nationsNode.stream()
//                .map(node -> node.path("name").asText())
//                .collect(Collectors.toList());
//    }

    // Entity 로 변환
    public static WatchaBookcaseToBookEntity toEntity(WatchaBookcaseToBookDto dto) {
        return WatchaBookcaseToBookEntity.builder()
                .bookCode(dto.getBookCode())
                .bookcaseCode(dto.bookcaseCode)
//                .contentType(dto.getContentType())
//                .mainTitle(dto.getMainTitle())
//                .publishYear(dto.getPublishYear())
//                .poster(dto.getPoster())
//                .backgroundColor(dto.getBackgroundColor())
//                .averageRating(dto.getAverageRating())
//                .authors(dto.getAuthors())
//                .nations(dto.getNations())
//                .ratingsCount(dto.getRatingsCount())
//                .wishesCount(dto.getWishesCount())
//                .genres(dto.getGenres())
                .build();
    }
}
