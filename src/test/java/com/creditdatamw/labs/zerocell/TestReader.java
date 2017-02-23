package com.creditdatamw.labs.zerocell;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Unit Tests for the reader class
 */
public class TestReader {

    @Test
    public void testShouldExtractPeopleFromFile() {
        List<Person> people = Reader.of(Person.class)
                                .from(new File("src/test/resources/test_people.xlsx"))
                                .list();
        assertNotNull(people);
        assertFalse(people.isEmpty());
        assertEquals(5, people.size());

        Person zikani = people.get(0);

        assertEquals(1, zikani.getRowNumber());
        assertEquals("Zikani", zikani.getFirstName());
    }

}
