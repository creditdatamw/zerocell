package com.creditdatamw.zerocell.handler;

import com.creditdatamw.zerocell.Person;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestEntityHandler {

    @Test
    public void testHandlerWithDefaults() {
        EntityHandler<Person> entityHandler = new EntityHandler<>(
            Person.class,
            true,
            0,
            0
        );

        assertEquals(Person.class, entityHandler.getEntityClass());
        assertEquals("ZEROCELL_READ_FIRST_SHEET_0", entityHandler.getSheetName());
        assertTrue(entityHandler.isSkipHeaderRow());
        assertEquals(0, entityHandler.getSkipFirstNRows());
    }
}
