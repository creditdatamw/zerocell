package com.creditdatamw.zerocell.converter;

/**
 * Converts a value from a String type to another
 */
@FunctionalInterface
public interface Converter<T> {
    T convert(String value, String columnName, int row);
}
