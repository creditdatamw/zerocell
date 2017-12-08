package com.creditdatamw.zerocell;

import com.creditdatamw.zerocell.column.ColumnInfo;
import com.creditdatamw.zerocell.column.RowNumberInfo;
import org.junit.Test;

import java.io.File;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class TestProgrammaticApi {

    @Test
    public void testCanCreateReaderWithConfiguration() {
        List<PersonWithoutAnnotation> people = Reader.of(PersonWithoutAnnotation.class)
                .from(new File("src/test/resources/test_people.xlsx"))
                .using(
                    new RowNumberInfo("rowNumber", Integer.class),
                    new ColumnInfo("ID", "id", 0, String.class),
                    new ColumnInfo("FIRST_NAME", "firstName", 1, String.class),
                    new ColumnInfo("MIDDLE_NAME", "middleName", 2, String.class),
                    new ColumnInfo("LAST_NAME", "lastName", 3, String.class),
                    new ColumnInfo("DATE_OF_BIRTH", "dateOfBirth", 4, LocalDate.class),
                    new ColumnInfo("DATE_REGISTERED", "dateOfRegistration", 6, Date.class),
                    new ColumnInfo("FAV_NUMBER", "favouriteNumber", 5, Integer.class)
                )
                .sheet("uploads")
                .list();
        assertNotNull(people);
        assertFalse(people.isEmpty());
        assertEquals(5, people.size());

        PersonWithoutAnnotation zikani = people.get(0);

        assertEquals(1, zikani.getRowNumber());
        assertEquals("Zikani", zikani.getFirstName());
    }

    public static class PersonWithoutAnnotation {
        private int rowNumber;
        private String id;
        private String firstName;
        private String middleName;
        private String lastName;
        private LocalDate dateOfBirth;
        private Date dateOfRegistration;
        private int favouriteNumber;

        public PersonWithoutAnnotation() { }

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

        public Date getDateOfRegistration() {
            return dateOfRegistration;
        }

        public void setDateOfRegistration(Date dateOfRegistration) {
            this.dateOfRegistration = dateOfRegistration;
        }

        public LocalDate getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public int getFavouriteNumber() {
            return favouriteNumber;
        }

        public void setFavouriteNumber(int favouriteNumber) {
            this.favouriteNumber = favouriteNumber;
        }
    }

}
