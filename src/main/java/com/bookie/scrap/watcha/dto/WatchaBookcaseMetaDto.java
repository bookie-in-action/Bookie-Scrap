package com.bookie.scrap.watcha.dto;

import com.bookie.scrap.watcha.entity.WatchaBookcaseMetaEntity;
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
    private Integer bookCntInBookcase;

    @JsonProperty("likes_count")
    private Integer bookcaseLikes;

    @JsonProperty("replies_count")
    private Integer bookcaseRepliesCnt;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("updated_at")
    private String updatedAt;

    private WatchaUserDto user;

    public WatchaBookcaseMetaEntity toEntity() {
        return WatchaBookcaseMetaEntity.builder()
                .bookcaseCode(bookcaseCode)
                .bookcaseTitle(bookcaseTitle)
                .bookcaseDescription(bookcaseDescription)
                .bookCntInBookcase(bookCntInBookcase)
                .bookcaseLikes(bookcaseLikes)
                .bookcaseRepliesCnt(bookcaseRepliesCnt)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .user(user.toEntity())
                .build();
    }
}
