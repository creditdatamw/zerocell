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
 */
public class Converters {
    private static final Logger LOGGER = LoggerFactory.getLogger(Converters.class);

    public static final Converter<Float> toFloat = ((formattedValue, columnName, row) -> {
        try {
            return Float.valueOf(formattedValue == null ? "0.0" : formattedValue);
        } catch (Exception e) {
            LOGGER.error("Failed to parse {} as float. Using default of -1 at column={} row={} ", formattedValue, columnName, row);
            return new Float(-1.0);
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
            return LocalDateTime.of(1905, 01, 01, 0, 0, 0);
        }
    });


    public static final Converter<LocalDate> toLocalDate = ((formattedValue, columnName, rowNum) -> {
        try {
            return LocalDate.parse(formattedValue);
        } catch(Exception e) {
            LOGGER.error("Failed to parse {} as Date. Using default of null at column={} row={} ", formattedValue, columnName, rowNum);
            return LocalDate.of(1905, 01, 01);
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

    public static final Converter<Timestamp> toSqlTimestamp = ((formattedValue, columnName, row) -> {
        try {
            return Timestamp.valueOf(formattedValue == null ? "1905-01-01" : formattedValue);
        } catch (Exception e) {
            return null;
        }
    });

    public static final Converter<Integer> toInteger = ((formattedValue, columnName, rowNum) -> {
        try {
            return Integer.valueOf(formattedValue == null ? "0.0" : formattedValue);
        } catch (Exception e) {
            LOGGER.error("Failed to parse {} as integer. Using default of -1 at column={} row={} ", formattedValue, columnName, rowNum);
            return Integer.valueOf(-1);
        }
    });

    public static final Converter<Long> toLong = ((formattedValue, columnName, row) -> {
        //} else if (fieldType == Long.class || fieldType == long.class) {
        try {
            return Long.valueOf(formattedValue == null ? "0" : formattedValue);
        } catch (Exception e) {
            // LOGGER.error("Failed to parse {} as long. Using default of -1 at column={} row={} ", formattedValue, columnName, rowNum);
            return Long.valueOf(-1);
        }
    });

    public static final Converter<Double> toDouble = ((formattedValue, columnName, row) -> {
        //  } else if (fieldType == Double.class || fieldType == double.class) {
        try {
            return Double.valueOf(formattedValue == null ? "0.0" : formattedValue);
        } catch (Exception e) {
            // LOGGER.error("Failed to parse {} as double. Using default of -1 at column={} row={} ", formattedValue, columnName, rowNum);
            return Double.valueOf(-1.0);
        }
    });

    public static final Converter<Boolean> toBoolean = ((formattedValue, columnName, rowNum) -> {
        try {
            return Boolean.valueOf(formattedValue == null ? "FALSE" : "TRUE");
        } catch(Exception e) {
            //LOGGER.error("Failed to parse {} as Boolean. Using default of null at column={} row={} ", formattedValue, columnName, rowNum);
            return Boolean.FALSE;
        }
    });

}
