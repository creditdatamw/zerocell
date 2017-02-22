package com.creditdatamw.labs.zerocell.column;

import com.creditdatamw.labs.zerocell.converter.Converter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Value;

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
}
