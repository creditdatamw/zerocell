package com.creditdatamw.zerocell.handler;

import java.io.File;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.creditdatamw.zerocell.ReaderUtil;
import com.creditdatamw.zerocell.ZeroCellException;
import com.creditdatamw.zerocell.column.ColumnInfo;
import com.creditdatamw.zerocell.column.ColumnMapping;
import com.creditdatamw.zerocell.column.RowNumberInfo;

import static com.creditdatamw.zerocell.column.ColumnMapping.parseColumnMappingFromAnnotations;

public class EntityHandler<T> {

    private static final String DEFAULT_SHEET = "uploads";

    private final Class<T> type;
    private final EntityExcelSheetHandler<T> entitySheetHandler;
    private final String sheetName;
    private final boolean skipHeaderRow;
    private final int skipFirstNRows;
    private final int maxRowNumber;

    public EntityHandler(Class<T> clazz, boolean skipHeaderRow, int skipFirstNRows, int maxRowNumber) {
        Objects.requireNonNull(clazz);
        assert (skipFirstNRows >= 0);
        this.type = clazz;
        this.sheetName = DEFAULT_SHEET;
        this.entitySheetHandler = createSheetHandler(clazz, null);
        this.skipHeaderRow = skipHeaderRow;
        this.skipFirstNRows = skipFirstNRows;
        this.maxRowNumber = maxRowNumber;
    }

    public EntityHandler(Class<T> clazz, String sheetName, boolean skipHeaderRow, int skipFirstNRows, int maxRowNumber) {
        Objects.requireNonNull(clazz);
        assert (skipFirstNRows >= 0);
        this.type = clazz;
        this.sheetName = sheetName;
        this.entitySheetHandler = createSheetHandler(clazz, null);
        this.skipHeaderRow = skipHeaderRow;
        this.skipFirstNRows = skipFirstNRows;
        this.maxRowNumber = maxRowNumber;
    }

    public EntityHandler(Class<T> clazz, ColumnMapping columnMapping, boolean skipHeaderRow, int skipFirstNRows, int maxRowNumber) {
        Objects.requireNonNull(clazz);
        assert (skipFirstNRows >= 0);
        this.type = clazz;
        this.sheetName = DEFAULT_SHEET;
        this.entitySheetHandler = createSheetHandler(clazz, columnMapping);
        this.skipHeaderRow = skipHeaderRow;
        this.skipFirstNRows = skipFirstNRows;
        this.maxRowNumber = maxRowNumber;
    }

    public EntityHandler(Class<T> clazz, String sheetName, ColumnMapping columnMapping, boolean skipHeaderRow, int skipFirstNRows, int maxRowNumber) {
        Objects.requireNonNull(clazz);
        assert (skipFirstNRows >= 0);
        this.type = clazz;
        this.sheetName = sheetName;
        this.entitySheetHandler = createSheetHandler(clazz, columnMapping);
        this.skipHeaderRow = skipHeaderRow;
        this.skipFirstNRows = skipFirstNRows;
        this.maxRowNumber = maxRowNumber;
    }

    private boolean isRowNumberValueSetted() {
        return this.maxRowNumber != 0 && this.maxRowNumber != this.skipFirstNRows;
    }

    public String getSheetName() {
        return sheetName;
    }

    public EntityExcelSheetHandler<T> getEntitySheetHandler() {
        return entitySheetHandler;
    }

    public boolean isSkipHeaderRow() {
        return skipHeaderRow;
    }

    public int getSkipFirstNRows() {
        return skipFirstNRows;
    }

    public int getMaxRowNumber() {
        return maxRowNumber;
    }

    @SuppressWarnings("unchecked")
    private EntityExcelSheetHandler<T> createSheetHandler(Class<T> clazz, ColumnMapping columnMapping) {
        if (columnMapping == null) {
            columnMapping = parseColumnMappingFromAnnotations(clazz);
        }
        final RowNumberInfo rowNumberColumn = columnMapping.getRowNumberInfo();
        Map<Integer, ColumnInfo> infoMap = columnMapping.getColumnsMap();
        return new EntityExcelSheetHandler(this, rowNumberColumn, infoMap);
    }

    /**
     * Returns the extracted entities as an immutable list.
     *
     * @return an immutable list of the extracted entities
     */
    public List<T> readAsList() {
        return Collections.unmodifiableList(this.entitySheetHandler.read(null, sheetName));
    }

    /**
     * @deprecated use {@link #process(InputStream)} instead
     */
    @Deprecated
    public void process(File file) throws ZeroCellException {
        ReaderUtil.process(file, sheetName, this.entitySheetHandler);
    }

    public void process(InputStream input) throws ZeroCellException {
        ReaderUtil.process(input, sheetName, this.entitySheetHandler);
    }

    public Class<T> getEntityClass() {
        return type;
    }
}