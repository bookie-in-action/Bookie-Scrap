package com.bookie.scrap.common.util;

import com.bookie.scrap.watcha.dto.WatchaBookcaseDTO;
import com.bookie.scrap.watcha.request.WatchaBookcaseReponseHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ObjectMapperUtil {

    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> T readValue(String input, Class<T> clazz) throws IOException {
        return OBJECT_MAPPER.readValue(input, clazz);
    }

    public static <T> T readValue(String input, TypeReference<T> typeReference) throws IOException {
        return OBJECT_MAPPER.readValue(input, typeReference);
    }

    public static <T> T readValue(InputStream inputStream, Class<T> clazz) throws IOException {
        return OBJECT_MAPPER.readValue(inputStream, clazz);
    }

    public static <T> T readValue(InputStream inputStream, TypeReference<T> typeReference) throws IOException {
        return OBJECT_MAPPER.readValue(inputStream, typeReference);
    }

    public static <T> String writeValueAsString(T object) throws IOException {
        return OBJECT_MAPPER.writeValueAsString(object);
    }

    public static JsonNode readTree(String input) throws IOException {
        return OBJECT_MAPPER.readTree(input);
    }

    public static JsonNode readTree(InputStream inputStream) throws IOException {
        return OBJECT_MAPPER.readTree(inputStream);
    }

    public static <T> T treeToValue(JsonNode jsonNode, Class<T> clazz) throws IOException {
        return OBJECT_MAPPER.treeToValue(jsonNode, clazz);
    }
}