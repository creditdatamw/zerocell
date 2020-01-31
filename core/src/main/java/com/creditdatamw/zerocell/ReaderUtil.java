package com.creditdatamw.zerocell;

import com.creditdatamw.zerocell.handler.EntityHandler;
import org.apache.poi.EmptyFileException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.util.SAXHelper;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.*;
import java.util.Objects;

/**
 * Utility class for Reader implementations
 *
 */
public final class ReaderUtil {
    public static final String ERROR_NOT_OPENXML = "Cannot load file. The file must be an Excel 2007+ Workbook (.xlsx)";
    /**
     * Reads a list of POJOs from the given excel file.
     *
     * @param path Excel file to read from
     * @param sheetName The sheet to extract from in the workbook
     * @param reader The reader class to use to load the file from the sheet
     */
    public static void process(String path, String sheetName, ZeroCellReader reader) {
        if (path == null || path.trim().isEmpty()) {
            throw new IllegalArgumentException("'path' must be given");
        }

        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            throw new IllegalArgumentException("path must not be a directory");
        }
        try (OPCPackage opcPackage = OPCPackage.open(file.getAbsolutePath(), PackageAccess.READ)) {
            process(opcPackage, sheetName, reader);
        } catch(InvalidFormatException | EmptyFileException | NotOfficeXmlFileException ife) {
            throw new ZeroCellException(ERROR_NOT_OPENXML);
        } catch (IOException ioe) {
            throw new ZeroCellException("Failed to process file", ioe);
        }
    }

    /**
     * Reads a list of POJOs from the given excel file.
     *
     * @param file Excel file to read from
     * @param sheetName The sheet to extract from in the workbook
     * @param reader The reader class to use to load the file from the sheet
     */
    public static void process(File file, String sheetName, ZeroCellReader reader) {
        try (OPCPackage opcPackage = OPCPackage.open(file, PackageAccess.READ)) {
            process(opcPackage, sheetName, reader);
        } catch(InvalidFormatException | EmptyFileException | NotOfficeXmlFileException ife) {
            throw new ZeroCellException(ERROR_NOT_OPENXML);
        } catch (IOException ioe) {
            throw new ZeroCellException("Failed to process file", ioe);
        }
    }

    /**
     * Reads a list of POJOs from the given input stream.
     *
     * @param is InputStream to read Excel file from
     * @param sheetName The sheet to extract from in the workbook
     * @param reader The reader class to use to load the file from the sheet
     */
    public static void process(InputStream is, String sheetName, ZeroCellReader reader) {
        try (PushbackInputStream p = new PushbackInputStream(is, 16);
             OPCPackage opcPackage = OPCPackage.open(p)) {
            process(opcPackage, sheetName, reader);
        } catch (Exception e) {
            throw new ZeroCellException("Failed to process file", e);
        }
    }

    /**
     * Processes data from an Excel file contained in the OPCPackage using the
     * reader implementation
     * <p>
     * Please note that the process will read data from the first sheet in the
     * File when if sheet name is not specified
     * (i.e. the sheet name defaults to the {@link EntityHandler.DEFAULT_SHEET})
     * </p>
     * @param opcPackage the OpenXML OPC Package
     * @param sheetName The sheet name
     * @param reader the reader implementation that handles the entity mapping
     */
    private static void process(OPCPackage opcPackage, String sheetName, ZeroCellReader reader) {
        try {
            DataFormatter dataFormatter = new DataFormatter();
            ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opcPackage);
            XSSFReader xssfReader = new XSSFReader(opcPackage);
            StylesTable stylesTable = xssfReader.getStylesTable();
            InputStream sheetInputStream = null;
            XSSFReader.SheetIterator sheets = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
            while (sheets.hasNext()) {
                sheetInputStream = sheets.next();

                if (EntityHandler.DEFAULT_SHEET.equalsIgnoreCase(sheetName)) {
                    break;
                }
                if (sheets.getSheetName().equalsIgnoreCase(sheetName)) {
                    break;
                } else {
                    sheetInputStream = null;
                }
            }

            if (Objects.isNull(sheetInputStream)) {
                throw new SheetNotFoundException(sheetName);
            }

            XMLReader xmlReader = SAXHelper.newXMLReader();
            xmlReader.setContentHandler(new XSSFSheetXMLHandler(stylesTable, strings, reader, dataFormatter, false));
            xmlReader.parse(new InputSource(sheetInputStream));
            sheetInputStream.close();
            xmlReader = null;
            sheetInputStream = null;
            stylesTable = null;
            strings = null;
            xssfReader = null;
        } catch(InvalidFormatException | EmptyFileException | NotOfficeXmlFileException ife) {
            throw new ZeroCellException(ERROR_NOT_OPENXML);
        } catch(SheetNotFoundException ex) {
            throw new ZeroCellException(ex.getMessage());
        } catch (ZeroCellException ze) {
            throw ze; // Rethrow the Exception
        } catch (Exception e) {
            throw new ZeroCellException("Failed to process file", e);
        }
    }
}
