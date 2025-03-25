package com.bookie.scrap.common.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Converter
public class ListStringConverter implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(List<String> items) {
        if (items == null || items.isEmpty()) return "";
        return String.join(",", items);
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return new ArrayList<>();
        }

        return Arrays.asList(dbData.split(","));
    }
}

