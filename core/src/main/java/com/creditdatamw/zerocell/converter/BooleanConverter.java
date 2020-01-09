package com.creditdatamw.zerocell.converter;

import com.creditdatamw.zerocell.ZeroCellException;

public class BooleanConverter extends DefaultConverter<Boolean> {
    @Override
    public Boolean convert(String value, String columnName, int row) {
        try {
            return Boolean.valueOf(value);
        } catch(Exception e) {
            switch (this.getFallbackStrategy()) {
                case DEFAULT_TO_TRUE:
                    return Boolean.TRUE;
                case DEFAULT_TO_FALSE:
                    return Boolean.FALSE;
                default:
                    throw new ZeroCellException(String.format(
                        "Failed to parse '%s' as Boolean at column='%s' row='%s'", value, columnName, row));
            }
        }
    }
}
