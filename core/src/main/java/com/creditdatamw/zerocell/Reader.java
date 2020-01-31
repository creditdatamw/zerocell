package com.creditdatamw.zerocell;

import com.creditdatamw.zerocell.column.ColumnInfo;
import com.creditdatamw.zerocell.column.ColumnMapping;
import com.creditdatamw.zerocell.column.RowNumberInfo;
import com.creditdatamw.zerocell.handler.EntityHandler;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Main API for ZeroCell
 */
public class Reader<T> {

    public static <T> String[] columnsOf(Class<T> clazz) {
        return ColumnInfo.columnsOf(clazz);
    }

    public static <T> ReaderBuilder<T> of(Class<T> clazz) {
        return new ReaderBuilder<>(clazz);
    }

    public static final class ReaderBuilder<T> {
        private final Class<T> clazz;
        private File file;
        private String sheetName;
        private ColumnMapping columnMapping;
        private boolean skipHeaderRow = false;
        private int skipFirstNRows = 0;
        private int maxRowNumber = 0;

        public ReaderBuilder(Class<T> clazz) {
            this.clazz = clazz;
        }

        public ReaderBuilder<T> from(File file) {
            Objects.requireNonNull(file);
            this.file = file;
            return this;
        }

        public ReaderBuilder<T> using(RowNumberInfo rowNumberInfo, ColumnInfo... columns) {
            this.columnMapping = new ColumnMapping(rowNumberInfo, columns);
            return this;
        }

        public ReaderBuilder<T> using(ColumnInfo... columns) {
            this.columnMapping = new ColumnMapping(null, columns);
            return this;
        }

        public ReaderBuilder<T> sheet(String sheetName) {
            Objects.requireNonNull(sheetName);
            this.sheetName = sheetName;
            return this;
        }

        public ReaderBuilder<T> skipHeaderRow(boolean value) {
            this.skipHeaderRow = value;
            return this;
        }

        /**
         * Set the number of rows to skip before the header
         *
         * @param value is the number of rows to skip
         * @return ReaderBuilder
         */
        public ReaderBuilder<T> skipFirstNRows(int value) {
            assert (value > 0);
            this.skipFirstNRows = value;
            this.maxRowNumber += value;
            return this;
        }

        /**
         * Set the maximum number of rows to parse.
         *
         * @param rowNumber the number of rows to parse.
         * @return ReaderBuilder
         */
        public ReaderBuilder<T> setRowNumber(int rowNumber) {
            assert (rowNumber > 0);
            this.maxRowNumber += rowNumber;
            return this;
        }

        /**
         * Processes the file
         *
         * @param <T>
         * @return EntityHandler instance
         */
        @SuppressWarnings("unchecked")
        private <T> EntityHandler<T> processFile() {
            EntityHandler<T> entityHandler;
            if (!Objects.isNull(sheetName) && !Objects.isNull(columnMapping)) {
                entityHandler = new EntityHandler(clazz, sheetName, columnMapping, skipHeaderRow, skipFirstNRows, maxRowNumber);
            } else if (Objects.isNull(sheetName) && !Objects.isNull(columnMapping)) {
                entityHandler = new EntityHandler(clazz, columnMapping, skipHeaderRow, skipFirstNRows, maxRowNumber);
            } else if (!Objects.isNull(sheetName) && Objects.isNull(columnMapping)) {
                entityHandler = new EntityHandler(clazz, sheetName, skipHeaderRow, skipFirstNRows, maxRowNumber);
            } else {
                entityHandler = new EntityHandler(clazz, skipHeaderRow, skipFirstNRows, maxRowNumber);
            }
            entityHandler.process(file);
            return entityHandler;
        }

        /**
         * Reads the Entities from the Excel file and returns a list
         * @return List of entities
         */
        public List<T> list() {
            EntityHandler<T> entityHandler = processFile();
            return entityHandler.readAsList();
        }

        /**
         * Reads the Entities the Excel file and returns a Stream
         * @return Stream of entities
         */
        public Stream<T> stream() {
            EntityHandler<T> entityHandler = processFile();
            return entityHandler.readAsStream();
        }
    }
}
