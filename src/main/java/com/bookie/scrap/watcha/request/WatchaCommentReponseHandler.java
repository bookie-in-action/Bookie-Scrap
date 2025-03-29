package com.bookie.scrap.watcha.request;

import com.bookie.scrap.common.util.ObjectMapperUtil;
import com.bookie.scrap.watcha.dto.WatchaCommentDetailDto;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Slf4j
public class WatchaCommentReponseHandler {

    public static HttpClientResponseHandler<List<WatchaCommentDetailDto>> create() {
        return WatchaHandlerTemplate.createTemplateWithEntity(createHandlerLogic());
    }

    public static Function<HttpEntity, List<WatchaCommentDetailDto>> createHandlerLogic() {

        return httpEntity -> {
            try {
                JsonNode jsonNode = ObjectMapperUtil.readTree(httpEntity.getContent()).path("result");
                JsonNode resultNode = jsonNode.path("result");
                log.debug("Response jsonNode -> {}", resultNode.toString() );

                List<WatchaCommentDetailDto> commentDetailList = new ArrayList<>();

                if(!resultNode.isNull()){
                    for(JsonNode node : resultNode) {
                        WatchaCommentDetailDto watchaCommentDetail = ObjectMapperUtil.treeToValue(node, WatchaCommentDetailDto.class);
                        commentDetailList.add (watchaCommentDetail);
                    }
                }

                log.debug("Response watchaCommentDetailDTOList -> {}", commentDetailList );

                return commentDetailList;
            } catch (Exception e){
                log.error("Error Execute", e);
                throw new RuntimeException(e);
            }
        };
    }
}
