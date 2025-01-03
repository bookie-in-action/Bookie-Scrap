package com.bookie.scrap.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
public class HttpResponseWrapper {

    @Getter private final int code;
    @Getter private final String responseBody;
    @Getter private JsonNode jsonNode;
    @Getter private final Header[] headers;

    static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);
    }

    public HttpResponseWrapper(int code, Header[] headers, HttpEntity entity) {

        this.code = code;
        this.headers = headers;
        try {
            this.responseBody = EntityUtils.toString(entity);
        } catch (IOException | ParseException e) {
            throw new RuntimeException("error while parsing entity" + e.getMessage());
        }

        try {
            this.jsonNode = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
//            log.trace("no json involved or have trailing tokens");
        }

    }

    public Header[] findHeaders(String name) {
        return Arrays.stream(headers)
                .filter(header -> name.equalsIgnoreCase(header.getName()))
                .toArray(Header[]::new);
    }

    public Header findHeader(String name) {
        return Arrays.stream(headers)
                .filter(header -> name.equalsIgnoreCase(header.getName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("cannot find header [" + name + "]"));
    }

    public void printLog() {

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

