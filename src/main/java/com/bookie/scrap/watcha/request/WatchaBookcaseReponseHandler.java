package com.bookie.scrap.watcha.request;

import com.bookie.scrap.common.util.ResponseHandlerMaker;
import com.bookie.scrap.http.HttpRequestExecutor;
import com.bookie.scrap.http.HttpResponseWrapper;
import com.bookie.scrap.watcha.dto.WatchaBookcaseDTO;
import com.bookie.scrap.watcha.type.WatchaBookType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class WatchaBookcaseReponseHandler {

    public static Function<HttpResponseWrapper, List<WatchaBookcaseDTO>> getHandlerLogic() {

        return responseWrapper -> {
            log.debug("============== Parsed WatchaBookcaseReponseHandler ==============");
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode resultNode = responseWrapper.getJsonNode().path("result").path("result");
                List<WatchaBookcaseDTO> bookcaseDetailList = new ArrayList<>();

                if(resultNode.isArray()){
                    log.debug("============== resultNode isArray ==============");
                    //resultNode = resultNode.getJsonNode().path("content");

                    for(JsonNode node : resultNode) {
                            JsonNode tmpNode = node.get("content");
                            log.debug("============== resultNode get content ==============");
                            //log.debug("resultNode: {}", resultNode.toString());

                            log.debug("tmpNode.isNull ? {}", tmpNode.isNull());

                            if(!tmpNode.isNull()){
                            log.debug("============== tmpNode is Not Null ==============");

                            WatchaBookcaseDTO bookcaseDetail = objectMapper.treeToValue(tmpNode, WatchaBookcaseDTO.class);
                            bookcaseDetailList.add(bookcaseDetail);

                            log.debug("Parsed bookcaseDetail: {}", bookcaseDetail);
                            }
                        //log.debug("===========================================================");

                    }
                }
//
//                log.debug("=> Start searching for External Service URL [{}/{}]", bookcaseDetail.getBookCode(), bookcaseDetail.getMainTitle());
//                List<String> redirectUrls = bookcaseDetail.getExternalServices().stream()
//                        .map(WatchaBookcaseReponseHandler::fetchWatchaRedirectUrl).collect(Collectors.toList());
//                bookcaseDetail.setUrlMap(mapExternalUrlsToTypes(redirectUrls));
//                log.debug("<= End searching for External Service URL");

                log.debug("return bookcaseDetailList: {}", bookcaseDetailList);

                return bookcaseDetailList;

            } catch (JsonProcessingException e) {
                log.error("Error parsing JSON response", e);
                throw new RuntimeException(e);
            }
        };

    }
}
