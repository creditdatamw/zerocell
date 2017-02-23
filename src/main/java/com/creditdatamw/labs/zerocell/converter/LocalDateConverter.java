package com.creditdatamw.labs.zerocell.converter;

import java.time.LocalDate;

/**
 * Created by zikani on 2/22/2017.
 */
public class LocalDateConverter implements  Converter<LocalDate> {
    @Override
    public LocalDate convert(String value) {
        try {
            return LocalDate.parse(value);
        } catch(Exception e) {
            return LocalDate.of(1905, 01, 01);
        }
    }
}
