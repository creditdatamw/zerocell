package com.creditdatamw.zerocell;

import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;

import java.util.List;

/**
 * Proxy for ZeroCellReader Implementations
 */
public interface ZeroCellReader<T> extends XSSFSheetXMLHandler.SheetContentsHandler {
    List<T> read();
}
