package com.creditdatamw.zerocell.converter;

import com.creditdatamw.zerocell.internal.IgnoreInvalidValueException;
import com.creditdatamw.zerocell.ZeroCellException;

import java.time.LocalDateTime;

public class LocalDateTimeConverter extends DefaultConverter<LocalDateTime> {
    private static final String message = "Failed to parse '%s' as java.time.LocalDateTime at column='%s' row='%s'";

    @Override
    public LocalDateTime convert(String value, String columnName, int row) {
        try {
            return LocalDateTime.parse(value);
        } catch(Exception e) {
            switch (this.getFallbackStrategy()) {
                case LEGACY:
                case DEFAULT_TO_NULL:
                    return null;
                case IGNORE:
                    throw new IgnoreInvalidValueException();
                case DEFAULT:
                case THROW_EXCEPTION:
                default:
                    throw new ZeroCellException(String.format(message, value, columnName, row));
            }
        }
    }
}
