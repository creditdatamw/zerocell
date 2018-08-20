package com.creditdatamw.zerocell.example;

import com.creditdatamw.zerocell.Reader;
import com.creditdatamw.zerocell.ZeroCellException;
import com.creditdatamw.zerocell.column.ColumnInfo;
import com.creditdatamw.zerocell.column.RowNumberInfo;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

/**
 * This example shows how to load data from an excel sheet without using annotations
 * on the class itself. The column mappings are via the Reader.using method.
 *
 * Also note that zerocell doesn't require that ALL columns in the Excel sheet be
 * mapped to process the sheet.
 *
 */
public class ManualColumnMappingExample {
    /**
     * Main method
     *
     * @param args
     * @throws ZeroCellException
     */
    public static void main(String... args) throws ZeroCellException {
        File file = new File("../core/src/test/resources/test_people.xlsx");

        String sheet = "uploads";

        List<Patient> patients = Reader.of(Patient.class)
                .from(file)
                .sheet(sheet)
                .using(
                        new RowNumberInfo("row", Integer.class),
                        new ColumnInfo("ID", "patientID", 0, String.class),
                        new ColumnInfo("FIRST_NAME", "forename", 1, String.class),
                        new ColumnInfo("MIDDLE_NAME", "otherNames", 2, String.class),
                        new ColumnInfo("LAST_NAME", "surname", 3, String.class),
                        new ColumnInfo("DATE_OF_BIRTH", "dateOfBirth", 4, LocalDate.class)
                )
                .list();

        System.out.println("Patients from the file:");

        for(Patient p: patients) {
            System.out.println(String.format("row:%s data: %s", p.getRow(), p.toString()));
        }
    }
}
