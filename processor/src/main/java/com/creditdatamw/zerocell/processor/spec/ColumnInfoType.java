package com.creditdatamw.zerocell.processor.spec;

import com.creditdatamw.zerocell.ZeroCellException;
import com.creditdatamw.zerocell.annotation.Column;
import lombok.AllArgsConstructor;
import lombok.Data;

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
@Data
@AllArgsConstructor
class ColumnInfoType {
    private String name;

    private String fieldName;

    private int index;

    private String dataFormat;

    private TypeMirror type;

    private TypeMirror converterClass;

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
                    annotation.convertorClass();
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
