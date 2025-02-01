package com.bookie.scrap.watcha.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WatchaCommentUserPhoto {

    @JsonProperty("original")
    private String commentUserPhotoOriginal;

    @JsonProperty("large")
    private String commentUserPhotoLarge;

    @JsonProperty("small")
    private String commentUserPhotoSmall;
}
