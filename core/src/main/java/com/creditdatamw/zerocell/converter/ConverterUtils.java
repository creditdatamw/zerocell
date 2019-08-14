package com.creditdatamw.zerocell.converter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Utility class for Converters
 */
public final class ConverterUtils {
    ConverterUtils() {}

    /**
     * Converts a value from a string to an object of the specified type
     *
     * @param fieldType Type of class to convert value to
     * @param formattedValue the value from Excel as a string
     * @param columnName The name of the column
     * @param rowNum The row number
     * @return a value converted from string to the corresponding type
     */
    public static Object convertValueToType(Class<?> fieldType, String formattedValue, String columnName, int rowNum) {
        Object value = null;
        if (fieldType == String.class) {
            value = String.valueOf(formattedValue);
        } else if (fieldType == LocalDateTime.class) {
            return Converters.toLocalDateTime.convert(formattedValue, columnName, rowNum);

        } else if (fieldType == LocalDate.class) {
            return Converters.toLocalDate.convert(formattedValue, columnName, rowNum);

        } else if (fieldType == java.sql.Date.class) {
            return Converters.toSqlDate.convert(formattedValue, columnName, rowNum);

        } else if (fieldType == Timestamp.class) {
            return Converters.toSqlTimestamp.convert(formattedValue, columnName, rowNum);

        } else if (fieldType == Integer.class || fieldType == int.class) {
            return Converters.toInteger.convert(formattedValue, columnName, rowNum);

        } else if (fieldType == Long.class || fieldType == long.class) {
            return Converters.toLong.convert(formattedValue, columnName, rowNum);

        } else if (fieldType == Double.class || fieldType == double.class) {
            return Converters.toDouble.convert(formattedValue, columnName, rowNum);

        } else if (fieldType == Float.class || fieldType == float.class) {
            return Converters.toFloat.convert(formattedValue, columnName, rowNum);

        } else if (fieldType == Boolean.class) {
            return Converters.toBoolean.convert(formattedValue, columnName, rowNum);
        }
        return value;
    }

}
