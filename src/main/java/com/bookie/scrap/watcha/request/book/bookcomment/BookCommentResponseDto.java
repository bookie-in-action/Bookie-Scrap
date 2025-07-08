package com.bookie.scrap.watcha.request.book.bookcomment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

import java.util.List;

@Getter
public class BookCommentResponseDto {

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
        private List<JsonNode> comments;

        @JsonIgnore
        private List<String> userCodes;

        @JsonSetter
        public void setComments(List<JsonNode> comments) {
            this.comments = comments;
            this.userCodes = comments.stream()
                    .map(json -> json.get("user").get("code"))
                    .map(JsonNode::asText)
                    .toList();
        }
    }

    public boolean hasNoData() {
        return this.getResult() == null || this.getResult().getComments() == null || this.getResult().getComments().isEmpty();
    }
}
