package com.creditdatamw.zerocell.converter;


abstract class DefaultConverter<T> implements Converter<T> {
    private FallbackStrategy fallbackStrategy;

    public DefaultConverter() {
        this.fallbackStrategy = FallbackStrategy.DEFAULT;
    }

    public DefaultConverter(FallbackStrategy fallbackStrategy) {
        this.fallbackStrategy = fallbackStrategy;
    }

    public DefaultConverter<T> withFallbackStrategy(FallbackStrategy fallbackStrategy) {
        this.fallbackStrategy = fallbackStrategy;
        return this;
    }

    public FallbackStrategy getFallbackStrategy() {
        return fallbackStrategy;
    }
}
