package com.bookie.scrap.watcha.config;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.bookie.scrap.http.HttpMethod;
import com.bookie.scrap.http.HttpResponseWrapper;
import com.bookie.scrap.watcha.response.WatchaCommentDetail;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class WatchaComment extends WatchaBaseRequest<List<WatchaCommentDetail>> {

    private final String HTTP_PROTOCOL = "https";
    private final String HTTP_HOST = "pedia.watcha.com";
//     "next_uri" : "/api/contents/byLKj8M/comments?filter=all&order=popular&page=2&size=8",
    private String HTTP_ENDPOINT = "/api/contents/%s/comments?page=%d&size=%d";
    private final int HTTP_PORT = 443;
    private final HttpMethod HTTP_METHOD = HttpMethod.GET;

    {
        initHttpHost();
        setImplClassName(this.getClass().getSimpleName());
    }

    public WatchaComment(String bookCode, int page, int size) { //, String order
        try {
            log.info("bookCode: " + bookCode + ", page: " + page + ", size: " + size);
            this.HTTP_ENDPOINT = String.format(HTTP_ENDPOINT, bookCode, page, size);
            log.info("HTTP_ENDPOINT Template: " + this.HTTP_ENDPOINT);
        } catch (Exception e) {
            log.error("Error formatting HTTP_ENDPOINT: " + e.getMessage());
            throw e;
        }
        initHttpMethod();
        initCustomHandler();
    }

    private void initCustomHandler() {
        Function<HttpResponseWrapper, List<WatchaCommentDetail>> commentDetailsFunction = responseWrapper -> {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode resultNode = responseWrapper.getJsonNode().path("result").path("result");

                List<WatchaCommentDetail> commentDetails = new ArrayList<>();

                if(resultNode.isArray()){
                    for(JsonNode node : resultNode) {
                        WatchaCommentDetail commentDetail = objectMapper.treeToValue(node, WatchaCommentDetail.class);
                        commentDetails.add(commentDetail);
                        log.debug("Parsed commentDetail: {}", commentDetail);
                        log.debug("===========================================================");
                    }
                }
                return commentDetails;

            } catch (JsonProcessingException e) {
                log.error("Error parsing JSON response", e);
                throw new RuntimeException(e);
            }
        };

        setResponseHandler(commentDetailsFunction);
    }

}
