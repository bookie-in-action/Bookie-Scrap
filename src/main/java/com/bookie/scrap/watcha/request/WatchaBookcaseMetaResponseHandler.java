package com.bookie.scrap.watcha.request;

import com.bookie.scrap.common.util.ObjectMapperUtil;
import com.bookie.scrap.watcha.dto.WatchaBookcaseMetaDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@Slf4j
public class WatchaBookcaseMetaResponseHandler {


    public static HttpClientResponseHandler<List<WatchaBookcaseMetaDto>> create() {
        return WatchaHandlerTemplate.createTemplateWithEntity(createHandlerLogic());
    }

    private static Function<HttpEntity, List<WatchaBookcaseMetaDto>> createHandlerLogic() {


        return httpEntity -> {

            try {
                JsonNode jsonNode = ObjectMapperUtil.readTree(httpEntity.getContent()).path("result");
                JsonNode resultNode = jsonNode.path("result");

                if (resultNode.isEmpty()) {
                    return Collections.emptyList();
                }

                List<WatchaBookcaseMetaDto> watchaBookcaseMetaDtoList = ObjectMapperUtil.parseListFromTree(resultNode, WatchaBookcaseMetaDto.class);

                String bookCode = jsonNode.path("next_uri").asText().split("/")[3];
                watchaBookcaseMetaDtoList.stream().forEach(dto -> dto.setBookCode(bookCode));

                log.debug("Parsed BookcaseMeta: {}", watchaBookcaseMetaDtoList);

                return watchaBookcaseMetaDtoList;

            } catch (JsonProcessingException e) {
                log.error("Error parsing JSON response", e);
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        };
    }

}
