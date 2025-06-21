package com.bookie.legacy.watcha.request;

import com.bookie.legacy.common.util.ObjectMapperUtil;
import com.bookie.legacy.watcha.dto.WatchaBookcaseMetaDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@Slf4j
public class WatchaBookToBookcaseMetasResponseHandler {


    public static HttpClientResponseHandler<List<WatchaBookcaseMetaDto>> create(String bookCode) {
        return WatchaHandlerTemplate.createTemplateWithEntity(createHandlerLogic(bookCode));
    }

    private static Function<HttpEntity, List<WatchaBookcaseMetaDto>> createHandlerLogic(String bookCode) {


        return httpEntity -> {

            try {
                JsonNode jsonNode = ObjectMapperUtil.readTree(httpEntity.getContent()).path("result");
                JsonNode resultNode = jsonNode.path("result");

                if (jsonNode.isEmpty()) {
                    return Collections.emptyList();
                }

                List<WatchaBookcaseMetaDto> watchaBookcaseMetaDtoList = ObjectMapperUtil.parseListFromTree(resultNode, WatchaBookcaseMetaDto.class);
                ;
                watchaBookcaseMetaDtoList.stream().forEach(dto -> {
                    dto.setBookCode(bookCode);
                });

                log.trace("Parsed BookcaseMeta: {}", watchaBookcaseMetaDtoList.size());

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
