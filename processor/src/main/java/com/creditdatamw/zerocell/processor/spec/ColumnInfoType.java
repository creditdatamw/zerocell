package com.creditdatamw.zerocell.processor.spec;

import com.creditdatamw.zerocell.ZeroCellException;
import com.creditdatamw.zerocell.annotation.Column;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Custom "type" class representing a column
 */
class ColumnInfoType {
    private String name;

    private String fieldName;

    private int index;

    private String dataFormat;

    private TypeMirror type;

    private TypeMirror converterClass;

    public ColumnInfoType(String name, String fieldName, int index, String dataFormat, TypeMirror type, TypeMirror converterClass) {
        this.name = name;
        this.fieldName = fieldName;
        this.index = index;
        this.dataFormat = dataFormat;
        this.type = type;
        this.converterClass = converterClass;
    }

    public String getName() {
        return name.replaceAll("_", " ").toUpperCase().trim();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getDataFormat() {
        return dataFormat;
    }

    public void setDataFormat(String dataFormat) {
        this.dataFormat = dataFormat;
    }

    public TypeMirror getType() {
        return type;
    }

    public void setType(TypeMirror type) {
        this.type = type;
    }

    public TypeMirror getConverterClass() {
        return converterClass;
    }

    public void setConverterClass(TypeMirror converterClass) {
        this.converterClass = converterClass;
    }

    /**
     * Extracts the @{@link Column} annotations from the typeElement
     *
     * @param typeElement Element of {@link javax.lang.model.element.ElementKind#CLASS}
     * @return
     */
    public static final List<ColumnInfoType> columnsOf(TypeElement typeElement) {
        List<ColumnInfoType> columns = new ArrayList<>();
        for(Element element: typeElement.getEnclosedElements()) {
            if (! element.getKind().isField()) {
                continue;
            }
            Column annotation = element.getAnnotation(Column.class);
            if (! Objects.isNull(annotation)) {
                TypeMirror typeClazz = null,
                        converterClazz = null;
                try {
                    typeClazz = element.asType();
                } catch (MirroredTypeException mte) {
                    typeClazz = mte.getTypeMirror();
                }
                try {
                    annotation.converterClass();
                } catch (MirroredTypeException mte) {
                    converterClazz = mte.getTypeMirror();
                }
                columns.add(new ColumnInfoType(annotation.name(),
                        element.getSimpleName().toString(),
                        annotation.index(),
                        annotation.dataFormat(),
                        typeClazz,
                        converterClazz));
            }
        }
        // Check for out of range and duplicate column indexes
        boolean[] indexUsed = new boolean[columns.size()];
        int index;
        for(ColumnInfoType c: columns) {
            index = c.getIndex();
            if (index > indexUsed.length - 1) {
                throw new ZeroCellException(
                    String.format(
                        "Column index out of range. index=%s columnCount=%s." +
                        "Ensure there @Column annotations for all indexes from 0 to %s",
                        index,
                        indexUsed.length,
                        indexUsed.length - 1));
            }
            if (indexUsed[index]) {
                throw new ZeroCellException("Cannot map two columns to the same index: " + index);
            }
            indexUsed[index] = true;
        }
        indexUsed = null;
        return columns;
    }
}
