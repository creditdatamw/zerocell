package com.creditdatamw.zerocell.processor.spec;

import com.creditdatamw.zerocell.annotation.Column;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import java.time.LocalDate;
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
        return columns;
    }
}
