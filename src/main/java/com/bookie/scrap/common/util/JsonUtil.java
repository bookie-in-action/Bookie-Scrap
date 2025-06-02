package com.bookie.scrap.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class JsonUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static Map<String, Object> toMap(JsonNode node) {
        return mapper.convertValue(node, new TypeReference<>() {});
    }

    public static String toPrettyJson(JsonNode node) throws JsonProcessingException {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
    }
}
