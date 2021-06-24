package com.creditdatamw.zerocell.internal;

import com.creditdatamw.zerocell.converter.FallbackStrategy;

/**
 * This is a special exception that accompanies the {@link FallbackStrategy#IGNORE}
 * strategy. When this exception is thrown, the EntitySheetHandler MUST not
 * attempt to set the value on the field.
 */
public class IgnoreInvalidValueException extends RuntimeException {
}
