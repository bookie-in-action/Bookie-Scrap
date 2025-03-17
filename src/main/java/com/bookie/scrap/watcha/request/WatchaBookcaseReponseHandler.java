package com.bookie.scrap.watcha.request;

import com.bookie.scrap.common.util.ObjectMapperUtil;
import com.bookie.scrap.watcha.dto.WatchaBookcaseDTO;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Slf4j
public class WatchaBookcaseReponseHandler {

    public static HttpClientResponseHandler<List<WatchaBookcaseDTO>> create() {
        return WatchaHandlerTemplate.createTemplateWithEntity(createHandlerLogic());
    }

    public static Function<HttpEntity, List<WatchaBookcaseDTO>> createHandlerLogic() {

        return httpEntity -> {
            try {
                JsonNode resultNode = ObjectMapperUtil.readTree(EntityUtils.toString((httpEntity))).get("result").get("items").get("result");

                List<WatchaBookcaseDTO> bookcaseDetailList = new ArrayList<>();

                for(JsonNode node : resultNode) {
                    JsonNode contentNode = node.get("content");

                    if(!contentNode.isNull()){
                        WatchaBookcaseDTO watchaBookcase = ObjectMapperUtil.treeToValue(contentNode, WatchaBookcaseDTO.class);
                        bookcaseDetailList.add(watchaBookcase);
                    }
                }
                return bookcaseDetailList;

            } catch (Exception e) {
                log.error("Error Execute", e);
                throw new RuntimeException(e);
            }
        };
    }
}
