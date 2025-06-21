package com.bookie.legacy.common.util;

public class StringUtil {
    public static boolean hasText(String text) {
        if (text == null || text.isBlank()) {
            return false;
        }

        return true;
    }

    public static String trim(String text) {
        if (text == null) {
            return "";
        }

        return text.trim();
    }

    public static String nonNull(String text) {
        return text != null ? text.trim() : "";
    }

    public static String eliminateEmoji(String value) {
        String regex = "[^\\p{L}\\p{N}\\p{P}\\p{Z}]";
        if (!StringUtil.hasText(value)) {
            return value;
        }

        return value.replaceAll(regex, "");
    }

}
