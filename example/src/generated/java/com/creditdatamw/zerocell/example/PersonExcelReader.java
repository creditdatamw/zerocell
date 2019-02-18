package com.creditdatamw.zerocell.example;

import static com.creditdatamw.zerocell.converter.Converters.*;

import com.creditdatamw.zerocell.ReaderUtil;
import com.creditdatamw.zerocell.ZeroCellException;
import com.creditdatamw.zerocell.ZeroCellReader;
import java.io.File;
import java.lang.Override;
import java.lang.String;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PersonExcelReader implements ZeroCellReader<Person> {
  private static Logger LOGGER = LoggerFactory.getLogger(com.creditdatamw.zerocell.example.Person.class);

  static final int COL_0 = 0;

  static final int COL_1 = 1;

  static final int COL_2 = 2;

  static final int COL_3 = 3;

  static final int COL_4 = 4;

  static final int COL_6 = 6;

  static final int COL_5 = 5;

  private boolean validateHeaders;

  private boolean isHeaderRow;

  private int currentRow;

  private int currentCol;

  private Person cur;

  private List<Person> data;

  public PersonExcelReader() {
    this.data = new ArrayList<>();
  }

  @Override
  public List<Person> read(final File file, final String sheet) {
    this.reset();
    ReaderUtil.process(file, sheet, this);
    List<Person> dataList;
    dataList = Collections.unmodifiableList(this.data);
    return dataList;
  }

  private void reset() {
    this.currentRow = -1;
    this.currentCol = -1;
    this.cur = null;
    this.data.clear();
  }

  @Override
  public void headerFooter(final String text, final boolean b, final String tagName) {
    // Skip, not processing headers or footers here
  }

  @Override
  public void startRow(final int i) {
    currentRow = i;
    isHeaderRow = false;
    // Skip header row
    if (currentRow == 0) {
      isHeaderRow=true;
      return;
    }
    cur = new Person();
    cur.setRow(currentRow);
  }

  @Override
  public void cell(String cellReference, final String formattedValue,
      final XSSFComment xssfComment) {
    if (java.util.Objects.isNull(cur)) return;
    // Gracefully handle missing CellRef here in a similar way as XSSFCell does
    if(cellReference == null) {
      cellReference = new CellAddress(currentRow, currentCol).formatAsString();
    }
    int column = new CellReference(cellReference).getCol();
    currentCol = column;
    if (COL_0 == column) {
      assertColumnName("ID", formattedValue);
      //Handle any exceptions thrown by the converter - this stops execution of the whole process;
      try {
        cur.setId(new com.creditdatamw.zerocell.example.IdPrefixingConverter().convert(formattedValue, "ID", currentRow));
      } catch(Exception e) {
        throw new ZeroCellException("com.creditdatamw.zerocell.example.IdPrefixingConverter threw an exception while trying to convert value " + formattedValue, e);
      }
      return;
    }
    if (COL_1 == column) {
      assertColumnName("FIRST_NAME", formattedValue);
      cur.setFirstName(noop.convert(formattedValue, "FIRST_NAME", currentRow));
      return;
    }
    if (COL_2 == column) {
      assertColumnName("MIDDLE_NAME", formattedValue);
      cur.setMiddleName(noop.convert(formattedValue, "MIDDLE_NAME", currentRow));
      return;
    }
    if (COL_3 == column) {
      assertColumnName("LAST_NAME", formattedValue);
      cur.setLastName(noop.convert(formattedValue, "LAST_NAME", currentRow));
      return;
    }
    if (COL_4 == column) {
      assertColumnName("DATE_OF_BIRTH", formattedValue);
      cur.setDateOfBirth(toLocalDate.convert(formattedValue, "DATE_OF_BIRTH", currentRow));
      return;
    }
    if (COL_6 == column) {
      assertColumnName("DATE_REGISTERED", formattedValue);
      cur.setDateOfRegistration(toLocalDate.convert(formattedValue, "DATE_REGISTERED", currentRow));
      return;
    }
    if (COL_5 == column) {
      assertColumnName("FAV_NUMBER", formattedValue);
      cur.setFavouriteNumber(toInteger.convert(formattedValue, "FAV_NUMBER", currentRow));
      return;
    }
  }

  @Override
  public void endRow(final int i) {
    if (! java.util.Objects.isNull(cur)) {
      this.data.add(cur);
      this.cur = null;
    }
  }

  void assertColumnName(final String columnName, final String value) {
    if (validateHeaders && isHeaderRow) {
      if (! columnName.equalsIgnoreCase(value)) {
        throw new ZeroCellException(String.format("Expected Column '%s' but found '%s'", columnName, value));
      }
    }
  }
}
