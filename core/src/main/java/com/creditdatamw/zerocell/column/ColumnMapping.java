package com.creditdatamw.zerocell.column;

import java.util.Arrays;
import java.util.List;

public final class ColumnMapping {
    private final ColumnInfo rowNumberInfo;
    private final List<ColumnInfo> columns;

    public ColumnMapping(ColumnInfo rowNumberInfo, List<ColumnInfo> columns) {
        this.rowNumberInfo = rowNumberInfo;
        this.columns = columns;
    }

    public ColumnMapping(ColumnInfo rowNumberInfo,ColumnInfo... columns) {
        this.rowNumberInfo = rowNumberInfo;
        this.columns = Arrays.asList(columns);
    }

    public ColumnInfo getRowNumberInfo() {
        return rowNumberInfo;
    }

    public List<ColumnInfo> getColumns() {
        return columns;
    }
}
