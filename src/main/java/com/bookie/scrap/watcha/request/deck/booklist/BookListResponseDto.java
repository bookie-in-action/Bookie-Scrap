package com.bookie.scrap.watcha.request.deck.booklist;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
public class BookListResponseDto {

    @JsonProperty("metadata")
    private JsonNode metaData;

    private InnerResult result;

    @Getter
    public static class InnerResult {
        @JsonProperty("prev_uri")
        private String prevUri;

        @JsonProperty("next_uri")
        private String nextUri;

        private List<JsonNode> books;

        @JsonIgnore
        private List<String> bookCodes;

        @JsonSetter("result")
        public void setBooks(List<JsonNode> books) {
            this.books = books;
            this.bookCodes = books.stream()
                    .map(json -> json.get("content").get("code"))
                    .filter(Objects::nonNull)
                    .map(JsonNode::asText)
                    .collect(Collectors.toList());
        }
    }
}
