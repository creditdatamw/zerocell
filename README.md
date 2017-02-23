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


## Why not handle writing?

The Apache POI project has a good API for dealing with excel files and
provides the `SXSSFWorkbook` for writing large files in an efficient manner.

## Usage

Add the following to your `pom.xml`

```xml
<dependency>
    <groupId>com.creditdatamw.labs</groupId>
    <artifactId>zerocell</artifactId>
    <version>0.1.3</version>
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
                            .list();
```

## Gotchas

### Excel sheet naming

* Currently, there is a requirement that the Excel sheet you want to load data from must be named `uploads`.
This will be fixed in a later version.

### Exceptions

The API throws two kinds of exceptions - they are unchecked and may cause your code to stop executing if not 
handled. These are `ZeroCellException` and `ValidationException`. Typically `ZeroCellException` will wrap
another exception, so it's worth peeking at the `Exception#getCause` for those kind of exceptions.

TODO
====

* Add options for reading in batches and emitting
* Add RowPostProcessor for determining what to ignore (like in xcelite)
* Separate into zerocell-core (for runtime annotation processor) and zerocell-processor (for compile-time annotation processor)
* Add RowBean interface to zerocell

## Authors

* Zikani Nyirenda Mwase <zikani@creditdatamw.com>

---

Copyright (c) 2017, Credit Data CRB Ltd
