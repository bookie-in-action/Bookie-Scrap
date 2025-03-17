package com.bookie.scrap.watcha.dto;

import com.bookie.scrap.watcha.type.WatchaBookType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WatchaBookcaseDTO {

    @JsonProperty("code")
    private String bookCode;

    @JsonProperty("content_type")
    private String contentType;

    @JsonProperty("title")
    private String mainTitle;

    @JsonProperty("year")
    private Integer publishYear;

    @JsonProperty("poster")
    private WatchaBookType.Poster poster;

    @JsonProperty("background_color")
    private String backgroundColor;

    @JsonProperty("ratings_avg")
    private Double averageRating;

    @JsonProperty("author_names")
    private List<String> authors;
    private List<String> nations;

    @JsonProperty("ratings_count")
    private Long ratingsCount;

    @JsonProperty("wishes_count")
    private Long wishesCount;

    @JsonProperty("genres")
    private List<String> genres;

    @JsonProperty("nations")
    protected void setNations(List<JsonNode> nationsNode) {
        this.nations = Optional.ofNullable(nationsNode)
                .orElse(Collections.emptyList())
                .stream()
                .map(node -> node.path("name").asText())
                .collect(Collectors.toList());
    }

}
