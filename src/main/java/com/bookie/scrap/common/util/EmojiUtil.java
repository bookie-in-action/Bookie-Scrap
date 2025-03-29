package com.bookie.scrap.common.util;

public class EmojiUtil {
    public static String eliminateEmoji(String value) {
        String regex = "[^\\p{L}\\p{N}\\p{P}\\p{Z}]";
        if (!StringUtil.hasText(value)) {
            return value;
        }

        return value.replaceAll(regex, "");
    }
}
