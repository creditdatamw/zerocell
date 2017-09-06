package com.creditdatamw.zerocell;

import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;

import java.io.File;
import java.util.List;

/**
 * Proxy for ZeroCellReader Implementations
 */
public interface ZeroCellReader<T> extends XSSFSheetXMLHandler.SheetContentsHandler {
    List<T> read(final File file, final String sheet);
}
