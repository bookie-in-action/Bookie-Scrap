package com.bookie.scrap.watcha.request.book.bookmeta.rdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookMetaRdbDto {

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
    private RdbBookPoster bookPoster;

    @JsonProperty("author_names")
    private List<String> bookAuthors;

    @JsonProperty("nations")
    private List<String> nations;

    @JsonProperty("genres")
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
    private Map<RdbExternalService, String> externalServiceUrlMap = Map.of(
            RdbExternalService.KYOBO, "",
            RdbExternalService.ALADIN, "",
            RdbExternalService.YES24, ""
    );

    @JsonProperty("external_services")
    protected void setExternalServices(List<JsonNode> externalServicesNode) {
        for (JsonNode node : externalServicesNode) {
            String externalId = node.path("id").toString();
            String externalLink = node.path("href").toString();

            switch (externalId) {
                case "aladdin":
                    externalServiceUrlMap.put(RdbExternalService.ALADIN, externalLink);
                    break;
                case "yes24":
                    externalServiceUrlMap.put(RdbExternalService.YES24, externalLink);
                    break;
                case "kyobobook":
                    externalServiceUrlMap.put(RdbExternalService.KYOBO, externalLink);
                    break;
            }
        }

//        this.externalServices = Optional.ofNullable(externalServicesNode)
//                .orElse(Collections.emptyList())
//                .stream()
//                .map(node -> {
//                    String href = node.path("href").asText();
//                    String[] splitUrl = href.split("/");
//
//                    return String.format("https://redirect.watcha.com/galaxy/%s", splitUrl[4]);
//                }).collect(Collectors.toList());
    }

    public BookMetaRdbEntity toEntity() {
        return BookMetaRdbEntity.builder()
                .bookCode(this.bookCode)
                .bookDescription(this.bookDescription)
                .bookTitle(this.mainTitle)
                .bookSubtitle(this.subtitle)
                .bookIndex(this.bookIndex)
                .publishYear(this.publishYear)
                .bookPoster(this.bookPoster)
                .authors(this.bookAuthors.toString())
                .nations(this.nations.toString())
                .genres(this.bookGenres.toString())
                .bookDescription(this.bookDescription)
                .publisherDescription(this.publisherDescription)
                .authorDescription(this.authorDescription)
                .averageRating(this.averageRating)
                .ratingsCount(this.ratingsCount)
                .wishesCount(this.wishesCount)
                .aladinUrl(this.externalServiceUrlMap.get(RdbExternalService.ALADIN))
                .yes24Url(this.externalServiceUrlMap.get(RdbExternalService.YES24))
                .kyoboUrl(this.externalServiceUrlMap.get(RdbExternalService.KYOBO))
                .build();
    }
}
