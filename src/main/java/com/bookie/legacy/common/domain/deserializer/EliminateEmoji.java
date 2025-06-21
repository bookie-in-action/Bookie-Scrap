package com.bookie.legacy.common.domain.deserializer;

import com.bookie.legacy.common.util.StringUtil;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class EliminateEmoji extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        String raw = jsonParser.getText();

        if (!StringUtil.hasText(raw)) {
            return "";
        }

        return StringUtil.eliminateEmoji(raw);
    }
}
