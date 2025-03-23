package com.bookie.scrap.watcha.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WatchaBookcaseMetaDto {

    @JsonProperty("code")
    private String bookcaseCode;

    @JsonProperty("title")
    private String bookcaseTitle;

    @JsonProperty("description")
    private String bookcaseDescription;

    @JsonProperty("contents_count")
    private String bookCntInBookcase;

    @JsonProperty("likes_count")
    private Integer bookcaseLikes;

    @JsonProperty("replies_count")
    private Integer bookcaseRepliesCnt;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("updated_at")
    private String updatedAt;

    private WatchaUser user;

}
