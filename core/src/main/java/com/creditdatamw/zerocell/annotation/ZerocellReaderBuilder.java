package com.creditdatamw.zerocell.annotation;

import java.lang.annotation.*;

/**
 * Marker annotation for a class that Generates a ZeroCell Reader
 *
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@Documented
public @interface ZerocellReaderBuilder {
    /**
     * Gets the name of the generated Reader class, which
     * defaults to the name of the class with suffix of "Reader" e.g. PersonReader
     *
     * @return The name of the generated Reader class.
     */
    String value() default "__none__";
}
