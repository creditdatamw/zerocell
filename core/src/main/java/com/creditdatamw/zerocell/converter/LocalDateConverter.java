package com.creditdatamw.zerocell.converter;

import com.creditdatamw.zerocell.ZeroCellException;

import java.time.LocalDate;

public class LocalDateConverter extends DefaultConverter<LocalDate> {
    private static final String message = "Failed to parse '%s' as java.time.LocalDate at column='%s' row='%s'";
    @Override
    public LocalDate convert(String value, String columnName, int row) {
        try {
            return LocalDate.parse(value);
        } catch(Exception e) {
            switch (this.getFallbackStrategy()) {
                case LEGACY:
                case DEFAULT_TO_NULL:
                    return null;
                case DEFAULT:
                case THROW_EXCEPTION:
                default:
                    throw new ZeroCellException(String.format(message, value, columnName, row));
            }
        }
    }
}
