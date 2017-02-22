package com.creditdatamw.labs.zerocell.converter;

/**
 * Empty Converter class
 */
public final class NoopConverter implements Converter<String> {
    @Override
    public String convert(String value) {
        return value;
    }
}
