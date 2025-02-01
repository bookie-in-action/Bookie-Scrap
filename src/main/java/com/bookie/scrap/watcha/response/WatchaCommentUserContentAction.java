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

    @JsonProperty("rating")
    private String commentUserContentRating;

    @JsonProperty("status")
    private String commentUserContentStatus;

    @JsonProperty("mehed")
    private String commentUserContentMehed;

    @JsonProperty("watched_at")
    private String commentUserContentWatchedAt;
    
    @JsonProperty("user_code")
    private String commentUserContentUserCode;
    
    @JsonProperty("content_code")
    private String bookCode;

}
