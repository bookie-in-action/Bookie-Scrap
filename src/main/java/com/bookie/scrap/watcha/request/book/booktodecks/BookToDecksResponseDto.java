package com.bookie.scrap.watcha.request.book.booktodecks;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
public class BookToDecksResponseDto {

    @JsonProperty("metadata")
    private JsonNode metaData;

    private InnerResult result;

    @Getter
    public static class InnerResult {
        @JsonProperty("prev_uri")
        private String prevUri;

        @JsonProperty("next_uri")
        private String nextUri;

        private List<JsonNode> decks;

        @JsonIgnore
        private List<String> deckCodes;

        @JsonSetter("result")
        public void setDecks(List<JsonNode> decks) {
            this.decks = decks;
            this.deckCodes = decks.stream()
                    .map(json -> json.get("code"))
                    .filter(Objects::nonNull)
                    .map(JsonNode::asText)
                    .collect(Collectors.toList());
        }
    }

    public boolean hasNoData() {
        return this.getResult() == null || this.getResult().getDecks() == null || this.getResult().getDecks().isEmpty();
    }
}
