package com.creditdatamw.zerocell;

import org.apache.poi.EmptyFileException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.util.SAXHelper;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.Objects;

/**
 * Utility class for Reader implementations
 *
 */
public final class ReaderUtil {
    /**
     * Reads a list of POJOs from the given excel file.
     *
     * @param file Excel file to read from
     * @param sheetName The sheet to extract from in the workbook
     * @param reader The reader class to use to load the file from the sheet
     */
    public static void process(File file, String sheetName, ZeroCellReader reader) {
        try (FileInputStream fis = new FileInputStream(file);
             PushbackInputStream p = new PushbackInputStream(fis, 16);
             OPCPackage opcPackage = OPCPackage.open(p)) {

            DataFormatter dataFormatter = new DataFormatter();
            ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opcPackage);
            XSSFReader xssfReader = new XSSFReader(opcPackage);
            StylesTable stylesTable = xssfReader.getStylesTable();
            InputStream sheetInputStream = null;
            XSSFReader.SheetIterator sheets = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
            while (sheets.hasNext()) {
                sheetInputStream = sheets.next();
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
            throw new ZeroCellException("Cannot load file. The file must be an Excel 2007+ Workbook (.xlsx)");
        } catch(SheetNotFoundException ex) {
            throw new ZeroCellException(ex.getMessage());
        } catch (ZeroCellException ze) {
            throw ze; // Rethrow the Exception
        } catch (Exception e) {
            throw new ZeroCellException("Failed to process file", e);
        }
    }
}
