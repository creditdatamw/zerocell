package com.creditdatamw.zerocell.converter;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Common Converters
 *
 * The converters will throw a ZeroCellException in case of failure to parse the
 * provided.
 */
public class DefaultConverters {
    DefaultConverters() {}

    public final DefaultConverter<String> noop = new DefaultConverter<String>(){
        @Override
        public String convert(String value, String columnName, int row) {
            FallbackStrategy s = this.getFallbackStrategy();

            if (value == null &&
                    (s == FallbackStrategy.DEFAULT_TO_EMPTY_STRING ||
                            s == FallbackStrategy.DEFAULT)) {

                return "";
            }
            return value;
        }
    };

    public final DefaultConverter<Float> toFloat = new FloatConverter()
            .withFallbackStrategy(FallbackStrategy.DEFAULT);
    public final DefaultConverter<LocalDateTime> toLocalDateTime = new LocalDateTimeConverter()
            .withFallbackStrategy(FallbackStrategy.DEFAULT);
    public final DefaultConverter<LocalDate> toLocalDate = new LocalDateConverter()
            .withFallbackStrategy(FallbackStrategy.DEFAULT);
    public final DefaultConverter<Date> toSqlDate = new SqlDateConverter()
            .withFallbackStrategy(FallbackStrategy.DEFAULT);
    public final DefaultConverter<Timestamp> toSqlTimestamp = new SqlTimestampConverter()
            .withFallbackStrategy(FallbackStrategy.DEFAULT);
    public final DefaultConverter<Integer> toInteger = new IntegerConverter()
            .withFallbackStrategy(FallbackStrategy.DEFAULT);
    public final DefaultConverter<Long> toLong = new LongConverter()
            .withFallbackStrategy(FallbackStrategy.DEFAULT);
    public final DefaultConverter<Double> toDouble = new DoubleConverter()
            .withFallbackStrategy(FallbackStrategy.DEFAULT);
    public final DefaultConverter<Boolean> toBoolean = new BooleanConverter()
            .withFallbackStrategy(FallbackStrategy.DEFAULT);
}
