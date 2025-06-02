package com.bookie.legacy.common.domain.converter;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
class EmojiAndSymbolConverterTest {

    @Test
    void convertToDatabaseColumn() {
        String attribute = "정말 유명해서 한번쯤은 읽어볼만한 책들\uD83D\uDCD5";
        String regex = "[^\\p{L}\\p{N}\\p{P}\\p{Z}]";
        String str = attribute.replaceAll(regex, "");
        System.out.println(str);
    }
}