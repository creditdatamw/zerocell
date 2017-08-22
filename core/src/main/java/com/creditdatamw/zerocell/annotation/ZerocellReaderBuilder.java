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
     * The name of the generated Reader class.
     * Defaults to the name of the class with suffix of "Reader" e.g. PersonReader
     *
     * @return
     */
    String name() default "";
}
