package com.creditdatamw.zerocell;

import com.creditdatamw.zerocell.column.ColumnInfo;
import com.creditdatamw.zerocell.column.RowNumberInfo;
import org.junit.Test;

import java.io.File;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class TestSkipEmptyRows {

    @Test
    public void testCanSkipEmptyRowsWithProgrammaticAPI() {
        List<Person> people = Reader.of(Person.class)
                .from(new File("src/test/resources/test_people_with_empty_rows.xlsx"))
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
                .skipEmptyRows(true)
                .list();
        assertNotNull(people);
        assertFalse(people.isEmpty());
        assertEquals(5, people.size());

        Person zikani = people.get(0);

        assertEquals(1, zikani.getRowNumber());
        assertEquals("Zikani", zikani.getFirstName());
    }

    @Test
    public void testCanSkipEmptyRows() {
        List<Person> people = Reader.of(Person.class)
                .from(new File("src/test/resources/test_people_with_empty_rows.xlsx"))
                .sheet("uploads")
                .skipEmptyRows(true)
                .list();
        assertNotNull(people);
        assertFalse(people.isEmpty());
        assertEquals(5, people.size());

        Person zikani = people.get(0);

        assertEquals(1, zikani.getRowNumber());
        assertEquals("Zikani", zikani.getFirstName());
    }


}
