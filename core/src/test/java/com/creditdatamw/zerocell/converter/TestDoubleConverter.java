package com.creditdatamw.zerocell.converter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TestDoubleConverter {

    @Test
    public void testShouldConvertToMinValue() {
        DefaultConverter<Double> converter = new DoubleConverter()
                .withFallbackStrategy(FallbackStrategy.DEFAULT_TO_MIN_VALUE);
        double val = converter.convert(null, "COLUMN_NAME", 1);
        assertEquals(Double.MIN_VALUE, val, 0.0);

        val = converter.convert("", "COLUMN_NAME", 1);
        assertEquals(Double.MIN_VALUE, val, 0.0);

        val = converter.convert("not a number", "COLUMN_NAME", 1);
        assertEquals(Double.MIN_VALUE, val, 0.0);

        val = converter.convert("1.0", "COLUMN_NAME", 1);
        assertNotEquals(Double.MIN_VALUE, val, 0.0);
        assertEquals(1.0, val, 0.0);
    }
}
