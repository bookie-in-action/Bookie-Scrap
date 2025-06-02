package com.bookie.scrap.watcha.request.userwishbook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class UserWishBookResponseDto {

    @JsonProperty("metadata")
    private Object metaData;

    private InnerResult result;

    @Getter
    public static class InnerResult {
        @JsonProperty("prev_uri")
        private String prevUri;

        @JsonProperty("next_uri")
        private String nextUri;

        @JsonProperty("result")
        private List<Object> userWishBooks;
    }

}
