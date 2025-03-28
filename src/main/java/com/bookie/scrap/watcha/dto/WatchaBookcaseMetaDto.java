package com.bookie.scrap.watcha.dto;

import com.bookie.scrap.common.util.EmojiUtil;
import com.bookie.scrap.common.util.StringUtil;
import com.bookie.scrap.watcha.entity.WatchaBookToBookcaseMetaEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
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

    @Setter @JsonIgnore
    private String bookCode;

    public WatchaBookToBookcaseMetaEntity toEntity() {
        return WatchaBookToBookcaseMetaEntity.builder()
                .bookCode(StringUtil.trim(bookCode))
                .userCode(StringUtil.trim(user.getUserCode()))
                .bookcaseCode(StringUtil.trim(bookcaseCode))
                .bookcaseTitle(StringUtil.trim(EmojiUtil.eliminateEmoji(bookcaseTitle)))
                .bookcaseDescription(StringUtil.trim(EmojiUtil.eliminateEmoji(bookcaseDescription)))
                .bookCntInBookcase(bookCntInBookcase)
                .bookcaseLikes(bookcaseLikes)
                .bookcaseRepliesCnt(bookcaseRepliesCnt)
                .bookcaseCreatedAt(createdAt.replace("+09:00", ""))
                .bookcaseUpdatedAt(updatedAt.replace("+09:00", ""))
                .user(user.toEntity())
                .build();
    }


}
