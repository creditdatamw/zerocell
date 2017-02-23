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

TODO
====

* Handle primitive types, integer, char, short etc...
* Add options for reading in batches and emitting
* Add RowPostProcessor for determining what to ignore (like in xcelite)
* Separate into zerocell-core (for runtime annotation processor) and zerocell-processor (for compile-time annotation processor)
* Write the value for the @RowNumber annotation to the right field

## Authors

* Zikani Nyirenda Mwase <zikani@creditdatamw.com>

---

Copyright (c) 2017, Credit Data CRB Ltd
