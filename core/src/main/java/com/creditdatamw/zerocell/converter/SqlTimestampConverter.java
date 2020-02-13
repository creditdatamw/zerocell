package com.creditdatamw.zerocell.converter;

import com.creditdatamw.zerocell.internal.IgnoreInvalidValueException;
import com.creditdatamw.zerocell.ZeroCellException;

import java.sql.Timestamp;

public class SqlTimestampConverter extends DefaultConverter<Timestamp> {
    private static final String message = "Failed to parse '%s' as java.sql.Timestamp at column='%s' row='%s'";
    @Override
    public Timestamp convert(String value, String columnName, int row) {
        try {
            return Timestamp.valueOf(value);
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
