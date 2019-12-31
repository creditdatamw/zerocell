package com.creditdatamw.zerocell;

import com.creditdatamw.zerocell.handler.EntityHandler;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.*;

public class TestReaderUtil {

    @Test
    public void testCanProcessFilePath() {
        final EntityHandler<Person> entityHandler = new EntityHandler<Person>(
            Person.class,
            false,
            0,
            0
        );

        ReaderUtil.process(
            "src/test/resources/test_people.xlsx",
            entityHandler.getSheetName(),
            entityHandler.getEntitySheetHandler()
        );

        List<Person> people = entityHandler.readAsList();

        assertNotNull(people);
        assertFalse(people.isEmpty());
        assertEquals(5, people.size());

        Person zikani = people.get(0);

        assertEquals(1, zikani.getRowNumber());
        assertEquals("Zikani", zikani.getFirstName());
    }

    @Test
    public void testCanProcessFile() {
        final EntityHandler<Person> entityHandler = new EntityHandler<Person>(
            Person.class,
            false,
            0,
            0
        );
        ReaderUtil.process(
            new File("src/test/resources/test_people.xlsx"),
            entityHandler.getSheetName(),
            entityHandler.getEntitySheetHandler()
        );

        List<Person> people = entityHandler.readAsList();

        assertNotNull(people);
        assertFalse(people.isEmpty());
        assertEquals(5, people.size());

        Person zikani = people.get(0);

        assertEquals(1, zikani.getRowNumber());
        assertEquals("Zikani", zikani.getFirstName());
    }

    /**
     * Try to load from the first sheet, but the sheet we want to load from is
     * the "uploads" sheet which is the second sheet of the file.
     */
    @Test
    public void testFailToProcessFirstSheet() {
        final EntityHandler<Person> entityHandler = new EntityHandler<Person>(
                Person.class,
                false,
                0,
                0
        );
        try {
            ReaderUtil.process(
                    new File("src/test/resources/test_people_with_offset_sheet.xlsx"),
                    EntityHandler.DEFAULT_SHEET,
                    entityHandler.getEntitySheetHandler()
            );
            fail("Somehow read the correct sheet.");
        } catch(ZeroCellException e) {
            assertEquals("Expected Column 'ID' but found 'This sheet is the first but not the intended sheet'", e.getMessage());
        }
    }

    @Test
    public void testCanProcessInputStream() throws IOException {
        final EntityHandler<Person> entityHandler = new EntityHandler<Person>(
            Person.class,
            false,
            0,
            0
        );
        ReaderUtil.process(
            Files.newInputStream(
                Paths.get("src", "test", "resources", "test_people.xlsx")),
            entityHandler.getSheetName(),
            entityHandler.getEntitySheetHandler()
        );

        List<Person> people = entityHandler.readAsList();

        assertNotNull(people);
        assertFalse(people.isEmpty());
        assertEquals(5, people.size());

        Person zikani = people.get(0);

        assertEquals(1, zikani.getRowNumber());
        assertEquals("Zikani", zikani.getFirstName());
    }
}
