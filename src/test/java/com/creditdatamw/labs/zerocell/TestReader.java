package com.creditdatamw.labs.zerocell;

import com.creditdatamw.labs.zerocell.annotation.Column;
import com.creditdatamw.labs.zerocell.annotation.RowNumber;
import com.creditdatamw.labs.zerocell.converter.LocalDateConverter;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.io.File;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit Tests for the reader class
 */
public class TestReader {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

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

    @Test
    public void testShouldThrowOnInvalidSheetName() {
        thrown.expect(ZeroCellException.class);
        thrown.expectMessage("Could not find sheet INVALID_SHEET_NAME");
        Reader.of(Person.class)
                .from(new File("src/test/resources/test_people.xlsx"))
                .sheet("INVALID_SHEET_NAME")
                .list();
    }

    @Test
    public void testShouldThrowOnDuplicateIndex() {
        thrown.expect(ZeroCellException.class);
        thrown.expectMessage("Cannot map two columns to the same index: 0");
        Reader.of(DuplicateIndex.class)
                .from(new File("src/test/resources/test_people.xlsx"))
                .sheet("uploads")
                .list();
    }

    @Test
    public void testShouldThrowOnIncorrectColumnName() {
        thrown.expect(ZeroCellException.class);
        thrown.expectMessage("Expected Column 'DOB' but found 'DATE_OF_BIRTH'");
        Reader.of(Person2.class)
                .from(new File("src/test/resources/test_people.xlsx"))
                .sheet("uploads")
                .list();
    }

    @Data
    public static class DuplicateIndex {
        @Column(name = "ID", index = 0)
        private String id1;

        @Column(name = "ID 2", index = 0)
        private String id2;
    }

    @Data
    public static class Person2 {
        @RowNumber
        private int rowNumber;

        @Column(name= "ID", index=0)
        private String id;

        @NotEmpty
        @Column(name = "FIRST_NAME", index = 1)
        private String firstName;

        @NotEmpty
        @Column(name = "MIDDLE_NAME", index = 2)
        private String middleName;

        @NotEmpty
        @Column(name = "LAST_NAME", index = 3)
        private String lastName;

        @NotNull
        @Past
        @Column(name = "DOB", index = 4, convertorClass = LocalDateConverter.class)
        private LocalDate dateOfBirth;

        @NotNull
        @Past
        @Column(name = "DATE_REGISTERED", index = 6)
        private Date dateOfRegistration;

        @Column(name = "FAV_NUMBER", index = 5)
        private int favouriteNumber;
    }
}
