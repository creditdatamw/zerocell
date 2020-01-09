package com.creditdatamw.zerocell.converter;

public enum FallbackStrategy {
    LEGACY,
    DEFAULT,
    DEFAULT_TO_NULL,
    DEFAULT_TO_ZERO,
    DEFAULT_TO_TRUE,
    DEFAULT_TO_FALSE,
    DEFAULT_TO_EMPTY_STRING,
    DEFAULT_TO_MIN_VALUE,
    THROW_EXCEPTION;
}
