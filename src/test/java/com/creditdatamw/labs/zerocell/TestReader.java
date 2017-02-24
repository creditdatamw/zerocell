package com.creditdatamw.labs.zerocell;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit Tests for the reader class
 */
public class TestReader {

    @Test
    public void testShouldExtractPeopleFromFile() {
        List<Person> people = Reader.of(Person.class)
                                .from(new File("src/test/resources/test_people.xlsx"))
                                .sheet("uploads")
                                .list();
        assertNotNull(people);
        assertFalse(people.isEmpty());
        assertEquals(5, people.size());

        Person zikani = people.get(0);

        assertEquals(1, zikani.getRowNumber());
        assertEquals("Zikani", zikani.getFirstName());
    }

    @Test
    public void testShouldExtractColumns() {
        String[] columnNames = new String[] {
            "ID", "FIRST_NAME", "MIDDLE_NAME", "LAST_NAME", "DATE_OF_BIRTH", "FAV_NUMBER", "DATE_REGISTERED"
        };

        assertArrayEquals(columnNames, Reader.columnsOf(Person.class));
    }
}
