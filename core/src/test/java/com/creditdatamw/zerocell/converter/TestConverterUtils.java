package com.creditdatamw.zerocell.converter;

import org.junit.Test;

import static com.creditdatamw.zerocell.converter.ConverterUtils.convertValueToType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TestConverterUtils {

    @Test
    public void testConvertValuesToString() {
        assertEquals("Hello", convertValueToType(String.class, "Hello", "A", 1, FallbackStrategy.DEFAULT));
        assertEquals("HELLO", convertValueToType(String.class, "HELLO", "A", 1, FallbackStrategy.DEFAULT));
        assertEquals("1.0", convertValueToType(String.class, "1.0", "A", 1, FallbackStrategy.DEFAULT));
        assertEquals("2019-01-01", convertValueToType(String.class, "2019-01-01", "A", 1, FallbackStrategy.DEFAULT));
    }

    @Test
    public void testConvertValuesToLong() {
        assertNotEquals(Long.MIN_VALUE, convertValueToType(Long.class, "Hello", "A", 1, FallbackStrategy.DEFAULT_TO_NULL));
        assertEquals(null, convertValueToType(Long.class, "Hello", "A", 1, FallbackStrategy.DEFAULT_TO_NULL));

        long v = (long) convertValueToType(Long.class, "Hello", "A", 1, FallbackStrategy.DEFAULT);
        assertEquals(Long.MIN_VALUE, v);

        assertEquals(Long.MIN_VALUE, convertValueToType(Long.class, "2 + 2", "A", 1, FallbackStrategy.DEFAULT));
        assertEquals(-1L, convertValueToType(Long.class, "-1", "A", 1, FallbackStrategy.DEFAULT));
        assertEquals(1L, convertValueToType(Long.class, "1", "A", 1, FallbackStrategy.DEFAULT));
    }
}
