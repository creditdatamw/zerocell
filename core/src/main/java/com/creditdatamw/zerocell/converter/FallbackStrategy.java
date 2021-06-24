package com.creditdatamw.zerocell.converter;

public enum FallbackStrategy {
    /**
     * Uses the LEGACY approach for handling with failures, which is basically
     * setting the field to <code>null</code> for reference types and
     * <code>0</code> for numeric types.
     *
     * @deprecated this should be considered deprecated
     */
    LEGACY,
    /**
     * Applies the default Fallback Strategy for the field.
     */
    DEFAULT,
    /**
     * Ignores the invalid value from the Excel Cell -
     * does not set anything on the field. Basically skips the field.
     */
    IGNORE,
    /**
     * Sets the field on the field to null. Regardless of type.
     */
    DEFAULT_TO_NULL,
    /**
     * Sets the field to <code>0</code> if the value from the Excel cell
     * cannot be parsed as a numeric value.
     * <em>Applies only to numeric types.</em>
     */
    DEFAULT_TO_ZERO,
    /**
     * Sets the field to <code>true</code> if the value from the Excel cell
     * cannot be parsed as a boolean value.
     * <em>Applies only to Boolean types.</em>
     */
    DEFAULT_TO_TRUE,
    /**
     * Sets the field to <code>false</code> if the value from the Excel cell
     * cannot be parsed as a boolean value.
     * <em>Applies only to Boolean types.</em>
     */
    DEFAULT_TO_FALSE,
    /**
     * Sets the field to an empty String (<code>""</code>) if the value from
     * the Excel cell cannot be parsed. <em>Applies only to String fields.</em>
     */
    DEFAULT_TO_EMPTY_STRING,
    /**
     * Sets the field to the MIN_VALUE of the Field type.
     * <ul>
     *     <li>Long.MIN_VALUE for long, Long fields e.g. <code>private long id;</code></li>
     *     <li>Integer.MIN_VALUE for int, Integer fields e.g. <code>private int id;</code></li>
     *     <li>Double.MIN_VALUE for double, Double fields e.g. <code>private double amount;</code></li>
     *     <li>Float.MIN_VALUE for float, Float fields e.g. <code>private float id;</code></li>
     * </ul>
     *
     */
    DEFAULT_TO_MIN_VALUE,
    /**
     * Throws a {@link com.creditdatamw.zerocell.ZeroCellException} if the cell
     * value cannot be parsed and converted properly.
     * <p><strong>NOTE:</strong>The library will NOT process the rest of the
     * row (and File, actually) when this occurs.</p>
     */
    THROW_EXCEPTION;
}
