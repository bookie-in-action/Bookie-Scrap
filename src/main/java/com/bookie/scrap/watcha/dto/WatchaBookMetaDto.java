package com.bookie.scrap.watcha.dto;

import com.bookie.scrap.watcha.entity.WatchaBookMetaEntity;
import com.bookie.scrap.watcha.type.WatchaExternalService;
import com.bookie.scrap.watcha.type.WatchaPoster;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WatchaBookMetaDto {

    @JsonProperty("code")
    private String bookCode;

    @JsonProperty("title")
    private String mainTitle;
    private String subtitle;

    @JsonProperty("content")
    private String bookIndex;

    @JsonProperty("year")
    private Integer publishYear;

    @JsonProperty("poster")
    private WatchaPoster poster;

    @JsonProperty("author_names")
    private List<String> authors;
    private List<String> nations;
    private List<String> genres;

    @JsonProperty("description")
    private String bookDescription;

    @JsonProperty("publisher_description")
    private String publisherDescription;

    @JsonProperty("author_description")
    private String authorDescription;

    @JsonProperty("ratings_avg")
    private Double averageRating;

    @JsonProperty("ratings_count")
    private Long ratingsCount;

    @JsonProperty("wishes_count")
    private Long wishesCount;


    @JsonProperty("nations")
    protected void setNations(List<JsonNode> nationsNode) {
        this.nations = Optional.ofNullable(nationsNode)
                .orElse(Collections.emptyList())
                .stream()
                .map(node -> node.path("name").asText())
                .collect(Collectors.toList());
    }

    private List<String> externalServices;
    @Setter private Map<WatchaExternalService, String> urlMap;

    @JsonProperty("external_services")
    protected void setExternalServices(List<JsonNode> externalServicesNode) {
        this.externalServices = Optional.ofNullable(externalServicesNode)
                .orElse(Collections.emptyList())
                .stream()
                .map(node -> {
                    String href = node.path("href").asText();
                    String[] splitUrl = href.split("/");

                    return String.format("https://redirect.watcha.com/galaxy/%s", splitUrl[4]);
                }).collect(Collectors.toList());
    }

    public WatchaBookMetaEntity toEntity() {
        return WatchaBookMetaEntity.builder()
                .bookCode(this.bookCode)
                .bookDescription(this.bookDescription)
                .bookTitle(this.mainTitle)
                .bookSubtitle(this.subtitle)
                .bookIndex(this.bookIndex)
                .publishYear(this.publishYear)
                .posterHd(this.poster.getHd())
                .posterXlarge(this.poster.getXlarge())
                .posterLarge(this.poster.getLarge())
                .posterMedium(this.poster.getMedium())
                .posterSmall(this.poster.getSmall())
                .authors(this.authors)
                .nations(this.nations)
                .genres(this.genres)
                .bookDescription(this.bookDescription)
                .publisherDescription(this.publisherDescription)
                .authorDescription(this.authorDescription)
                .averageRating(this.averageRating)
                .ratingsCount(this.ratingsCount)
                .wishesCount(this.wishesCount)
                .aladinUrl(this.urlMap.get(WatchaExternalService.ALADIN))
                .yes24Url(this.urlMap.get(WatchaExternalService.YES24))
                .kyoboUrl(this.urlMap.get(WatchaExternalService.KYOBO))
                .build();
    }
}
