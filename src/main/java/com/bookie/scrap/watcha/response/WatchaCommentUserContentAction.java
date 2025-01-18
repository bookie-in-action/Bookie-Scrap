package com.bookie.scrap.watcha.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WatchaCommentUserContentAction {
    
    private String rating;
    private String status;

    @JsonProperty("watched_at")
    private String watchedAt;
    
    @JsonProperty("user_code")
    private String userCode;
    
    @JsonProperty("content_code")
    private String contentCode;

}
