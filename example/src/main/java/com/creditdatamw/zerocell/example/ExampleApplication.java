package com.creditdatamw.zerocell.example;

import com.creditdatamw.zerocell.ReaderUtil;
import com.creditdatamw.zerocell.ZeroCellReader;

import java.io.File;
import java.util.List;

/**
 * Test for a PersonReader generator
 */
public class ExampleApplication {
    /**
     * PersonExample Class for tests
     */
    public static void main(String... args) {
        File file = new File("../core/src/test/resources/test_people.xlsx");
        String sheet = "uploads";
        ZeroCellReader<Person> reader = new com.creditdatamw.zerocell.example.PersonExcelReader();
        List<Person> people = reader.read(file, sheet);
        System.out.println("People from the file:");
        for(Person p: people) {
            System.out.println(String.format("row:%s data: %s", p.getRowNumber(), p.toString()));
        }
    }
}
