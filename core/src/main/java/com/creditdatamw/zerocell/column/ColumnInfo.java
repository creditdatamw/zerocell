package com.creditdatamw.zerocell.column;

import com.creditdatamw.zerocell.annotation.Column;
import com.creditdatamw.zerocell.converter.NoopConverter;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Information about a Column in a Java Bean
 */
public class ColumnInfo {
    private String name;

    private String fieldName;

    private int index;

    private String dataFormat;

    private Class<?> type;

    private Class<?> converterClass;

    public ColumnInfo(String name, String fieldName, int index, Class<?> dataType) {
        this.name = name;
        this.fieldName = fieldName;
        this.index = index;
        this.dataFormat = "";
        this.type = dataType;
        this.converterClass = NoopConverter.class;
    }

    public ColumnInfo(String name, String fieldName, int index, String dataFormat, Class<?> type, Class<?> converterClass) {
        this.name = name;
        this.fieldName = fieldName;
        this.index = index;
        this.dataFormat = dataFormat;
        this.type = type;
        this.converterClass = converterClass;
    }

    public String getName() {
        return name;
    }

    public String getFieldName() {
        return fieldName;
    }

    public int getIndex() {
        return index;
    }

    public String getDataFormat() {
        return dataFormat;
    }

    public Class<?> getType() {
        return type;
    }

    public Class<?> getConverterClass() {
        return converterClass;
    }

    public static final List<Column> annotationsOf(Class<?> clazz) {
        Field[] fieldArray = clazz.getDeclaredFields();
        List<Column> columns = new ArrayList<>(fieldArray.length);
        for (Field field : fieldArray) {
            Column annotation = field.getAnnotation(Column.class);
            if (!Objects.isNull(annotation)) {
                columns.add(annotation);
            }
        }
        return columns;
    }

    public static final String[] columnsOf(Class<?> clazz) {
        List<Column> columns = annotationsOf(clazz);
        String[] columnNames = new String[columns.size()];

        LoggerFactory.getLogger(ColumnInfo.class).debug(String.format("Found %s columns in class %s", columnNames.length, clazz.getName()));

        for(Column annotation: columns) {
            columnNames[annotation.index()] = annotation.name().trim();
        }
        columns = null;
        return columnNames;
    }
}
