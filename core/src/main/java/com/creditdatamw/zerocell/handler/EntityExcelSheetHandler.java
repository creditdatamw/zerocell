package com.creditdatamw.zerocell.handler;

import com.creditdatamw.zerocell.ReaderUtil;
import com.creditdatamw.zerocell.ZeroCellException;
import com.creditdatamw.zerocell.ZeroCellReader;
import com.creditdatamw.zerocell.column.ColumnInfo;
import com.creditdatamw.zerocell.converter.Converter;
import com.creditdatamw.zerocell.converter.NoopConverter;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.creditdatamw.zerocell.converter.ConverterUtils.convertValueToType;

final class EntityExcelSheetHandler<T> implements ZeroCellReader {
    private EntityHandler entityHandler;
    private final Logger LOGGER = LoggerFactory.getLogger(EntityExcelSheetHandler.class);

    private final ColumnInfo rowNumberColumn;
    private final ColumnInfo[] columns;
    private final List<T> entities;
    private final Converter NOOP_CONVERTER = new NoopConverter();
    private final Converter[] converters;
    private final int MAXIMUM_COL_INDEX;

    private boolean isHeaderRow = false;
    private int currentRow = -1;
    private int currentCol = -1;
    private T cur;

    EntityExcelSheetHandler(EntityHandler entityHandler, ColumnInfo rowNumberColumn, ColumnInfo[] columns) {
        this.entityHandler = entityHandler;
        this.rowNumberColumn = rowNumberColumn;
        this.columns = columns;
        this.converters = cacheConverters();
        this.entities = new ArrayList<>();
        this.MAXIMUM_COL_INDEX = columns.length - 1;
    }

    private Converter[] cacheConverters() {
        Converter[] cv = new Converter[columns.length];
        for (ColumnInfo c: columns) {
            cv[c.getIndex()] = NOOP_CONVERTER;
            try {
                if (!c.getConverterClass().equals(NoopConverter.class)) {
                    cv[c.getIndex()] = (Converter) c.getConverterClass().newInstance();
                }
            } catch (InstantiationException | IllegalAccessException e) {
                LOGGER.error("Failed to instantiate Converter class: {}", c.getConverterClass());
            }
        }
        return cv;
    }

    @Override
    public List<T> read(File file, String sheet) {
        /** We don't need to process the file here since that's
         * handled in {@link ReaderUtil} which MUST be used when using this class
         */
        return Collections.unmodifiableList(this.entities);
    }

    void clear() {
        this.currentRow = -1;
        this.currentCol = -1;
        this.cur = null;
        this.entities.clear();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void startRow(int i) {
        currentRow = i;
        //skip the current row
        if (currentRow - entityHandler.getSkipFirstNRows() < 0) return;
        currentRow = currentRow - entityHandler.getSkipFirstNRows();
        // skip the header row
        if (currentRow == 0) {
            isHeaderRow = true;
            return;
        } else {
            isHeaderRow = false;
        }
        try {
            cur = (T) entityHandler.getEntityClass().newInstance();
            // Write to the field with the @RowNumber annotation here if it exists
            if (! Objects.isNull(rowNumberColumn)) {
                writeColumnField(cur, String.valueOf(currentRow), rowNumberColumn, currentRow);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ZeroCellException("Failed to create and instance of " + entityHandler.getEntityClass().getName(), e);
        }
    }

    @Override
    public void endRow(int i) {
        if (! Objects.isNull(cur)) {
            this.entities.add(cur);
            cur = null;
        }
    }

    @Override
    public void cell(String cellReference, String formattedValue, XSSFComment xssfComment) {
        // gracefully handle missing CellRef here in a similar way as XSSFCell does
        if(cellReference == null) {
            cellReference = new CellAddress(currentRow, currentCol).formatAsString();
        }

        int column = new CellReference(cellReference).getCol();
        currentCol = column;

        // We ignore additional cells here since we only care about the cells
        // in the columns array i.e. the defined columns
        if (column > MAXIMUM_COL_INDEX) {
            LOGGER.trace("Invalid Column index found: " + column);
            return;
        }

        ColumnInfo currentColumnInfo = columns[column];

        if (isHeaderRow && !entityHandler.isSkipHeaderRow()) {
            if (! currentColumnInfo.getName().equalsIgnoreCase(formattedValue.trim())){
                throw new ZeroCellException(String.format("Expected Column '%s' but found '%s'", currentColumnInfo.getName(), formattedValue));
            }
        }
        // Prevent from trying to write to a null instance
        if (Objects.isNull(cur)) return;
        writeColumnField(cur, formattedValue, currentColumnInfo, currentRow);
    }

    /**
     * Write the value read from the excel cell to a field
     *
     * @param object the object to write to
     * @param formattedValue the value read from the current excel column/row
     * @param currentColumnInfo Column metadata
     * @param rowNum the row number
     */
    private void writeColumnField(T object, String formattedValue, ColumnInfo currentColumnInfo, int rowNum) {
        String fieldName = currentColumnInfo.getFieldName();
        try {
            Converter converter = NOOP_CONVERTER;
            if (currentColumnInfo.getIndex() != -1 && currentColumnInfo.getIndex() < columns.length) {
                converter = converters[currentColumnInfo.getIndex()];
            }
            Object value = null;
            // Don't use a converter if there isn't a custom one
            if (converter instanceof NoopConverter) {
                value = convertValueToType(currentColumnInfo.getType(), formattedValue, currentColumnInfo.getName(), rowNum);
            } else {
                // Handle any exceptions thrown by the converter - this stops execution of the whole process
                try {
                    value = converter.convert(formattedValue, currentColumnInfo.getName(), rowNum);
                } catch(Exception e) {
                    throw new ZeroCellException(String.format("%s threw an exception while trying to convert value %s ", converter.getClass().getName(), formattedValue), e);
                }
            }
            Field field = entityHandler.getEntityClass().getDeclaredField(currentColumnInfo.getFieldName());
            boolean access = field.isAccessible();
            if (! access) {
                field.setAccessible(true);
            }
            field.set(cur, value);
            field.setAccessible(field.isAccessible() && access);
        } catch (IllegalArgumentException e) {
            throw new ZeroCellException(String.format("Failed to write value %s to field %s at row %s", formattedValue, fieldName, rowNum));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            LOGGER.error("Failed to set field: {}", fieldName, e);
        }
    }

    @Override
    public void headerFooter(String text, boolean b, String tagName) {
        // Skip, no headers or footers in CSV
    }
}
