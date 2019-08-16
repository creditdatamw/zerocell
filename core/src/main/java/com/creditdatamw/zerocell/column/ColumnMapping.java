package com.creditdatamw.zerocell.column;

import com.creditdatamw.zerocell.ZeroCellException;
import com.creditdatamw.zerocell.annotation.Column;
import com.creditdatamw.zerocell.annotation.RowNumber;
import com.creditdatamw.zerocell.converter.NoopConverter;

import java.lang.reflect.Field;
import java.util.*;

public final class ColumnMapping {
    private final ColumnInfo rowNumberInfo;
    private final List<ColumnInfo> columns;

    public ColumnMapping(ColumnInfo rowNumberInfo, List<ColumnInfo> columns) {
        this.rowNumberInfo = rowNumberInfo;
        this.columns = columns;
    }

    public ColumnMapping(ColumnInfo rowNumberInfo,ColumnInfo... columns) {
        this.rowNumberInfo = rowNumberInfo;
        this.columns = Arrays.asList(columns);
    }

    public ColumnInfo getRowNumberInfo() {
        return rowNumberInfo;
    }

    public List<ColumnInfo> getColumns() {
        return columns;
    }


    /**
     * Creates a ColumnMapping from Zerocell {@link Column}
     * and {@link RowNumber} annotations applied on a Class
     *
     * @param clazz
     * @return
     */
    public static ColumnMapping parseColumnMappingFromAnnotations(Class<?> clazz) {
        Field[] fieldArray = clazz.getDeclaredFields();
        ArrayList<ColumnInfo> list = new ArrayList<>(fieldArray.length);
        ColumnInfo rowNumberColumn = null;
        for (Field field: fieldArray) {

            RowNumber rowNumberAnnotation = field.getAnnotation(RowNumber.class);

            if (! Objects.isNull(rowNumberAnnotation)) {
                rowNumberColumn = new ColumnInfo("__id__", field.getName(), -1, null,Integer.class, NoopConverter.class);
                continue;
            }

            Column annotation = field.getAnnotation(Column.class);
            if (! Objects.isNull(annotation)) {
                Class<?> converter = annotation.converterClass();
                list.add(new ColumnInfo(annotation.name().trim(),
                        field.getName(),
                        annotation.index(),
                        annotation.dataFormat(),
                        field.getType(),
                        converter));
            }
        }

        if (list.isEmpty()) {
            throw new ZeroCellException(String.format("Class %s does not have @Column annotations", clazz.getName()));
        }
        list.trimToSize();
        return new ColumnMapping(rowNumberColumn, list);
    }

    /**
     * Create th map, where key is the index from {@link Column} annotation,
     * and value is the ColumnInfo.
     * The method also checks the indexes duplicates and throws a
     * ZeroCellException in this case
     *
     * @throws ZeroCellException in the case of a duplicate index
     */
    public Map<Integer, ColumnInfo> getColumnsMap() {
        Map<Integer, ColumnInfo> map = new HashMap<>();
        this.columns.forEach(info -> {
            int index = info.getIndex();
            if (Objects.isNull(map.get(index))) {
                map.put(index, info);
            } else {
                throw new ZeroCellException("Cannot map two columns to the same index: " + index);
            }
        });
        return map;
    }
}
