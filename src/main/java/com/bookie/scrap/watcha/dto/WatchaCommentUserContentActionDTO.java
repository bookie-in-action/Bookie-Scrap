package com.bookie.scrap.watcha.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class WatchaCommentUserContentActionDTO {
    
    private String rating;
    private String status;
    private String watchedAt;
    private String userCode;
    private String contentCode;

}
