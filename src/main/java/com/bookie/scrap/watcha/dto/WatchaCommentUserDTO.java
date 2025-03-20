package com.bookie.scrap.watcha.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class WatchaCommentUserDTO {
    
    private String code;
    private String name;
    private String watchaPlayUser;
    private String officialUser;

}
