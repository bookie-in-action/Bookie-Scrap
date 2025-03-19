package com.bookie.scrap.watcha.domain;

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
@Builder
public class PageInfo {

    private String page;
    private String size;
    private String items;
    private String filter;
    private String order;

    public String buildUrl(String baseUrl) {
        // 필수 값 검증
        if (baseUrl == null || baseUrl.isBlank()) {
            throw new IllegalArgumentException("Base URL must not be null or empty.");
        }
//        if (bookCode == null || bookCode.isBlank()) {
//            throw new IllegalArgumentException("Book Code parameter must not be null or empty.");
//        }
//        if (page != null || !page.isBlank()) {
//            throw new IllegalArgumentException("Page parameter must not be null or empty.");
//        }
//        if (size == null || size.isBlank()) {
//            throw new IllegalArgumentException("Size parameter must not be null or empty.");
//        }

        URIBuilder uriBuilder;
        try {
//            return new URIBuilder(baseUrl + "/" + URLEncoder.encode(bookCode, StandardCharsets.UTF_8))
//                    .addParameter("page", page)
//                    .addParameter("size", size)
//                    .build()
//                    .toString();

            uriBuilder = new URIBuilder(baseUrl);

            if(page != null && !page.isEmpty()){
                uriBuilder.addParameter("page", page);
            }
            if(size != null && !size.isEmpty()){
                uriBuilder.addParameter("size", size);
            }
            if(items != null && !items.isEmpty()){
                uriBuilder.addParameter("items", items);
            }
            if(filter != null && !filter.isEmpty()){
                uriBuilder.addParameter("filter", filter);
            }
            if(order != null && !order.isEmpty()){
                uriBuilder.addParameter("order", order);
            }

            uriBuilder.build();
        } catch (URISyntaxException e) {
            log.error("Invalid URL : {}", e.getMessage());
            throw new RuntimeException(e);
        }
        return uriBuilder.toString();
    }
}
