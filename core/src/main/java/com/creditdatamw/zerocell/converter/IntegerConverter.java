package com.creditdatamw.zerocell.converter;

import com.creditdatamw.zerocell.internal.IgnoreInvalidValueException;
import com.creditdatamw.zerocell.ZeroCellException;

public class IntegerConverter extends DefaultConverter<Integer> {
    private static final String message = "Failed to parse '%s' as Integer at column='%s' row='%s'";

    @Override
    public Integer convert(String value, String columnName, int row) {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            switch (this.getFallbackStrategy()) {
                case LEGACY:
                case DEFAULT_TO_NULL:
                    return null;
                case DEFAULT_TO_ZERO:
                    return 0;
                case THROW_EXCEPTION:
                    throw new ZeroCellException(String.format(message, value, columnName, row));
                case IGNORE:
                    throw new IgnoreInvalidValueException();
                case DEFAULT_TO_MIN_VALUE:
                case DEFAULT:
                default:
                    return Integer.MIN_VALUE;
            }
        }
    }
}
