package com.bookie.scrap.watcha.request.user.userwishbook;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

import java.util.List;

@Getter
public class UserWishBookResponseDto {

    @JsonProperty("metadata")
    private JsonNode metaData;

    private InnerResult result;

    @Getter
    public static class InnerResult {
        @JsonProperty("prev_uri")
        private String prevUri;

        @JsonProperty("next_uri")
        private String nextUri;

        private List<JsonNode> userWishBooks;

        private List<String> userWishBookCodes;

        @JsonSetter("result")
        public void getUserWishBookCodes(List<JsonNode> nodes) {
            this.userWishBooks = nodes.stream().map(node -> node.get("content")).toList();
            this.userWishBookCodes = this.userWishBooks.stream().map(node -> node.get("code").asText()).toList();
        }
    }

    public boolean hasNoData() {
        return this.getResult() == null || this.getResult().getUserWishBooks() == null || this.getResult().getUserWishBooks().isEmpty();
    }

}
