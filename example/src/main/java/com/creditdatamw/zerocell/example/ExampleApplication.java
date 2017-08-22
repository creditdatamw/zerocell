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
        File file = new File("src/main/resources/test_people.xlsx");
        String sheet = "uploads";
        ZeroCellReader<Person> reader = new com.creditdatamw.zerocell.example.PersonReader();
        ReaderUtil.process(file, sheet, reader);
        List<Person> people = reader.read();
        for(Person p: people) {
            System.out.println(String.format("row:%s data: %s", p.getRowNumber(), p.toString()));
        }
    }
}
