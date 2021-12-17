package com.creditdatamw.zerocell.converter;

import com.creditdatamw.zerocell.internal.IgnoreInvalidValueException;
import com.creditdatamw.zerocell.ZeroCellException;

public class LongConverter extends DefaultConverter<Long> {
    private static final String message = "Failed to parse '%s' as Long at column='%s' row='%s'";
    @Override
    public Long convert(String value, String columnName, int row) {
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            switch (this.getFallbackStrategy()) {
                case LEGACY:
                case DEFAULT_TO_NULL:
                    return null;
                case DEFAULT_TO_ZERO:
                    return 0L;
                case THROW_EXCEPTION:
                    throw new ZeroCellException(String.format(message, value, columnName, row));
                case IGNORE:
                    throw new IgnoreInvalidValueException();
                case DEFAULT_TO_MIN_VALUE:
                case DEFAULT:
                default:
                    return Long.MIN_VALUE;
            }
        }
    }
}
