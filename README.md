[![](https://img.shields.io/github/license/creditdatamw/zerocell.svg)](./LICENSE)
[![](https://img.shields.io/maven-central/v/com.creditdatamw.labs/zerocell-core.svg)](http://mvnrepository.com/artifact/com.creditdatamw.labs/zerocell-core)

ZeroCell
========

ZeroCell provides a simple API for loading data from Excel sheets into 
Plain Old Java Objects (POJOs) using annotations to map columns from an Excel sheet 
to fields in Java classes. 

In case you don't fancy annotations or don't want to have to change your existing classes, 
you can map the columns to the fields without the annotations.

## Why should I use this?

The library doesn't use the same approach that Apache POIs usermodel API and 
other POI based libraries use to process/store data loaded from the Excel file 
as a result it uses less resources as it doesn't process things such as Cell styles that take up memory.
You also don't have to spend time setting data from cells to your Java objects, just
define the mappings and let ZeroCell handle the rest.

## What ZeroCell _cannot_ do for you

* Read or process excel workbook styles and other visual effects
* Load data into complex object hierarchies
* Write to excel files: The Apache POI library (which we use underneath) has a good API for writing to Excel files and provides the `SXSSFWorkbook` for writing large files in an efficient manner.

## Usage

There are three ways to use zerocell: via annotations, the programmatic api and using the annotation processor.

First things first, add the following dependency to your `pom.xml`

```xml
<dependency>
    <groupId>com.creditdatamw.labs</groupId>
    <artifactId>zerocell-core</artifactId>
    <version>0.3.2</version>
</dependency>
```

### Using Annotations

You create a class with `@Column` (and optionally `@RowNumber`) 
annotations to represent a row in an Excel sheet and
then use the static methods on the `Reader` class to read the 
list of data from the file.

For example:

```java
public class Person {
    @RowNumber
    private int rowNumber;
    
    @Column(index=0, name="FIRST_NAME")
    private String firstName;
    
    @Column(index=1, name="LAST_NAME")
    private String lastName;
    
    @Column(index=2, name="DATE_OF_BIRTH")
    private LocalDate dateOfBirth;
    
    // Getters and setters here ...
    
    public static void main(String... args) {
        // Then using the `Reader` class you can load 
        // a list from the excel file as follows:
        List<Person> people = Reader.of(Person.class)
                            .from(new File("people.xlsx"))
                            .sheet("Sheet 1")
                            .list();
        
        // You can also inspect the column names of 
        // the class using the static `columnsOf` method:
        String[] columns = Reader.columnsOf(Person.class);    
    }
}
```

### Using the Programmatic API

If you don't want to use annotations you can still use ZeroCell to load from Excel sheet
to your Java objects without too much work. The only difference with the annotation approach
 is that you have to define the column mappings via the `Reader.using` method.

For example:

```java
public class Person {
    private int rowNo;
    
    private String id;
	
    private String firstName;
    
    private String middleName;
	
    private String lastName;
    
    private LocalDate dateOfBirth;
	
    private LocalDate dateOfRegistration;
    
    // Getters and setters here ...
    
    public static void main(String... args) {
        // Map the columns using, Reader.using method here
        List<Person> people = Reader.of(Person.class)
                            .from(new File("people.xlsx"))                            
                            .using(
                                new RowNumberInfo("rowNo", Integer.class),
                                new ColumnInfo("ID", "id", 0, String.class),
                                new ColumnInfo("FIRST_NAME", "firstName", 1, String.class),
                                new ColumnInfo("MIDDLE_NAME", "middleName", 2, String.class),
                                new ColumnInfo("LAST_NAME", "lastName", 3, String.class),
                                new ColumnInfo("DATE_OF_BIRTH", "dateOfBirth", 4, LocalDate.class),
                                new ColumnInfo("DATE_REGISTERED", "dateOfRegistration", 5, Date.class)
                            )
                            .sheet("Sheet 1")
                            .list();
         
        people.forEach(person -> {
            // Do something with person here    
        });    
    }
}
```

### Using the Annotation Processor

ZeroCell provides an annotation processor to generate Reader 
classes to read records from Excel without Runtime reflection 
which makes the code amenable to better auditing and customization.

In order to use the functionality you will _need_ to add 
the `zerocell-processor` dependency to your POM. This adds a compile-time 
annotation processor which generates the classes:

```xml
<dependency>
    <groupId>com.creditdatamw.labs</groupId>
    <artifactId>zerocell-processor</artifactId>
    <version>0.3.2</version>
    <scope>provided</scope>
</dependency>
```

Then, in your code use the `@ZerocellReaderBuilder` annotation on a class
that contains ZeroCell `@Column` annotations.

Using a class defined as in the example shown below:

```java
package com.example;

@ZerocellReaderBuilder
public class Person {
    @RowNumber
    private int rowNumber;
    
    @Column(index=0, name="FIRST_NAME")
    private String firstName;
    
    @Column(index=1, name="LAST_NAME")
    private String lastName;
    
    @Column(index=2, name="DATE_OF_BIRTH")
    private LocalDate dateOfBirth;
    
    public static void main(String... args) {
        File file = new File("people.xlsx");
        String sheet = "Sheet 1";
        ZeroCellReader<Person> reader = new com.example.PersonReader();
        List<Person> people = reader.read(file, sheet);
        people.forEach(person -> {
            // do something with person
        });
    }
}
```

Generates a class in the com.example package

```java
package com.example;

public class PersonReader implements ZeroCellReader {
  // generated code here
}
```

## Loading the Correct Sheet

If you do not specify the name of the sheet to load from, zerocell attempts to
load data from the first Sheet in the Workbook. If the Sheet doesn't have matching
columns as the defined model an exception will be thrown. In order to ensure
the correct sheet is loaded it is recommended to always specify the sheet name.

## Exception Handling

The API throws `ZeroCellException` if something goes wrong, e.g. sheet not found. 
It is an unchecked exception and may cause your code to stop executing if not 
handled. Typically `ZeroCellException` will wrap another exception, so it's worth 
peeking at the cause using `Exception#getCause`.

## CONTRIBUTING

See the [`CONTRIBUTING.md`](CONTRIBUTING.md) file for more information.

---

Copyright (c) Credit Data CRB Ltd
