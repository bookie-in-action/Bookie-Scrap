package com.bookie.scrap.watcha.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class WatchaCommentDetailDTO {
    
    private String code;
    private WatchaCommentUserDTO commentUser;
    private String text;
    private String likesCount;
    private String repliesCount;
    private String contentCode;
    private String userCode;
    private String watchedAt;
    private String spoiler;
    private WatchaCommentUserContentActionDTO userContentAction;
}
