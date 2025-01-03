package com.bookie.scrap.response.watcha;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

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
    private Poster poster;

    @JsonProperty("author_names")
    private List<String> authors;
    private List<String> nations;
    private List<String> genres;

    private List<String> externalServices;

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

    @Setter private Map<TYPE, String> urlMap;

    public enum TYPE {ALADIN, YES24, KYOBO}

    @JsonProperty("nations")
    protected void setNations(List<JsonNode> nationsNode) {
        this.nations = Optional.ofNullable(nationsNode)
                .orElse(Collections.emptyList())
                .stream()
                .map(node -> node.path("name").asText())
                .collect(Collectors.toList());
    }

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

    @Getter
    @ToString
    protected static class Poster {
        private String hd;
        private String xlarge;
        private String large;
        private String medium;
        private String small;
    }

}
