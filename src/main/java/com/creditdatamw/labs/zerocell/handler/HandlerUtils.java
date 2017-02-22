package com.creditdatamw.labs.zerocell.handler;

import java.time.LocalDate;

/**
 * Utilities for the Reader Class
 */
public class HandlerUtils {

    public static LocalDate parseAsLocalDate(String columnName, int rowNum, String value) {
        try {
            return LocalDate.parse(value);
        } catch(Exception e) {

        }
        return null;
    }
}
