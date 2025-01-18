package com.bookie.scrap.watcha.response;

import com.bookie.scrap.watcha.dto.WatchaBookEntity;
import com.bookie.scrap.watcha.type.WatchaBookType;
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
public class WatchaBookDetail {

    private String code;
    private String title;
    private String subtitle;

    @JsonProperty("content")
    private String index;
    private String year;

    @JsonProperty("poster")
    private WatchaBookType.Poster poster;

    @JsonProperty("author_names")
    private List<String> authors;
    private List<String> nations;
    private List<String> genres;

    private String description;

    @JsonProperty("publisher_description")
    private String publisherDescription;

    @JsonProperty("author_description")
    private String authorDescription;

    @JsonProperty("ratings_avg")
    private String averageRating;

    @JsonProperty("ratings_count")
    private String ratingsCount;

    @JsonProperty("wishes_count")
    private String wishesCount;


    @JsonProperty("nations")
    protected void setNations(List<JsonNode> nationsNode) {
        this.nations = Optional.ofNullable(nationsNode)
                .orElse(Collections.emptyList())
                .stream()
                .map(node -> node.path("name").asText())
                .collect(Collectors.toList());
    }

    private List<String> externalServices;
    @Setter private Map<WatchaBookType.EXTERNAL_SERVICE, String> urlMap;

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

    public WatchaBookEntity toEntity() {
        //TODO: pk, createdAt, modifiedAt 처리
        return WatchaBookEntity.builder()
                .pk("1")
                .createdAt("11")
                .updatedAt("11")
                .code(this.code)
                .bookTitle(this.title)
                .bookSubtitle(this.subtitle)
                .bookIndex(this.index)
                .publishYear(this.year)
                .posterHd(this.poster.getHd())
                .posterXlarge(this.poster.getXlarge())
                .posterLarge(this.poster.getLarge())
                .posterMedium(this.poster.getMedium())
                .posterSmall(this.poster.getSmall())
                .authors(this.authors)
                .nations(this.nations)
                .genres(this.genres)
                .bookDescription(this.description)
                .publisherDescription(this.publisherDescription)
                .authorDescription(this.authorDescription)
                .averageRating(this.averageRating)
                .ratingsCount(this.ratingsCount)
                .wishesCount(this.wishesCount)
                .aladinUrl(this.urlMap.get(WatchaBookType.EXTERNAL_SERVICE.ALADIN))
                .yes24Url(this.urlMap.get(WatchaBookType.EXTERNAL_SERVICE.YES24))
                .kyoboUrl(this.urlMap.get(WatchaBookType.EXTERNAL_SERVICE.KYOBO))
                .build();
    }
}
