package com.creditdatamw.zerocell;

import com.creditdatamw.zerocell.handler.EntityHandler;
import com.creditdatamw.zerocell.column.ColumnInfo;

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

        public ReaderBuilder(Class<T> clazz) {
            this.clazz = clazz;
        }

        public ReaderBuilder from(File file) {
            Objects.requireNonNull(file);
            this.file = file;
            return this;
        }

        public ReaderBuilder sheet(String sheetName) {
            Objects.requireNonNull(sheetName);
            this.sheetName = sheetName;
            return this;
        }

        public <T> List<T> list() {
            EntityHandler<T> entityHandler = Objects.isNull(sheetName) ? new EntityHandler(clazz) : new EntityHandler(clazz, sheetName);
            entityHandler.process(file);
            return entityHandler.readAsList();
        }
    }
}
