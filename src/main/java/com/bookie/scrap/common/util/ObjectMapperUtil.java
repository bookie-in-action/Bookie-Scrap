package com.bookie.scrap.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

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

}