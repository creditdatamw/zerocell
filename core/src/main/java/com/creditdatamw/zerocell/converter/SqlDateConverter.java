package com.creditdatamw.zerocell.converter;

import com.creditdatamw.zerocell.internal.IgnoreInvalidValueException;
import com.creditdatamw.zerocell.ZeroCellException;

import java.sql.Date;

public class SqlDateConverter extends DefaultConverter<Date> {
    private static final String message = "Failed to parse '%s' as java.sql.Date at column='%s' row='%s'";

    @Override
    public Date convert(String value, String columnName, int row) {
        try {
            return Date.valueOf(value);
        } catch (Exception e) {
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
