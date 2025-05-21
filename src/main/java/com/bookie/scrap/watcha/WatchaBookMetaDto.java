package com.bookie.scrap.watcha;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.StringUtils;

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
    private WatchaBookPoster bookPoster;

    @JsonProperty("author_names")
    private List<String> bookAuthors;

    private List<String> nations;

    private List<String> bookGenres;

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
                .bookPoster(this.bookPoster)
                .authors(String.join(",", this.bookAuthors))
                .nations(String.join(",", this.nations))
                .genres(String.join(",", this.getBookGenres()))
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
