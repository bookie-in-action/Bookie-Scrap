package com.bookie.scrap.common.util;

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
        return text != null ? text : "";
    }

}
