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
        List<String> columnNames = new ArrayList<>(fieldArray.length);
        for (Field field: fieldArray) {
            Column annotation = field.getAnnotation(Column.class);
            if (! Objects.isNull(annotation)) {
                int index = annotation.index();
                if (! Objects.isNull(columnNames.get(index))) {
                    throw new ZeroCellException("Cannot map two columns to one index: " + index);
                }
                columnNames.set(index, annotation.name());
            }
        }
        String[] arr = new String[columnNames.size()];
        return columnNames.toArray(arr);
    }
}
