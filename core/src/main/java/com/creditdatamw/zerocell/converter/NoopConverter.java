package com.creditdatamw.zerocell.converter;

/**
 * Empty Converter class - returns the same exact value provided.
 */
public final class NoopConverter extends DefaultConverter<String> {
    @Override
    public String convert(String value, String column, int row) {
        return value;
    }
}
