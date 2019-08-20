package com.creditdatamw.zerocell.converter;

import org.junit.Test;

import static com.creditdatamw.zerocell.converter.ConverterUtils.convertValueToType;
import static org.junit.Assert.assertEquals;

public class TestConverterUtils {

    @Test
    public void testConvertValuesToString() {
        assertEquals("Hello", convertValueToType(String.class, "Hello", "A", 1));
        assertEquals("HELLO", convertValueToType(String.class, "HELLO", "A", 1));
        assertEquals("1.0", convertValueToType(String.class, "1.0", "A", 1));
        assertEquals("2019-01-01", convertValueToType(String.class, "2019-01-01", "A", 1));
    }
}
