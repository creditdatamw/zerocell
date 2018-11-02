package com.creditdatamw.zerocell;

import com.creditdatamw.zerocell.column.ColumnInfo;
import com.creditdatamw.zerocell.column.ColumnMapping;
import com.creditdatamw.zerocell.column.RowNumberInfo;
import com.creditdatamw.zerocell.handler.EntityHandler;

import java.io.File;
import java.util.List;
import java.util.Objects;

/**
 * Main API for ZeroCell
 */
public class Reader {

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
         * @param value
         * @return
         */
        public ReaderBuilder<T> skipFirstNRows(int value) {
            assert(value > 0);
            this.skipFirstNRows = value;
            return this;
        }

        public <T> List<T> list() {
            EntityHandler<T> entityHandler;
            if (!Objects.isNull(sheetName) && !Objects.isNull(columnMapping)) {
                entityHandler = new EntityHandler(clazz, sheetName, columnMapping, skipHeaderRow, skipFirstNRows);
            } else if (Objects.isNull(sheetName) && !Objects.isNull(columnMapping)) {
                entityHandler = new EntityHandler(clazz, columnMapping, skipHeaderRow, skipFirstNRows);
            } else if (!Objects.isNull(sheetName) && Objects.isNull(columnMapping)) {
                entityHandler = new EntityHandler(clazz, sheetName, skipHeaderRow, skipFirstNRows);
            } else {
                entityHandler = new EntityHandler(clazz, skipHeaderRow, skipFirstNRows);
            }
            entityHandler.process(file);
            return entityHandler.readAsList();
        }
    }
}
