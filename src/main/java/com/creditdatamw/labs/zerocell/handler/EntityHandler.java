package com.creditdatamw.labs.zerocell.handler;

import com.creditdatamw.labs.zerocell.ZeroCellException;
import com.creditdatamw.labs.zerocell.annotation.Column;
import com.creditdatamw.labs.zerocell.annotation.RowNumber;
import com.creditdatamw.labs.zerocell.column.ColumnInfo;
import com.creditdatamw.labs.zerocell.converter.Converter;
import com.creditdatamw.labs.zerocell.converter.NoopConverter;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.util.SAXHelper;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import javax.validation.ValidationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class EntityHandler<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(EntityHandler.class);

    private static final String DEFAULT_SHEET = "uploads";

    private final Class<T> type;
    private final EntityExcelSheetHandler<T> entitySheetHandler;
    private final String sheetName;

    public EntityHandler(Class<T> clazz) {
        Objects.requireNonNull(clazz);
        this.type = clazz;
        this.sheetName = DEFAULT_SHEET;
        this.entitySheetHandler = createSheetHandler(clazz);
    }

    @SuppressWarnings("unchecked")
    private EntityExcelSheetHandler<T> createSheetHandler(Class<T> clazz) {
        Field[] fieldArray = clazz.getDeclaredFields();
        final ColumnInfo[] columns = new ColumnInfo[fieldArray.length];
        ColumnInfo rowNumberColumn = null;
        int indexed = 0;
        for (Field field: fieldArray) {

            RowNumber rowNumberAnnotation = field.getAnnotation(RowNumber.class);

            if (! Objects.isNull(rowNumberAnnotation)) {
                rowNumberColumn = new ColumnInfo("__id__", field.getName(), -1, null,Integer.class, NoopConverter.class);
                continue;
            }

            Column annotation = field.getAnnotation(Column.class);
            if (! Objects.isNull(annotation)) {
                Class<?> converter = annotation.convertorClass();

                // if (converter.getSuperclass() != Converter.class) {
                //    throw new ZeroCellException(String.format("Converter must be subclass of the %s class", Converter.class.getName()));
                //}
                int index = annotation.index();
                if (! Objects.isNull(columns[index])) {
                    throw new ZeroCellException("Cannot map two columns to the same index: " + index);
                }

                columns[index] = new ColumnInfo(annotation.name(),
                                               field.getName(),
                                               annotation.index(),
                                               annotation.dataFormat(),
                                               field.getType(),
                                               converter);
                indexed++;
            }
        }

        if (indexed < 1) {
            throw new ZeroCellException(String.format("Class %s does not have @Column annotations", clazz.getName()));
        }
        return new EntityExcelSheetHandler(rowNumberColumn, columns);
    }

    /**
     * Returns the extracted entities as an immutable list.
     * @return an immutable list of the extracted entities
     */
    public List<T> readAsList() {
        List<T> list = Collections.unmodifiableList(this.entitySheetHandler.read());
        return list;
    }

    /**
     * Reads a list of POJOs from the given excel file.
     *
     * @param file Excel file to read from
     * @return list of the eextracted entities
     */
    public void parseExcel(File file) {
        try (FileInputStream fis = new FileInputStream(file);
             OPCPackage opcPackage = OPCPackage.open(fis)) {

            DataFormatter dataFormatter = new DataFormatter();
            ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opcPackage);
            XSSFReader xssfReader = new XSSFReader(opcPackage);
            StylesTable stylesTable = xssfReader.getStylesTable();
            InputStream sheetInputStream = null;
            XSSFReader.SheetIterator sheets = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
            while(sheets.hasNext()) {
                sheetInputStream = sheets.next();
                if (sheets.getSheetName().equalsIgnoreCase(sheetName)) {
                    break;
                } else {
                    sheetInputStream = null;
                }
            }

            if (Objects.isNull(sheetInputStream)) {
                throw new ZeroCellException(String.format("Could not find sheet %s", sheetName));
            }

            XMLReader xmlReader = SAXHelper.newXMLReader();
            xmlReader.setContentHandler(new XSSFSheetXMLHandler(stylesTable,strings, entitySheetHandler, dataFormatter, false));
            xmlReader.parse(new InputSource(sheetInputStream));
            sheetInputStream.close();
            xmlReader = null;
            sheetInputStream = null;
            stylesTable = null;
            strings = null;
            xssfReader = null;
        } catch (Exception e) {
            throw new ZeroCellException("Failed to process file", e);
        }
    }

    private final class EntityExcelSheetHandler<T> implements XSSFSheetXMLHandler.SheetContentsHandler {
        private final Logger LOGGER = LoggerFactory.getLogger(EntityExcelSheetHandler.class);

        private final ColumnInfo rowNumberColumn;
        private final ColumnInfo[] columns;
        private final List<T> entities;

        private boolean validateHeaders = true;
        private boolean isHeaderRow = false;
        private int currentRow = -1;
        private int currentCol = -1;
        private T cur;

        EntityExcelSheetHandler(ColumnInfo rowNumberColumn, ColumnInfo[] columns) {
            this.rowNumberColumn = rowNumberColumn;
            this.columns = columns;
            this.entities = new ArrayList<>();
        }

        List<T> read() {
            return Collections.unmodifiableList(this.entities);
        }

        void clear() {
            this.currentRow = -1;
            this.currentCol = -1;
            this.cur = null;
            this.entities.clear();
        }

        @Override
        @SuppressWarnings("unchecked")
        public void startRow(int i) {
            currentRow = i;
            // skip the header row
            if (currentRow == 0) {
                isHeaderRow = true;
                return;
            }
            isHeaderRow = false;
            try {
                cur = (T) type.newInstance();
                // Write to the field with the @RowNumber annotation here if it exists
                if (! Objects.isNull(rowNumberColumn)) {
                    writeColumnField(cur, String.valueOf(i), rowNumberColumn, i);
                }
            } catch (InstantiationException | IllegalAccessException e) {
                throw new ZeroCellException("Failed to create and instance of " + type.getName(), e);
            }
        }

        @Override
        public void endRow(int i) {
            if (! Objects.isNull(cur)) {
                this.entities.add(cur);
                cur = null;
            }
        }

        @Override
        public void cell(String cellReference, String formattedValue, XSSFComment xssfComment) {
            if (Objects.isNull(cur)) return;

            // gracefully handle missing CellRef here in a similar way as XSSFCell does
            if(cellReference == null) {
                cellReference = new CellAddress(currentRow, currentCol).formatAsString();
            }

            int column = new CellReference(cellReference).getCol();
            currentCol = column;

            if (column > columns.length) {
                throw new ZeroCellException("Invalid Column index found: " + column);
            }

            ColumnInfo currentColumnInfo = columns[column];

            writeColumnField(cur, formattedValue, currentColumnInfo, currentRow);
        }



        /**
         * Write the value read from the excel cell to a field
         *
         * @param object the object to write to
         * @param formattedValue the value read from the current excel column/row
         * @param currentColumnInfo Column metadata
         * @param rowNum the row number
         */
        private void writeColumnField(T object, String formattedValue, ColumnInfo currentColumnInfo, int rowNum) {
            assertColumnName(currentColumnInfo.getName(), formattedValue);
            String fieldName = currentColumnInfo.getFieldName();
            try {
                Converter converter = (Converter) currentColumnInfo.getConverterClass().newInstance();
                Object value = null;
                // Don't use a converter if there isn't a custom one
                if (converter instanceof NoopConverter) {
                    value = convertValueToType(currentColumnInfo.getType(), formattedValue, currentColumnInfo.getName(), rowNum);
                } else {
                    // Handle any exceptions thrown by the converter - this stops execution of the whole process
                    try {
                        value = converter.convert(formattedValue);
                    } catch(Exception e) {
                        new ZeroCellException(String.format("%s threw an exception while trying to convert value %s ", converter.getClass().getName(), formattedValue), e);
                    }
                }
                Field field = type.getDeclaredField(currentColumnInfo.getFieldName());
                boolean access = field.isAccessible();
                if (! access) {
                    field.setAccessible(true);
                }
                field.set(cur, value);
                field.setAccessible(field.isAccessible() && access);
            } catch (IllegalArgumentException e) {
                throw new ZeroCellException(String.format("Failed to write value %s to field %s at row %s", formattedValue, fieldName, rowNum));
            } catch (InstantiationException | NoSuchFieldException | IllegalAccessException e) {
                LOGGER.error("Failed to set field: {}", fieldName, e);
            }
        }

        public Object convertValueToType(Class<?> fieldType, String formattedValue, String columnName, int rowNum) {
            Object value = null;
            if (fieldType == String.class) {
                value = String.valueOf(formattedValue);
            } else if (fieldType == LocalDateTime.class || fieldType == LocalDate.class) {
                value = parseAsLocalDate(columnName, rowNum, formattedValue);
            } else if (fieldType == java.sql.Date.class) {
                value = Date.valueOf(parseAsLocalDate(columnName, rowNum, formattedValue));
            } else if (fieldType == Timestamp.class) {
                value = Timestamp.valueOf(formattedValue == null ? "1905-01-01" : formattedValue);;
            } else if (fieldType == Integer.class || fieldType == int.class) {
                try {
                    value = Integer.valueOf(formattedValue == null ? "0.0" : formattedValue);
                } catch(Exception e) {
                    value = new Integer(-1);
                    LOGGER.error("Failed to parse {} as integer. Using default of -1 at column={} row={} ", formattedValue, columnName, rowNum);
                }
            } else if (fieldType == Long.class || fieldType == long.class) {
                try {
                    value = Long.valueOf(formattedValue == null ? "0" : formattedValue);
                } catch(Exception e) {
                    value = new Integer(-1);
                    LOGGER.error("Failed to parse {} as long. Using default of -1 at column={} row={} ", formattedValue, columnName, rowNum);
                }
            } else if (fieldType == Double.class || fieldType == double.class) {
                try {
                    value = Double.valueOf(formattedValue == null ? "0.0" : formattedValue);
                } catch(Exception e) {
                    value = new Double(-1.0);
                    LOGGER.error("Failed to parse {} as double. Using default of -1 at column={} row={} ", formattedValue, columnName, rowNum);
                }
            } else if (fieldType == Float.class || fieldType == float.class) {
                try {
                    value = Float.valueOf(formattedValue == null ? "0.0" : formattedValue);
                } catch(Exception e) {
                    value = new Float(-1.0);
                    LOGGER.error("Failed to parse {} as float. Using default of -1 at column={} row={} ", formattedValue, columnName, rowNum);
                }
            } else if (fieldType == Boolean.class) {
                try {
                    value = Boolean.valueOf(formattedValue == null ? "FALSE" : "TRUE");
                } catch(Exception e) {
                    value = null;
                    LOGGER.error("Failed to parse {} as Boolean. Using default of null at column={} row={} ", formattedValue, columnName, rowNum);
                }
            }
            return value;
        }

        private void assertColumnName(String columnName, String value) {
            if (validateHeaders && isHeaderRow) {
                if (! columnName.equalsIgnoreCase(value)){
                    throw new ValidationException(String.format("Expected Column '%s' but found '%s'", columnName, value));
                }
            }
        }

        private LocalDate parseAsLocalDate(String columnName, int rowNum, String value) {
            LocalDate date = HandlerUtils.parseAsLocalDate(columnName, rowNum, value);
            if (Objects.isNull(date)) {
                LOGGER.error("Failed to parse {} value={} from row={}", columnName, value, rowNum);
            }
            return date;
        }

        @Override
        public void headerFooter(String text, boolean b, String tagName) {
            // Skip, no headers or footers in CSV
        }
    }
}
