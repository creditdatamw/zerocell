package com.creditdatamw.zerocell.example;

import com.creditdatamw.zerocell.ZeroCellException;
import com.creditdatamw.zerocell.ZeroCellReader;

import java.io.File;
import java.util.List;

/**
 * This example shows how to load rows from an Excel Sheet into a List via the
 * class generated by the zerocell annotation processor.
 *
 * The class can be found when you build the module in the <code>target/classes/generated</code>
 * directory.
 *
 * The API for this is also very simple and straight forward.
 */
public class AnnotationProcessorExample {
    /**
     * MainEntity method
     *
     * @param args
     * @throws ZeroCellException
     */
    public static void main(String... args) throws ZeroCellException {
        File file = new File("../core/src/test/resources/test_people.xlsx");

        String sheet = "uploads";

        ZeroCellReader<Person> reader = new PersonExcelReader();

        List<Person> people = reader.read(file, sheet);

        System.out.println("People from the file:");

        for(Person p: people) {
            System.out.println(String.format("row:%s data: %s", p.getRow(), p.toString()));
        }
    }
}
