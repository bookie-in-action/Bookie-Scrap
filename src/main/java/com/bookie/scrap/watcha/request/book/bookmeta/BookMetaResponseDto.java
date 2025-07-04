package com.bookie.scrap.watcha.request.book.bookmeta;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

import java.util.List;

@Getter
public class BookMetaResponseDto {

    @JsonProperty("metadata")
    private JsonNode metaData;

    @JsonProperty("result")
    private JsonNode bookMeta;

    @JsonProperty("result")
    private void setBookMeta(JsonNode result) {
        this.bookMeta = result;
        this.contentType = result.get("content_type").toString();
    }

    @JsonIgnore
    private String contentType;

    public boolean isBook() {
        if (contentType.equals("books")) {
            return true;
        }
        return false;
    }

}
