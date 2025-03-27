package com.bookie.scrap.common.domain.converter;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EmojiAndSymbolConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) return null;

        String regex = "[^\\p{L}\\p{N}\\p{P}\\p{Z}]";
        return attribute.replaceAll(regex, "");
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return dbData;
    }
}
