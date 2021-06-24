package com.creditdatamw.zerocell.annotation;

import com.creditdatamw.zerocell.converter.FallbackStrategy;
import com.creditdatamw.zerocell.converter.NoopConverter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A Column in the excel workbook
 */
@Target({ElementType.FIELD, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    /**
     *
     * @return Name of the column in the excelWorkbook - this must be the value in that column
     */
    String name();

    /**
     *
     * @return Position of the column. Starts with the 0 index.
     */
    int index();

    /**
     *
     * @return Format for the value of the column, iff applicable
     */
    String dataFormat() default "";

    /**
     *
     * @return Format for the value of the column, iff applicable
     */
    FallbackStrategy fallback() default FallbackStrategy.DEFAULT;

    /**
     *
     * @return Converter to use to convert the string value to the field type's value
     */
    Class<?> converterClass() default NoopConverter.class;
}
