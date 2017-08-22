ZeroCell
========

Existing Excel libraries do too much just to read data from a workbook.
This library is optimized for *reading* data from excel only.
Particularly, it is optimized for getting the data from Excel into
POJO (Plain Old Java Objects). 

## Goals 

* Get POJOs from Excel with lower overheads i.e. read excel files with as few resources as possible
* Provide mappings for POJOs to excel rows via annotations
* Generate excel readers via a compile-time annotation processor

## Non-Goals

* Read or process excel workbook styles and other visual effects
* Writing to excel files
* Loading data into complex object hierarchies.

## Why not handle writing?

The Apache POI project has a good API for dealing with excel files and
provides the `SXSSFWorkbook` for writing large files in an efficient manner.

## Usage

Add the following to your `pom.xml`

```xml
<dependency>
    <groupId>com.creditdatamw.zerocell</groupId>
    <artifactId>zerocell-core</artifactId>
    <version>0.2.0</version>
</dependency>
```

ZeroCell has a very simple API. You can create a class to represent a row in an Excel sheet.
For example:

```java
@lombok.Data
public class Person {
    @RowNumber
    private int rowNumber;
    
    @Column(index=0, name="FIRST_NAME")
    private String firstName;
    
    @Column(index=1, name="LAST_NAME")
    private String lastName;
    
    @Column(index=2, name="DATE_OF_BIRTH")
    private LocalDate dateOfBirth;
}
```

Then using the `Reader` class you can load a list from the excel file as follows:

```java
List<Person> people = Reader.of(Person.class)
                            .from(new File("people.xlsx"))
                            .sheet("Sheet 1")
                            .list();
```

You can also inspect the column names of the class using the static `columnsOf` method:

```java
String[] columns = Reader.columnsOf(Person.class);
```

## Gotchas

### Exceptions

The API throws `ZeroCellException` if something goes wrong, e.g. sheet not found. 
It is an unchecked exception and may cause your code to stop executing if not 
handled. Typically `ZeroCellException` will wrap another exception, so it's worth 
peeking at the cause using `Exception#getCause`.

TODO
====

* Add options for reading in batches and emitting
* Add RowPostProcessor for determining what to ignore (like in xcelite)
* Add RowBean interface to zerocell
* Handle column name mismatches


## Authors

* Zikani Nyirenda Mwase <zikani@creditdatamw.com>

---

Copyright (c) 2017, Credit Data CRB Ltd
