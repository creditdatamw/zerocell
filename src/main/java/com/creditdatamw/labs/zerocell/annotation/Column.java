package com.creditdatamw.labs.zerocell.annotation;

import com.creditdatamw.labs.zerocell.converter.NoopConverter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * A Column in the excel workbook
 */
@Target({ElementType.FIELD, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    /**
     * Name of the column in the excelWorkbook - this must be the value in that column
     * @return
     */
    String name();

    /**
     * Position of the column. Starts with the 0 index.
     * @return
     */
    int index();

    /**
     * Format for the value of the column
     * @return
     */
    String dataFormat() default "";

    /**
     * Converter to use to convert the string value to the field type's value
     * @return
     */
    Class<?> convertorClass() default NoopConverter.class;
}
