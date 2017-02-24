package com.creditdatamw.labs.zerocell.column;

import com.creditdatamw.labs.zerocell.ZeroCellException;
import com.creditdatamw.labs.zerocell.annotation.Column;
import com.creditdatamw.labs.zerocell.annotation.RowNumber;
import com.creditdatamw.labs.zerocell.converter.Converter;
import com.creditdatamw.labs.zerocell.converter.NoopConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Value;
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

    public static final String[] columnsOf(Class<?> clazz) {
        Field[] fieldArray = clazz.getDeclaredFields();
        List<Column> columns = new ArrayList<>(fieldArray.length);
        for (Field field: fieldArray) {
            Column annotation = field.getAnnotation(Column.class);
            if (! Objects.isNull(annotation)) {
                columns.add(annotation);
            }
        }
        String[] columnNames = new String[columns.size()];

        System.out.println(String.format("Found %s columns in class %s", columnNames.length, clazz.getName()));
        for(Column annotation: columns) {
            columnNames[annotation.index()] = annotation.name();
        }
        columns = null;
        return columnNames;
    }
}
