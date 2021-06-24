package com.creditdatamw.zerocell.converter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestBooleanConverter {
    private BooleanConverter converter = new BooleanConverter();
    @Test
    public void testBooleanConverter() {
        assertEquals(false, converter.withFallbackStrategy(FallbackStrategy.DEFAULT)
                .convert("false", "TEST_COLUMN", 1));
    }
}
