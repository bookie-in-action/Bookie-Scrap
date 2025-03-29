package com.bookie.scrap.watcha.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class WatchaCommentUserContentActionDTO {

    @JsonProperty("rating")
    private String rating;

    @JsonProperty("status")
    private String status;

    @JsonProperty("mehed")
    private String mehed;

    @JsonProperty("watched_at")
    private String watchedAt;

    @JsonProperty("user_code")
    private String userCode;

    @JsonProperty("content_code")
    private String contentCode;

}
