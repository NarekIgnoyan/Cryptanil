package com.primesoft.cryptanil.utils

class StringUtils {
    companion object{
        fun replaceCommas(string: String): String {
            return string.replace(",", ".")
        }

        fun isValidDouble(s: String): Boolean {
            return !(s.isEmpty() || s == "-" || s == "." || s.contains("-.") || s.contains(".-"))
        }
    }
}