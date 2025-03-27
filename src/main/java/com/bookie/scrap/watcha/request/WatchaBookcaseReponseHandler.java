package com.bookie.scrap.watcha.request;

import com.bookie.scrap.common.util.ObjectMapperUtil;
import com.bookie.scrap.watcha.dto.WatchaBookcaseToBookDTO;
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

    public static HttpClientResponseHandler<List<WatchaBookcaseToBookDTO>> create() {
        return WatchaHandlerTemplate.createTemplateWithEntity(createHandlerLogic());
    }

    public static Function<HttpEntity, List<WatchaBookcaseToBookDTO>> createHandlerLogic() {

        return httpEntity -> {
            try {
                JsonNode jsonNode = ObjectMapperUtil.readTree(EntityUtils.toString((httpEntity))).get("result");

                String bookcaseCode = jsonNode.get("next_uri").asText().split("/")[3];
                JsonNode resultNode = jsonNode.get("result");

                List<WatchaBookcaseToBookDTO> bookcaseDetailList = new ArrayList<>();

                for(JsonNode node : resultNode) {
                    JsonNode contentNode = node.get("content");

                    if(!contentNode.isNull()){
                        WatchaBookcaseToBookDTO watchaBookcase = ObjectMapperUtil.treeToValue(contentNode, WatchaBookcaseToBookDTO.class);
                        watchaBookcase.setBookcaseCode(bookcaseCode);
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
