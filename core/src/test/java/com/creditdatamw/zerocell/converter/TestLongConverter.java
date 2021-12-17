package com.creditdatamw.zerocell.converter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TestLongConverter {
    @Test
    public void testConvertValuesToLong() {
        DefaultConverter<Long> converter = new LongConverter()
                .withFallbackStrategy(FallbackStrategy.DEFAULT);

        long got = converter.convert("Hello", "A", 1);
        assertEquals(Long.MIN_VALUE, got, 0.0);

    }

    /**
     * switch (this.getFallbackStrategy()) {
     *                 case LEGACY:
     *                 case DEFAULT_TO_NULL:
     *                     return null;
     *                 case DEFAULT_TO_ZERO:
     *                     return 0L;
     *                 case THROW_EXCEPTION:
     *                     throw new ZeroCellException(String.format(message, value, columnName, row));
     *                 case IGNORE:
     *                     throw new IgnoreInvalidValueException();
     *                 case DEFAULT_TO_MIN_VALUE:
     *                 case DEFAULT:
     *                 default:
     *                     return Long.MIN_VALUE;
     *             }
     */
}
