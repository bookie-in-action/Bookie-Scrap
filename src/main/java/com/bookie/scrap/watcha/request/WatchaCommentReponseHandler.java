package com.bookie.scrap.watcha.request;

import com.bookie.scrap.common.util.ObjectMapperUtil;
import com.bookie.scrap.watcha.dto.WatchaCommentDetailDTO;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@Slf4j
public class WatchaCommentReponseHandler {

    public static HttpClientResponseHandler<List<WatchaCommentDetailDTO>> create() {
        return WatchaHandlerTemplate.createTemplateWithEntity(createHandlerLogic());
    }

    public static Function<HttpEntity, List<WatchaCommentDetailDTO>> createHandlerLogic() {

        return httpEntity -> {
            try {
                //JsonNode jsonNode = ObjectMapperUtil.readTree()
                JsonNode jsonNode = ObjectMapperUtil.readTree(EntityUtils.toString((httpEntity))).path("result");
                JsonNode resultNode = jsonNode.path("result");
                log.debug("Response jsonNode -> {}", resultNode.toString() );
                ///JsonNode resultNode = ObjectMapperUtil.readTree(EntityUtils.toString((httpEntity))).get("result").get("result");

                List<WatchaCommentDetailDTO> commentDetailList = new ArrayList<>();

                if(!resultNode.isNull()){
                    for(JsonNode node : resultNode) {
                        WatchaCommentDetailDTO watchaCommentDetail = ObjectMapperUtil.treeToValue(node, WatchaCommentDetailDTO.class);
                        commentDetailList.add (watchaCommentDetail);
                    }
                }

//                List<WatchaCommentDetailDTO> watchaCommentDetailDTOList = ObjectMapperUtil.parseListFromTree(resultNode, WatchaCommentDetailDTO.class);
//                log.debug("Response watchaCommentDetailDTOList -> {}", watchaCommentDetailDTOList.toString() );
                log.debug("Response watchaCommentDetailDTOList -> {}", commentDetailList );

                return commentDetailList;
//                return Collections.emptyList(); // 또는 List.of();
            } catch (Exception e){
                log.error("Error Execute", e);
                throw new RuntimeException(e);
            }
        };
    }
}
