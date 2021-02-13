package com.creditdatamw.zerocell.converter;

import org.junit.Test;

import static com.creditdatamw.zerocell.converter.ConverterUtils.convertValueToType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TestConverterUtils {

    @Test
    public void testConvertValuesToString() {
        assertEquals("Hello", convertValueToType(String.class, "Hello", "A", 1));
        assertEquals("HELLO", convertValueToType(String.class, "HELLO", "A", 1));
        assertEquals("1.0", convertValueToType(String.class, "1.0", "A", 1));
        assertEquals("2019-01-01", convertValueToType(String.class, "2019-01-01", "A", 1));
    }

    @Test
    public void testConvertValuesToLong() {
        assertEquals(Long.MIN_VALUE, convertValueToType(Long.class, "Hello", "A", 1));
        assertEquals(Long.MIN_VALUE, convertValueToType(Long.class, "Hello", "A", 1));

        long v = (long) convertValueToType(Long.class, "5000", "A", 1);
        assertEquals(5_000L, v);

        assertEquals(Long.MIN_VALUE, convertValueToType(Long.class, "2 + 2", "A", 1));
        assertEquals(-1L, convertValueToType(Long.class, "-1", "A", 1));
        assertEquals(Long.MIN_VALUE, convertValueToType(Long.class, "1.0", "A", 1));
    }
}
