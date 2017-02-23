package com.creditdatamw.labs.zerocell;

import com.creditdatamw.labs.zerocell.column.ColumnInfo;
import com.creditdatamw.labs.zerocell.handler.EntityHandler;

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

        public ReaderBuilder(Class<T> clazz) {
            this.clazz = clazz;
        }

        public ReaderBuilder from(File file) {
            Objects.requireNonNull(file);
            this.file = file;
            return this;
        }

        public <T> List<T> list() {
            EntityHandler<T> entityHandler = new EntityHandler(clazz);
            entityHandler.parseExcel(file);
            return entityHandler.readAsList();
        }
    }
}
