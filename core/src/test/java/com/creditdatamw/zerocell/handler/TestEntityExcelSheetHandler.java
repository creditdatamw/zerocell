package com.creditdatamw.zerocell.handler;

import com.creditdatamw.zerocell.Person;
import com.creditdatamw.zerocell.column.ColumnInfo;
import com.creditdatamw.zerocell.column.ColumnMapping;
import org.junit.Test;

/**
 * Tests for the EntityExcelSheetHandler
 */
public class TestEntityExcelSheetHandler {

    @Test
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
                null // mapping.getColumns().toArray()
            );

    }
}
