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

    public static <T> ReaderBuilder of(Class<T> clazz) {
        return new ReaderBuilder<>(clazz);
    }

    public static final class ReaderBuilder<T> {
        private final Class<T> clazz;
        private File file;
        private String sheetName;
        private ColumnMapping columnMapping;
        private boolean skipHeaderRow = false;

        public ReaderBuilder(Class<T> clazz) {
            this.clazz = clazz;
        }

        public ReaderBuilder from(File file) {
            Objects.requireNonNull(file);
            this.file = file;
            return this;
        }

        public ReaderBuilder using(RowNumberInfo rowNumberInfo, ColumnInfo... columns) {
            this.columnMapping = new ColumnMapping(rowNumberInfo, columns);
            return this;
        }

        public ReaderBuilder using(ColumnInfo... columns) {
            this.columnMapping = new ColumnMapping(null, columns);
            return this;
        }

        public ReaderBuilder sheet(String sheetName) {
            Objects.requireNonNull(sheetName);
            this.sheetName = sheetName;
            return this;
        }

        public ReaderBuilder skipHeaderRow(boolean value) {
            this.skipHeaderRow = value;
            return this;
        }

        public <T> List<T> list() {
            EntityHandler<T> entityHandler;
            if (!Objects.isNull(sheetName) && !Objects.isNull(columnMapping)) {
                entityHandler = new EntityHandler(clazz, sheetName, columnMapping, skipHeaderRow);
            } else if (Objects.isNull(sheetName) && !Objects.isNull(columnMapping)) {
                entityHandler = new EntityHandler(clazz, columnMapping, skipHeaderRow);
            } else if (!Objects.isNull(sheetName) && Objects.isNull(columnMapping)) {
                entityHandler = new EntityHandler(clazz, sheetName, skipHeaderRow);
            } else {
                entityHandler = new EntityHandler(clazz, skipHeaderRow);
            }
            entityHandler.process(file);
            return entityHandler.readAsList();
        }
    }
}
