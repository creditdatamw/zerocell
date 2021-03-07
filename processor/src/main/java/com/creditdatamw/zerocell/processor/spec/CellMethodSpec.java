package com.creditdatamw.zerocell.processor.spec;

import com.creditdatamw.zerocell.converter.NoopConverter;
import com.creditdatamw.zerocell.processor.ZeroCellAnnotationProcessor;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.Modifier;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Generates a method that uses a specified convertor
 * for a field in a ZerocellReaderBuilder.
 */
public class CellMethodSpec {

    public static MethodSpec build(List<ColumnInfoType> columns) {
        final CodeBlock.Builder builder = CodeBlock.builder();
        LoggerFactory.getLogger(ZeroCellAnnotationProcessor.class)
                     .info("Found {} columns in source class", columns.size());
        columns.forEach(column -> {
            String staticFieldName = "COL_" + column.getIndex();
            String fieldName = column.getFieldName();

            String beanSetterProperty = beanSetterPropertyName(fieldName);

            builder.beginControlFlow("if ($L == column)", staticFieldName)
                .addStatement("assertColumnName($S, formattedValue)", column.getName());

            converterStatementFor(builder, column, beanSetterProperty);

            builder.addStatement("return").endControlFlow();
        });

        return  MethodSpec.methodBuilder("cell")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String.class, "cellReference")
                .addParameter(String.class, "formattedValue", Modifier.FINAL)
                .addParameter(XSSFComment.class, "xssfComment", Modifier.FINAL)
                .addStatement("if (java.util.Objects.isNull(cur)) return")
                .addComment("Gracefully handle missing CellRef here in a similar way as XSSFCell does")
                .beginControlFlow("if(cellReference == null)")
                .addStatement("cellReference = new $T(currentRow, currentCol).formatAsString()", org.apache.poi.ss.util.CellAddress.class)
                .endControlFlow()
                .addStatement("int column = new $T(cellReference).getCol()", org.apache.poi.ss.util.CellReference.class)
                .addStatement("currentCol = column")
                .addCode(builder.build())
                .build();
    }

    private static void converterStatementFor(CodeBlock.Builder builder,
                                              ColumnInfoType column,
                                              String beanSetterProperty) {
        String columnName = column.getName();
        String converterClass = String.format("%s", column.getConverterClass());
        String throwException = "throw new ZeroCellException(\"$L threw an exception while trying to convert value \" + formattedValue, e)";

        CodeBlock converterCodeBlock = CodeBlock.builder()
                .addStatement("//Handle any exceptions thrown by the converter - this stops execution of the whole process")
                .beginControlFlow("try")
                    .addStatement("cur.set$L(new $L().convert(formattedValue, $S, currentRow))",
                            beanSetterProperty,
                            converterClass,
                            columnName)
                .nextControlFlow("catch(Exception e)")
                    .addStatement(throwException, converterClass)
                .endControlFlow()
                .build();
        // Don't use a converter if there isn't a custom one
        if (converterClass.equals(NoopConverter.class.getTypeName())) {
            builder.addStatement(converterStatementFor(column), beanSetterProperty, columnName);
        } else {
            builder.add(converterCodeBlock);
        }
    }

    public static String converterStatementFor(ColumnInfoType column) {
        String fieldType = String.format("%s", column.getType());

        if (fieldType.equals(LocalDateTime.class.getTypeName())) {
            return "cur.set$L(toLocalDateTime.convert(formattedValue, $S, currentRow))";

        } else if (fieldType.equals(LocalDate.class.getTypeName())) {
            return "cur.set$L(toLocalDate.convert(formattedValue, $S, currentRow))";

        } else if (fieldType.equals(java.sql.Date.class.getTypeName())) {
            return "cur.set$L(toSqlDate.convert(formattedValue, $S, currentRow))";

        } else if (fieldType.equals(Timestamp.class.getTypeName())) {
            return "cur.set$L(toSqlTimestamp.convert(formattedValue, $S, currentRow))";

        } else if (fieldType.equals(Integer.class.getTypeName()) ||
                fieldType.equals(int.class.getTypeName())) {
            return "cur.set$L(toInteger.convert(formattedValue, $S, currentRow))";

        } else if (fieldType.equals(Long.class.getTypeName()) ||
                fieldType.equals(long.class.getTypeName())) {
            return "cur.set$L(toLong.convert(formattedValue, $S, currentRow))";

        } else if (fieldType.equals(Double.class.getTypeName()) ||
                fieldType.equals(double.class.getTypeName())) {
            return "cur.set$L(toDouble.convert(formattedValue, $S, currentRow))";

        } else if (fieldType.equals(Float.class.getTypeName()) ||
                fieldType.equals(float.class.getTypeName())) {
            return "cur.set$L(toFloat.convert(formattedValue, $S, currentRow))";

        } else if (fieldType.equals(Boolean.class.getTypeName())) {
            return "cur.set$L(toBoolean.convert(formattedValue, $S, currentRow))";
        }
        // Default to Converters.noop.convert
        return "cur.set$L(noop.convert(formattedValue, $S, currentRow))";
    }

    /**
     * Returns the field name for a "setter" for a POJO, i.e. the part after the "set" text.
     * <br/>
     * Example: <code>"set" + beanSetterPropertyName("name")  -> "setName"</code>
     *
     * @param fieldName
     * @return
     */
    static String beanSetterPropertyName(String fieldName) {
        // TODO(zikani): Find a better way ??
        // Here we're assuming people follow standards of naming POJO fields
        String beanSetterProperty = String.valueOf(fieldName.charAt(0))
                .toUpperCase()
                .concat(fieldName.substring(1));
        return beanSetterProperty;
    }
}
