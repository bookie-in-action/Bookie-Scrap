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
            log.debug("============== Parsed WatchaBookcaseReponseHandler ==============");
            try {
                JsonNode resultNode = ObjectMapperUtil.readTree(EntityUtils.toString((httpEntity))).get("result").get("items").get("result");
                log.debug("resultNode: {}", resultNode.toString());

                List<WatchaBookcaseDTO> bookcaseDetailList = new ArrayList<>();
                log.debug("resultNode size: {}", resultNode.size());

                for(JsonNode node : resultNode) {
                    JsonNode contentNode = node.get("content");

                    log.debug("node {}", node);
                    log.debug("contentNode {}", contentNode);

                    log.debug("============== resultNode get content ==============");
                    log.debug("contentNode.isNull ? {}", contentNode.isNull());
                        if(!contentNode.isNull()){
                            log.debug("============== contentNode is Not Null ==============");

                            WatchaBookcaseDTO watchaBookcase = ObjectMapperUtil.treeToValue(contentNode, WatchaBookcaseDTO.class);
                            log.debug("Parsed bookcaseDetail: {}", watchaBookcase);

                            bookcaseDetailList.add(watchaBookcase);
                        }
                    }
                log.debug("Parsed bookcaseDetail: {}", bookcaseDetailList.toString());

                return null;

            } catch (Exception e) {
                log.error("Error Execute", e);
                throw new RuntimeException(e);
            }
        };
    }
}
