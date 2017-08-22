package com.creditdatamw.zerocell;

/**
 * RuntimeException occurred
 */
public class ZeroCellException extends RuntimeException {
    public ZeroCellException(String message) {
        super(message);
    }

    public ZeroCellException(String message, Throwable cause) {
        super(message, cause);
    }

    public ZeroCellException(Throwable cause) {
        super(cause);
    }

    public ZeroCellException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
