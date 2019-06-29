package com.creditdatamw.zerocell;

import com.creditdatamw.zerocell.annotation.Column;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;

public class TestHandler {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testFS701MissingColumnIssue() {
        // A row class with an index skipped must not be allowed
        thrown.expect(ZeroCellException.class);
        thrown.expectMessage(String.format("Expected Column '%s' but found '%s'", "First", "ID"));
        Reader.of(Row.class)
                .from(new File("src/test/resources/test_people.xlsx"))
                .sheet("uploads")
                .list();
    }

    public static class Row {
        @Column(index = 0, name ="First") String first;
        @Column(index = 1, name ="Second") String second;
        @Column(index = 3, name ="Third") String third;

        public Row() {
        }

        public String getFirst() {
            return first;
        }

        public void setFirst(String first) {
            this.first = first;
        }

        public String getSecond() {
            return second;
        }

        public void setSecond(String second) {
            this.second = second;
        }

        public String getThird() {
            return third;
        }

        public void setThird(String third) {
            this.third = third;
        }
    }
}
