package com.creditdatamw.zerocell.converter;

import com.creditdatamw.zerocell.internal.IgnoreInvalidValueException;
import com.creditdatamw.zerocell.ZeroCellException;

public class DoubleConverter extends DefaultConverter<Double> {
    private static final String message = "Failed to parse '%s' as Double at column='%s' row='%s'";

    @Override
    public Double convert(String value, String columnName, int row) {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            switch (this.getFallbackStrategy()) {
                case LEGACY:
                case DEFAULT_TO_NULL:
                    return null;
                case DEFAULT_TO_ZERO:
                    return 0.0D;
                case THROW_EXCEPTION:
                    throw new ZeroCellException(String.format(message, value, columnName, row));
                case IGNORE:
                    throw new IgnoreInvalidValueException();
                case DEFAULT_TO_MIN_VALUE:
                case DEFAULT:
                default:
                    return Double.MIN_VALUE;
            }
        }
    }
}
