package com.creditdatamw.zerocell.column;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestColumnInfo {
    @Test
    public void testConstructor() {
        ColumnInfo columnInfo = new ColumnInfo("COLUMN 1", "columnOne", 1, Integer.class);

        assertEquals("columnOne", columnInfo.getFieldName());
        assertEquals(Integer.class, columnInfo.getType());
        assertEquals("COLUMN 1", columnInfo.getName());
        assertEquals(1, columnInfo.getIndex());
    }
}
