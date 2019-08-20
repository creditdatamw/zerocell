package com.creditdatamw.zerocell.handler;

import com.creditdatamw.zerocell.ReaderUtil;
import com.creditdatamw.zerocell.ZeroCellException;
import com.creditdatamw.zerocell.ZeroCellReader;
import com.creditdatamw.zerocell.column.ColumnInfo;
import com.creditdatamw.zerocell.converter.Converter;
import com.creditdatamw.zerocell.converter.NoopConverter;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

import static com.creditdatamw.zerocell.converter.ConverterUtils.convertValueToType;

final class EntityExcelSheetHandler<T> implements ZeroCellReader {
    private EntityHandler<T> entityHandler;
    private final Logger LOGGER = LoggerFactory.getLogger(EntityExcelSheetHandler.class);

    private final ColumnInfo rowNumberColumn;
    private final Map<Integer, ColumnInfo> columns;
    private final List<T> entities;
    private final Converter NOOP_CONVERTER = new NoopConverter();
    private final Map<Integer, Converter> converters;
    private boolean isHeaderRow = false;
    private int currentRow = -1;
    private int currentCol = -1;
    private EmptyColumnCounter emptyColumnCounter = new EmptyColumnCounter();
    private T cur;

    EntityExcelSheetHandler(EntityHandler<T> entityHandler, ColumnInfo rowNumberColumn, Map<Integer, ColumnInfo> columns) {
        this.entityHandler = entityHandler;
        this.rowNumberColumn = rowNumberColumn;
        this.columns = columns;
        this.converters = cacheConverters();
        this.entities = new ArrayList<>();
    }

    private Map<Integer, Converter> cacheConverters() {
        Map<Integer, Converter> mappedConverters = new HashMap<>();
        for (ColumnInfo columnInfo : columns.values()) {
            mappedConverters.put(columnInfo.getIndex(), NOOP_CONVERTER);
            try {
                if (!columnInfo.getConverterClass().equals(NoopConverter.class)) {
                    mappedConverters.put(columnInfo.getIndex(), (Converter) columnInfo.getConverterClass().newInstance());
                }
            } catch (InstantiationException | IllegalAccessException e) {
                LOGGER.error("Failed to instantiate Converter class: {}", columnInfo.getConverterClass());
            }
        }
        return mappedConverters;
    }

    /**
     * Returns a list of entities loaded from the provided Excel file
     * @param file the Excel file to load data from
     * @param sheet the sheet to load data from
     * @return list of entities from the sheet
     */
    @Override
    public List<T> read(File file, String sheet) {
        /**
         * We don't need to process the file here since that's
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
            if (!Objects.isNull(rowNumberColumn)) {
                writeColumnField(cur, String.valueOf(currentRow), rowNumberColumn, currentRow);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ZeroCellException("Failed to create and instance of " + entityHandler.getEntityClass().getName(), e);
        }
    }

    private boolean isRowNumberValueSetted() {
        return entityHandler.getMaxRowNumber() != 0 &&
            entityHandler.getMaxRowNumber() != entityHandler.getSkipFirstNRows();
    }

    @Override
    public void endRow(int i) {
        if (isRowNumberValueSetted() && i > entityHandler.getMaxRowNumber()) {
            return;
        }

        if (!Objects.isNull(cur)) {
            if (entityHandler.isSkipEmptyRows() && emptyColumnCounter.rowIsEmpty()) {
                LOGGER.warn("Row#{} skipped because it is empty", i);
            } else {
                this.entities.add(cur);
            }
        }
        cur = null;
        emptyColumnCounter.reset();
    }

    @Override
    public void cell(String cellReference, String formattedValue, XSSFComment xssfComment) {
        if (cellReference == null) {
            cellReference = new CellAddress(currentRow, currentCol).formatAsString();
        }
        int column = new CellReference(cellReference).getCol();
        currentCol = column;

        ColumnInfo currentColumnInfo = columns.get(column);
        if (Objects.isNull(currentColumnInfo)) {
            return;
        }

        if (isHeaderRow && !entityHandler.isSkipHeaderRow()) {
            if (!currentColumnInfo.getName().equalsIgnoreCase(formattedValue.trim())) {
                throw new ZeroCellException(String.format("Expected Column '%s' but found '%s'", currentColumnInfo.getName(), formattedValue));
            }
        }
        // Prevent from trying to write to a null instance
        if (Objects.isNull(cur)) return;
        if (entityHandler.isSkipEmptyRows()) {
            if (formattedValue == null || formattedValue.isEmpty()) {
                emptyColumnCounter.increment();
            }
        }
        writeColumnField(cur, formattedValue, currentColumnInfo, currentRow);
    }

    /**
     * Write the value read from the excel cell to a field
     *
     * @param object            the object to write to
     * @param formattedValue    the value read from the current excel column/row
     * @param currentColumnInfo Column metadata
     * @param rowNum            the row number
     */
    private void writeColumnField(T object, String formattedValue, ColumnInfo currentColumnInfo, int rowNum) {
        String fieldName = currentColumnInfo.getFieldName();
        try {
            Converter converter = NOOP_CONVERTER;
            if (currentColumnInfo.getIndex() != -1) {
                converter = converters.get(currentColumnInfo.getIndex());
            }
            Object value = null;
            // Don't use a converter if there isn't a custom one
            if (converter instanceof NoopConverter) {
                value = convertValueToType(currentColumnInfo.getType(), formattedValue, currentColumnInfo.getName(), rowNum);
            } else {
                // Handle any exceptions thrown by the converter - this stops execution of the whole process
                try {
                    value = converter.convert(formattedValue, currentColumnInfo.getName(), rowNum);
                } catch (Exception e) {
                    throw new ZeroCellException(String.format("%s threw an exception while trying to convert value %s ", converter.getClass().getName(), formattedValue), e);
                }
            }
            Field field = entityHandler.getEntityClass()
                    .getDeclaredField(currentColumnInfo.getFieldName());
            boolean access = field.isAccessible();
            if (!access) {
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

    private class EmptyColumnCounter {
        private int count = 0;

        void increment() {
            this.count += 1;
        }

        boolean rowIsEmpty() {
            return this.count >= columns.size();
        }
        void reset() {
            count = 0;
        }
    }
}
