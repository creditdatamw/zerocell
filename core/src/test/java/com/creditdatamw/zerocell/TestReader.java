package com.creditdatamw.zerocell;


import com.creditdatamw.zerocell.annotation.Column;
import com.creditdatamw.zerocell.annotation.RowNumber;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit Tests for the reader class
 */
public class TestReader {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

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
    public void testShouldSkipFirst3RowsFromFile() {
        List<Person> people = Reader.of(Person.class)
                                .from(new File("src/test/resources/test_people_with_offset_header.xlsx"))
                                .sheet("uploads")
                                .skipFirstNRows(3)
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

    @Test
    public void testShouldThrowForNonOpenXMLFile() throws IOException {
        File file = temporaryFolder.newFile();
        thrown.expect(ZeroCellException.class);
        thrown.expectMessage("Cannot load file. The file must be an Excel 2007+ Workbook (.xlsx)");
        Reader.of(Person.class)
                .from(file)
                .sheet("uploads")
                .list();
    }

    public static class DuplicateIndex {
        @Column(name = "ID", index = 0)
        private String id1;

        @Column(name = "ID 2", index = 0)
        private String id2;

        public DuplicateIndex() {
        }

        public String getId1() {
            return id1;
        }

        public void setId1(String id1) {
            this.id1 = id1;
        }

        public String getId2() {
            return id2;
        }

        public void setId2(String id2) {
            this.id2 = id2;
        }
    }

    public static class Person2 {
        @RowNumber
        private int rowNumber;

        @Column(name= "ID", index=0)
        private String id;

        @Column(name = "FIRST_NAME", index = 1)
        private String firstName;

        @Column(name = "MIDDLE_NAME", index = 2)
        private String middleName;

        @Column(name = "LAST_NAME", index = 3)
        private String lastName;

        @Column(name = "DOB", index = 4)
        private LocalDate dateOfBirth;

        @Column(name = "DATE_REGISTERED", index = 6)
        private Date dateOfRegistration;

        @Column(name = "FAV_NUMBER", index = 5)
        private int favouriteNumber;

        public Person2() {
        }

        public int getRowNumber() {
            return rowNumber;
        }

        public void setRowNumber(int rowNumber) {
            this.rowNumber = rowNumber;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getMiddleName() {
            return middleName;
        }

        public void setMiddleName(String middleName) {
            this.middleName = middleName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public LocalDate getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public Date getDateOfRegistration() {
            return dateOfRegistration;
        }

        public void setDateOfRegistration(Date dateOfRegistration) {
            this.dateOfRegistration = dateOfRegistration;
        }

        public int getFavouriteNumber() {
            return favouriteNumber;
        }

        public void setFavouriteNumber(int favouriteNumber) {
            this.favouriteNumber = favouriteNumber;
        }
    }
}
