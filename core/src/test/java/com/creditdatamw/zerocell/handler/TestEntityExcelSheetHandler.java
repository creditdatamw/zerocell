package com.creditdatamw.zerocell.handler;

import com.creditdatamw.zerocell.Person;
import com.creditdatamw.zerocell.column.ColumnMapping;

/**
 * Tests for the EntityExcelSheetHandler
 */
public class TestEntityExcelSheetHandler {

    // @Test
    public void testHandlerWithDefaults() {
        EntityHandler<Person> entityHandler = new EntityHandler<>(
            Person.class,
            true,
            0,
            0
        );

        ColumnMapping mapping = ColumnMapping.parseColumnMappingFromAnnotations(Person.class);
        EntityExcelSheetHandler<Person> sheetHandler =
            new EntityExcelSheetHandler<Person>(
                entityHandler,
                mapping.getRowNumberInfo(),
                mapping.getColumnsMap()
            );
        // TODO: test the sheet handler's methods
    }
}
