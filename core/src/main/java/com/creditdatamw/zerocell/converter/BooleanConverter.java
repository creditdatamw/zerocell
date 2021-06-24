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
                case LEGACY:
                case DEFAULT_TO_FALSE:
                    return Boolean.FALSE;
                // This is a special case for DO_NOT_SET
                case IGNORE: // TODO(zikani03): Review whether this case is appropriate
                default:
                    throw new ZeroCellException(String.format(
                        "Failed to parse '%s' as Boolean at column='%s' row='%s'", value, columnName, row));
            }
        }
    }
}
