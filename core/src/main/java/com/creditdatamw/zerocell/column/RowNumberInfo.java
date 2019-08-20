package com.creditdatamw.zerocell.column;

public final class RowNumberInfo extends ColumnInfo {
    public RowNumberInfo(String fieldName, Class<?> dataType) {
        super("__rowNUmber__", fieldName, -1, dataType);
    }
}

