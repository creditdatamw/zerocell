package com.creditdatamw.zerocell.example;

import com.creditdatamw.zerocell.converter.Converter;

/**
 * Simple Converter that prefixes values with ID-
 */
public class IdPrefixingConverter implements Converter<String> {
    @Override
    public String convert(String value, String columnName, int row) {
        return String.format("ID-%s", value);
    }
}
