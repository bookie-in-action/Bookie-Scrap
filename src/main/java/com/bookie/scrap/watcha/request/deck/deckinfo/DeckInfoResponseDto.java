package com.bookie.scrap.watcha.request.deck.deckinfo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
public class DeckInfoResponseDto {

    @JsonProperty("metadata")
    private JsonNode metaData;

    private InnerResult result;

    @Getter
    public static class InnerResult {

        @JsonProperty("code")
        private String deckCode;

        private String title;

        private String description;

        private String contents_count;

        private String likes_count;

        private String replies_count;

        private String created_at;

        private String updated_at;

        @JsonIgnore
        private String userCode;

        @JsonSetter("user")
        public void setUser(JsonNode user) {
            this.userCode = user.get("code").asText();
        }

    }
}
