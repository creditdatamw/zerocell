package com.creditdatamw.labs.zerocell.converter;

/**
 * Converts a value from a String type to another
 */
@FunctionalInterface
public interface Converter<T> {
    T convert(String value);
}
