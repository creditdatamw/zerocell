package com.creditdatamw.labs.zerocell;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertFalse;

/**
 * Unit Tests for the reader class
 */
public class TestReader {

    @Test
    public void testShouldExtractPeopleFromFile() {
        List<Person> people = Reader.of(Person.class)
                                .from(new File("src/test/resources/test_people.xlsx"))
                                .list();
        Assert.assertNotNull(people);
        assertFalse(people.isEmpty());
    }

}
