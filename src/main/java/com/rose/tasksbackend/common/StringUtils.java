package com.rose.tasksbackend.common;

public class StringUtils {
    public static final String EMPTY = "";

    public static boolean isBlankOrEmpty(String text) {
        return text == null || text.isBlank() || text.isEmpty();
    }
}
