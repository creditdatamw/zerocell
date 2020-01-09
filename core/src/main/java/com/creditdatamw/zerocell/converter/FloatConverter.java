package com.creditdatamw.zerocell.converter;

import com.creditdatamw.zerocell.ZeroCellException;

class FloatConverter extends DefaultConverter<Float> {
    private static final String message = "Failed to parse '%s' as Float at column='%s' row='%s'";
    @Override
    public Float convert(String value, String columnName, int row) {
        try {
            return Float.parseFloat(value);
        } catch (Exception e) {
            switch (this.getFallbackStrategy()) {
                case LEGACY:
                case DEFAULT_TO_NULL:
                    return null;
                case DEFAULT_TO_ZERO:
                    return 0F;
                case THROW_EXCEPTION:
                    throw new ZeroCellException(String.format(message, value, columnName, row));
                case DEFAULT_TO_MIN_VALUE:
                case DEFAULT:
                default:
                    return Float.MIN_VALUE;
            }
        }
    }
}
