package com.creditdatamw.zerocell.column;

import com.creditdatamw.zerocell.annotation.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Information about a Column in a Java Bean
 */
@Getter
@AllArgsConstructor
public class ColumnInfo {
    private String name;

    private String fieldName;

    private int index;

    private String dataFormat;

    private Class<?> type;

    private Class<?> converterClass;

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
