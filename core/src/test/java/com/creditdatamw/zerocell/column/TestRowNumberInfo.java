package com.creditdatamw.zerocell.column;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestRowNumberInfo {
    @Test
    public void testConstructor() {
        RowNumberInfo rowNumberInfo = new RowNumberInfo("rowNo", Integer.class);

        assertEquals("rowNo", rowNumberInfo.getFieldName());
        assertEquals(Integer.class, rowNumberInfo.getType());
        assertEquals("__ROWNUMBER__", rowNumberInfo.getName());
    }
}
