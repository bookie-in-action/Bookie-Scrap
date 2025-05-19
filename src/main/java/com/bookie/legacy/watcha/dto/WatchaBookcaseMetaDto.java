package com.bookie.legacy.watcha.dto;

import com.bookie.legacy.common.domain.deserializer.EliminateEmoji;
import com.bookie.legacy.common.util.StringUtil;
import com.bookie.legacy.watcha.domain.deserializer.WatchaParseDateTimeDeserializer;
import com.bookie.legacy.watcha.entity.WatchaBookToBookcaseMetaEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WatchaBookcaseMetaDto {

    @JsonProperty("code")
    private String bookcaseCode;

    @JsonProperty("title")
    @JsonDeserialize(using = EliminateEmoji.class)
    private String bookcaseTitle;

    @JsonProperty("description")
    @JsonDeserialize(using = EliminateEmoji.class)
    private String bookcaseDescription;

    @JsonProperty("contents_count")
    private Integer bookCntInBookcase;

    @JsonProperty("likes_count")
    private Integer bookcaseLikes;

    @JsonProperty("replies_count")
    private Integer bookcaseRepliesCnt;

    @JsonProperty("created_at")
    @JsonDeserialize(using = WatchaParseDateTimeDeserializer.class)
    private String bookcaseCreatedAt;

    @JsonProperty("updated_at")
    @JsonDeserialize(using = WatchaParseDateTimeDeserializer.class)
    private String bookcaseUpdatedAt;

    private WatchaUserDto user;

    @Setter @JsonIgnore
    private String bookCode;

    public WatchaBookToBookcaseMetaEntity toEntity() {
        return WatchaBookToBookcaseMetaEntity.builder()
                .bookCode(StringUtil.nonNull(bookCode))
                .userCode(StringUtil.nonNull(user.getUserCode()))
                .bookcaseCode(StringUtil.nonNull(bookcaseCode))
                .bookcaseTitle(StringUtil.nonNull(bookcaseTitle))
                .bookcaseDescription(StringUtil.nonNull(bookcaseDescription))
                .bookCntInBookcase(bookCntInBookcase)
                .bookcaseLikes(bookcaseLikes)
                .bookcaseRepliesCnt(bookcaseRepliesCnt)
                .bookcaseCreatedAt(bookcaseCreatedAt)
                .bookcaseUpdatedAt(bookcaseUpdatedAt)
                .user(user.toEntity())
                .build();
    }


}
