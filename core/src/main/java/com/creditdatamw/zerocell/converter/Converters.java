package com.creditdatamw.zerocell.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Common Converters
 *
 * The converters will throw a ZeroCellException in case of failure to parse the
 * provided.
 */
public class Converters {
    private static final Logger LOGGER = LoggerFactory.getLogger(Converters.class);

    public static final Converter<Float> toFloat = ((formattedValue, columnName, row) -> {
        try {
            return Float.valueOf(formattedValue);
        } catch (Exception e) {
            LOGGER.error("Failed to parse {} as Float. Using default of null at column={} row={} ", formattedValue, columnName, row);
            return null;
        }
    });

    public static final Converter<String> noop = ((formattedValue, columnName, row) -> {
        return formattedValue;
    });

    public static final Converter<LocalDateTime> toLocalDateTime = ((formattedValue, columnName, rowNum) -> {
        try {
            return LocalDateTime.parse(formattedValue);
        } catch(Exception e) {
            LOGGER.error("Failed to parse {} as Date. Using default of null at column={} row={} ", formattedValue, columnName, rowNum);
            return null;
        }
    });


    public static final Converter<LocalDate> toLocalDate = ((formattedValue, columnName, rowNum) -> {
        try {
            return LocalDate.parse(formattedValue);
        } catch(Exception e) {
            LOGGER.error("Failed to parse {} as Date. Using default of null at column={} row={} ", formattedValue, columnName, rowNum);
            return null;
        }
    });

    public static final Converter<java.sql.Date> toSqlDate = ((formattedValue, columnName, rowNum) -> {
        try {
            return Objects.isNull(formattedValue) ? null : Date.valueOf(formattedValue);
        } catch (Exception e) {
            LOGGER.error("Failed to parse {} as Date. Using default of null at column={} row={} ", formattedValue, columnName, rowNum);
            return null;
        }
    });

    public static final Converter<Timestamp> toSqlTimestamp = ((formattedValue, columnName, rowNum) -> {
        try {
            return Timestamp.valueOf(formattedValue);
        } catch (Exception e) {
            LOGGER.error("Failed to parse {} as Timestamp. Using default of null at column={} row={} ", formattedValue, columnName, rowNum);
            return null;
        }
    });

    public static final Converter<Integer> toInteger = ((formattedValue, columnName, rowNum) -> {
        try {
            return Integer.valueOf(formattedValue == null ? "0.0" : formattedValue);
        } catch (Exception e) {
            LOGGER.error("Failed to parse {} as Integer. Using default of null at column={} row={} ", formattedValue, columnName, rowNum);
            return null;
        }
    });

    public static final Converter<Long> toLong = ((formattedValue, columnName, rowNum) -> {
        //} else if (fieldType == Long.class || fieldType == long.class) {
        try {
            return Long.valueOf(formattedValue);
        } catch (Exception e) {
            LOGGER.error("Failed to parse {} as Long. Using default of null at column={} row={} ", formattedValue, columnName, rowNum);
            return null;
        }
    });

    public static final Converter<Double> toDouble = ((formattedValue, columnName, rowNum) -> {
        //  } else if (fieldType == Double.class || fieldType == double.class) {
        try {
            return Double.valueOf(formattedValue);
        } catch (Exception e) {
            LOGGER.error("Failed to parse {} as Double. Using default of null at column={} row={} ", formattedValue, columnName, rowNum);
            return null;
        }
    });

    public static final Converter<Boolean> toBoolean = ((formattedValue, columnName, rowNum) -> {
        try {
            return Boolean.valueOf(formattedValue == null ? "FALSE" : "TRUE");
        } catch(Exception e) {
            LOGGER.error("Failed to parse {} as Boolean. Using default of false at column={} row={} ", formattedValue, columnName, rowNum);
            return Boolean.FALSE;
        }
    });

}
