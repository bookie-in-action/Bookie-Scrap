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



        log.trace("=========================== HTTP RESPONSE ===========================");
        log.trace("[Response Status Code]: " + code);

        log.trace("[Response Headers]");
        Arrays.stream(headers)
                .forEach(header -> log.trace("   " + header.getName() + ": " + header.getValue()));


        try {

            Optional<Header> contentTypeOpt = HttpResponseUtil.findHeader(headers, "Content-Type");

            log.trace("[Response Body]");
            if (contentTypeOpt.isPresent() && contentTypeOpt.get().getValue().contains("application/json")) {
                JsonNode jsonNode = ObjectMapperUtil.readTree(EntityUtils.toString(entity));
                log.trace("\n" + jsonNode.toPrettyString());
            } else {
                log.trace("   not a json Response");
            }

        }catch (Exception e) {
            throw new RuntimeException("   Failed to log request body: " + e.getMessage());
        }
        log.trace("================================ END ================================\n\n");

    }
}
