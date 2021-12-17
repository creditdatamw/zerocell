package com.creditdatamw.zerocell.converter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Utility class for Converters
 */
public final class ConverterUtils {
    ConverterUtils() {}

    static final DefaultConverters defaultConverters = Converters.newDefaultConverters();

    /**
     * Converts a value from a string to an object of the specified type
     *
     * @param fieldType Type of class to convert value to
     * @param formattedValue the value from Excel as a string
     * @param columnName The name of the column
     * @param rowNum The row number
     * @return a value converted from string to the corresponding type
     */
    @Deprecated
    public static Object convertValueToType(Class<?> fieldType,
                                            String formattedValue,
                                            String columnName,
                                            int rowNum) {

        Object value = null;
        if (fieldType == String.class) {
            value = String.valueOf(formattedValue);
        } else if (fieldType == LocalDateTime.class) {
            return Converters.toLocalDateTime
                    .convert(formattedValue, columnName, rowNum);

        } else if (fieldType == LocalDate.class) {
            return Converters.toLocalDate
                    .convert(formattedValue, columnName, rowNum);

        } else if (fieldType == java.sql.Date.class) {
            return Converters.toSqlDate
                    .convert(formattedValue, columnName, rowNum);

        } else if (fieldType == Timestamp.class) {
            return defaultConverters.toSqlTimestamp
                    .convert(formattedValue, columnName, rowNum);

        } else if (fieldType == Integer.class || fieldType == int.class) {
            return Converters.toInteger
                    .convert(formattedValue, columnName, rowNum);

        } else if (fieldType == Long.class || fieldType == long.class) {
            return Converters.toLong
                    .convert(formattedValue, columnName, rowNum);

        } else if (fieldType == Double.class || fieldType == double.class) {
            return Converters.toDouble
                    .convert(formattedValue, columnName, rowNum);

        } else if (fieldType == Float.class || fieldType == float.class) {
            return defaultConverters.toFloat
                    .convert(formattedValue, columnName, rowNum);

        } else if (fieldType == Boolean.class) {
            return defaultConverters.toBoolean
                    .convert(formattedValue, columnName, rowNum);
        }

        return value;
    }

    /**
     * Converts a value from a string to an object of the specified type
     *
     * @param fieldType Type of class to convert value to
     * @param formattedValue the value from Excel as a string
     * @param columnName The name of the column
     * @param rowNum The row number
     * @return a value converted from string to the corresponding type
     */
    public static Object convertValueToType(Class<?> fieldType,
                                            String formattedValue,
                                            String columnName,
                                            int rowNum,
                                            FallbackStrategy fallbackStrategy) {


        Object value = null;
        if (fieldType == String.class) {
            value = String.valueOf(formattedValue);
        } else if (fieldType == LocalDateTime.class) {
            return defaultConverters.toLocalDateTime
                    .withFallbackStrategy(fallbackStrategy)
                    .convert(formattedValue, columnName, rowNum);

        } else if (fieldType == LocalDate.class) {
            return defaultConverters.toLocalDate
                    .withFallbackStrategy(fallbackStrategy)
                    .convert(formattedValue, columnName, rowNum);

        } else if (fieldType == java.sql.Date.class) {
            return defaultConverters.toSqlDate
                    .withFallbackStrategy(fallbackStrategy)
                    .convert(formattedValue, columnName, rowNum);

        } else if (fieldType == Timestamp.class) {
            return defaultConverters.toSqlTimestamp
                    .withFallbackStrategy(fallbackStrategy)
                    .convert(formattedValue, columnName, rowNum);

        } else if (fieldType == Integer.class || fieldType == int.class) {
            return defaultConverters.toInteger
                    .withFallbackStrategy(fallbackStrategy)
                    .convert(formattedValue, columnName, rowNum);

        } else if (fieldType == Long.class || fieldType == long.class) {
            return defaultConverters.toLong
                    .withFallbackStrategy(fallbackStrategy)
                    .convert(formattedValue, columnName, rowNum);

        } else if (fieldType == Double.class || fieldType == double.class) {
            return defaultConverters.toDouble
                    .withFallbackStrategy(fallbackStrategy)
                    .convert(formattedValue, columnName, rowNum);

        } else if (fieldType == Float.class || fieldType == float.class) {
            return defaultConverters.toFloat
                    .withFallbackStrategy(fallbackStrategy)
                    .convert(formattedValue, columnName, rowNum);

        } else if (fieldType == Boolean.class) {
            return defaultConverters.toBoolean
                    .withFallbackStrategy(fallbackStrategy)
                    .convert(formattedValue, columnName, rowNum);
        }

        return value;
    }

}
