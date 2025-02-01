package com.bookie.scrap.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
public class HttpResponseUtil {

    public static Header findHeader(Header[] headers, String name) {
        return Arrays.stream(headers)
                .filter(header -> name.equalsIgnoreCase(header.getName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("cannot find header [" + name + "]"));
    }

    public static void printLog(HttpEntity entity, Header[] headers, int code) throws IOException, ParseException {

        ObjectMapper objectMapper = new ObjectMapper();
        String responseBody = EntityUtils.toString(entity);
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        log.debug("=========================== HTTP RESPONSE ===========================");
        log.debug("[Response Status Code]: " + code);

        log.debug("[Response Headers]");
        Arrays.stream(headers)
                .forEach(header -> log.debug("   " + header.getName() + ": " + header.getValue()));


        try {
            log.debug("[Response Body]");
            if (jsonNode != null) {
                log.debug("\n" + jsonNode.toPrettyString());
            } else {
                log.debug("   " + responseBody);
            }

        }catch (Exception e) {
            throw new RuntimeException("   Failed to log request body: " + e.getMessage());
        }
        log.debug("================================ END ================================\n\n");

    }
}
