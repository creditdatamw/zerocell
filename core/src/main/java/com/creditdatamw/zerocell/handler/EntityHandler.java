package com.creditdatamw.zerocell.handler;

import com.creditdatamw.zerocell.ReaderUtil;
import com.creditdatamw.zerocell.ZeroCellException;
import com.creditdatamw.zerocell.column.ColumnInfo;
import com.creditdatamw.zerocell.column.ColumnMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.creditdatamw.zerocell.column.ColumnMapping.parseColumnMappingFromAnnotations;

public class EntityHandler<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(EntityHandler.class);

    private static final String DEFAULT_SHEET = "uploads";

    private final Class<T> type;
    private final EntityExcelSheetHandler<T> entitySheetHandler;
    private final String sheetName;
    private final boolean skipHeaderRow;
    private final int skipFirstNRows;

    public EntityHandler(Class<T> clazz, boolean skipHeaderRow, int skipFirstNRows) {
        Objects.requireNonNull(clazz);
        assert(skipFirstNRows >= 0);
        this.type = clazz;
        this.sheetName = DEFAULT_SHEET;
        this.entitySheetHandler = createSheetHandler(clazz, null);
        this.skipHeaderRow = skipHeaderRow;
        this.skipFirstNRows = skipFirstNRows;
    }

    public EntityHandler(Class<T> clazz, String sheetName, boolean skipHeaderRow, int skipFirstNRows) {
        Objects.requireNonNull(clazz);
        assert(skipFirstNRows >= 0);
        this.type = clazz;
        this.sheetName = sheetName;
        this.entitySheetHandler = createSheetHandler(clazz, null);
        this.skipHeaderRow = skipHeaderRow;
        this.skipFirstNRows = skipFirstNRows;
    }

    public EntityHandler(Class<T> clazz, ColumnMapping columnMapping, boolean skipHeaderRow, int skipFirstNRows) {
        Objects.requireNonNull(clazz);
        assert(skipFirstNRows >= 0);
        this.type = clazz;
        this.sheetName = DEFAULT_SHEET;
        this.entitySheetHandler = createSheetHandler(clazz, columnMapping);
        this.skipHeaderRow = skipHeaderRow;
        this.skipFirstNRows = skipFirstNRows;
    }

    public EntityHandler(Class<T> clazz, String sheetName, ColumnMapping columnMapping, boolean skipHeaderRow, int skipFirstNRows) {
        Objects.requireNonNull(clazz);
        assert(skipFirstNRows >= 0);
        this.type = clazz;
        this.sheetName = sheetName;
        this.entitySheetHandler = createSheetHandler(clazz, columnMapping);
        this.skipHeaderRow = skipHeaderRow;
        this.skipFirstNRows = skipFirstNRows;
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

    @SuppressWarnings("unchecked")
    private EntityExcelSheetHandler<T> createSheetHandler(Class<T> clazz, ColumnMapping columnMapping) {
        if (columnMapping == null) {
            columnMapping = parseColumnMappingFromAnnotations(clazz);
        }
        final ColumnInfo rowNumberColumn = columnMapping.getRowNumberInfo();
        final List<ColumnInfo> list = columnMapping.getColumns();
        final ColumnInfo[] columns = new ColumnInfo[list.size()];
        int index = 0;
        for(ColumnInfo columnInfo: list) {
            index = columnInfo.getIndex();
            if (index > columns.length - 1) {
                throw new ZeroCellException(
                        "Column index out of range. index=" + index + " columnCount=" + columns.length
                        + ". Ensure there @Column annotations for all indexes from 0 to " + (columns.length - 1));
            }
            if (! Objects.isNull(columns[index])) {
                throw new ZeroCellException("Cannot map two columns to the same index: " + index);
            }
            columns[index] = columnInfo;
        }
        return new EntityExcelSheetHandler(this, rowNumberColumn, columns);
    }

    /**
     * Returns the extracted entities as an immutable list.
     * @return an immutable list of the extracted entities
     */
    public List<T> readAsList() {
        List<T> list = Collections.unmodifiableList(this.entitySheetHandler.read(null, sheetName));
        return list;
    }

    public void process(File file) throws ZeroCellException {
        ReaderUtil.process(file, sheetName, this.entitySheetHandler);
    }

    public Class<T> getEntityClass() {
        return type;
    }
}