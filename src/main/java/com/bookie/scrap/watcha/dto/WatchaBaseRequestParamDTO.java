package com.bookie.scrap.watcha.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.net.URIBuilder;

import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Getter
@Setter
public class WatchaBaseRequestParamDTO {

    String bookCode;
    String page;
    String size;

    public WatchaBaseRequestParamDTO(String bookCode, String page, String size) {
        this.bookCode = bookCode;
        this.page = page;
        this.size = size;
    }

    public String buildUrl(String baseUrl) {
        try {
            return new URIBuilder(baseUrl + "/" + URLEncoder.encode(bookCode, StandardCharsets.UTF_8))
                    .addParameter("page", page)
                    .addParameter("size", size)
                    .build()
                    .toString();
        } catch (URISyntaxException e) {
            log.error("Invalid URL : " + e.getMessage());
            return null;
        }
    }
}
