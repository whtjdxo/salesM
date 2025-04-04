package com.web.common.util;

public class KnBankFormatter {

    /**
     * Formats a string value (C type) by padding spaces to the right.
     *
     * @param value The input string value.
     * @param length The total length of the formatted string.
     * @return The formatted string with spaces padded to the right.
     */
    public static String formatCString(String value, int length) {
        if (value == null) {
            value = "";
        }
        return String.format("%-" + length + "s", value);
    }

    /**
     * Formats a numeric value (N type) by padding spaces to the left.
     *
     * @param value The input numeric value.
     * @param length The total length of the formatted string.
     * @return The formatted string with spaces padded to the left.
     */
    public static String formatNString(String value, int length) {
        if (value == null) {
            value = "";
        }
        return String.format("%" + length + "s", value);
    }

    public static void main(String[] args) {
        // Example usage
        System.out.println("C Type: [" + formatCString("ABC", 9) + "]");
        System.out.println("N Type: [" + formatNString("123", 6) + "]");
    }
}
