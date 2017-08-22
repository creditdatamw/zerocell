package com.creditdatamw.zerocell;

public class SheetNotFoundException extends Exception {
    public SheetNotFoundException(String message) {
        super(String.format("Could not find sheet %s", message));
    }
}