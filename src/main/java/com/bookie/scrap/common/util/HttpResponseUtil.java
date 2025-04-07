package com.bookie.scrap.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
public class HttpResponseUtil {

    public static Optional<Header> findHeader(Header[] headers, String name) {
        return Arrays.stream(headers)
                .filter(header -> name.equalsIgnoreCase(header.getName()))
                .findFirst();
    }

    public static void printLog(HttpEntity entity, Header[] headers, int code){



        log.debug("=========================== HTTP RESPONSE ===========================");
        log.debug("[Response Status Code]: " + code);

        log.debug("[Response Headers]");
        Arrays.stream(headers)
                .forEach(header -> log.debug("   " + header.getName() + ": " + header.getValue()));


        try {

            Optional<Header> contentTypeOpt = HttpResponseUtil.findHeader(headers, "Content-Type");

            log.debug("[Response Body]");
            if (contentTypeOpt.isPresent() && contentTypeOpt.get().getValue().contains("application/json")) {
                JsonNode jsonNode = ObjectMapperUtil.readTree(EntityUtils.toString(entity));
                log.debug("\n" + jsonNode.toPrettyString());
            } else {
                log.debug(EntityUtils.toString(entity));
                log.debug("   not a json Response");
            }

        }catch (Exception e) {
            throw new RuntimeException("   Failed to log request body: " + e.getMessage());
        }
        log.debug("================================ END ================================\n\n");

    }
}
