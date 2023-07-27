package com.primesoft.cryptanil.utils;

public class StringUtils {

    public static String replaceCommas(String string) {
        return string.replace(",", ".");
    }

    public static boolean isValidDouble(String s) {
        return !(s.isEmpty() || s.equals("-") || s.equals(".") || s.contains("-.") || s.contains(".-"));
    }

}