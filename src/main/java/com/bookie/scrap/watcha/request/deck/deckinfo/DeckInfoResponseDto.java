package com.bookie.scrap.watcha.request.deck.deckinfo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

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

        @Override
        public String toString() {
            return "DeckInfoResponseDto.InnerResult{" +
                    "updated_at='" + updated_at + '\'' +
                    ", created_at='" + created_at + '\'' +
                    ", replies_count='" + replies_count + '\'' +
                    ", likes_count='" + likes_count + '\'' +
                    ", contents_count='" + contents_count + '\'' +
                    ", description='" + description + '\'' +
                    ", title='" + title + '\'' +
                    ", deckCode='" + deckCode + '\'' +
                    '}';
        }
    }

    public boolean hasNoData() {
        return this.getResult() == null;
    }
}
