package com.bookie.scrap.watcha.request.user.userlikepeople;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

import java.util.List;

@Getter
public class UserLikePeopleResponseDto {

    @JsonProperty("metadata")
    private JsonNode metaData;

    private InnerResult result;

    @Getter
    public static class InnerResult {
        @JsonProperty("prev_uri")
        private String prevUri;

        @JsonProperty("next_uri")
        private String nextUri;

        @JsonProperty("result")
        private List<JsonNode> userLikePeople;
    }

    public boolean hasNoData() {
        return this.getResult() == null || this.getResult().getUserLikePeople() == null || this.getResult().getUserLikePeople().isEmpty();
    }

}
