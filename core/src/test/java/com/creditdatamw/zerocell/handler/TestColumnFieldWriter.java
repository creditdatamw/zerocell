package com.creditdatamw.zerocell.handler;

import com.creditdatamw.zerocell.column.ColumnInfo;
import com.creditdatamw.zerocell.converter.FallbackStrategy;
import com.creditdatamw.zerocell.converter.NoopConverter;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;

public class TestColumnFieldWriter {

    @Test
    public void testWillNotSetFieldIfDO_NOT_SETStrategyIsUsed() throws NoSuchFieldException {
        EntityExcelSheetHandler.ColumnFieldWriter columnFieldWriter =
            new EntityExcelSheetHandler.ColumnFieldWriter();

        Item item = new Item();
        item.fallbackStrategyDoNotSet = 1234567;
        Field field = Item.class.getDeclaredField("fallbackStrategyDoNotSet");

        ColumnInfo columnInfo = new ColumnInfo(
            "COLUMN",
            "fallbackStrategyDoNotSet",
            0,
            Integer.class,
            FallbackStrategy.IGNORE
        );

        columnFieldWriter.writeColumnField(
            field,
            item,
            "NOT A NUMBER 999",
            columnInfo,
            1,
            new NoopConverter()
        );

        assertEquals(1234567, item.getFallbackStrategyDoNotSet());
    }

    private static class Item {
        private int fallbackStrategyDoNotSet;

        public int getFallbackStrategyDoNotSet() {
            return fallbackStrategyDoNotSet;
        }

        public void setFallbackStrategyDoNotSet(int fallbackStrategyDoNotSet) {
            this.fallbackStrategyDoNotSet = fallbackStrategyDoNotSet;
        }
    }
}
