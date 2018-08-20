package com.creditdatamw.zerocell.example;

import com.creditdatamw.zerocell.Reader;
import com.creditdatamw.zerocell.ReaderUtil;
import com.creditdatamw.zerocell.ZeroCellException;
import com.creditdatamw.zerocell.ZeroCellReader;

import java.io.File;
import java.util.List;

/**
 * This Basic Example shows how to load rows from an Excel Sheet into a List.
 * As you can see, it's really straight forward to read the rows.
 *
 */
public class BasicUsageExample {
    /**
     * Main method
     *
     * @param args
     * @throws ZeroCellException
     */
    public static void main(String... args) throws ZeroCellException {
        File file = new File("../core/src/test/resources/test_people.xlsx");

        String sheet = "uploads";

        List<Person> people = Reader.of(Person.class)
                .from(file)
                .sheet(sheet)
                .list();

        System.out.println("People from the file:");

        for(Person p: people) {
            System.out.println(String.format("row:%s data: %s", p.getRow(), p.toString()));
        }
    }
}
