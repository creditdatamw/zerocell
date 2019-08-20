package com.creditdatamw.zerocell.converter;

/**
 * Empty Converter class - returns the same exact value provided.
 */
public final class NoopConverter implements Converter<String> {
    @Override
    public String convert(String value, String column, int row) {
        return value;
    }
}
