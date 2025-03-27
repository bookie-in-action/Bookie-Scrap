package com.bookie.scrap.common.util;

public class StringUtil {
    public static boolean hasText(String text) {
        if (text == null || text.isBlank()) {
            return false;
        }

        return true;
    }

}
