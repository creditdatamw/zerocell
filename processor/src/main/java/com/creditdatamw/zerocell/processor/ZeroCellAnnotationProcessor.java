package com.creditdatamw.zerocell.processor;

import com.creditdatamw.zerocell.annotation.Column;
import com.creditdatamw.zerocell.annotation.RowNumber;
import com.creditdatamw.zerocell.annotation.ZerocellReaderBuilder;
import com.creditdatamw.zerocell.processor.spec.ReaderTypeSpec;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.HashSet;
import java.util.Set;

/**
 * ZeroCell Annotation Processor - generates classes that read values from Excel
 * rows into POJOs using the Excel SAX Handler approach which is much more efficient
 * at than the User model classes such as Cell.
 *
 * @author Zikani Nyirenda Mwase
 */
 // Some parts adapted from http://hannesdorfmann.com/annotation-processing/annotationprocessing101
@AutoService(Processor.class)
public class ZeroCellAnnotationProcessor extends AbstractProcessor {
    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;

    @SuppressWarnings("unused")
    public ZeroCellAnnotationProcessor() {
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new HashSet<>();
        annotations.add(ZerocellReaderBuilder.class.getCanonicalName());
        annotations.add(Column.class.getCanonicalName());
        annotations.add(RowNumber.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        typeUtils = env.getTypeUtils();
        elementUtils = env.getElementUtils();
        filer = env.getFiler();
        messager = env.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        for(Element annotatedElement : env.getElementsAnnotatedWith(ZerocellReaderBuilder.class)) {
            if (annotatedElement.getKind() != ElementKind.CLASS) {
                env.errorRaised();
                return true;
            }
            TypeElement classElement = (TypeElement) annotatedElement;
            try {
                ReaderTypeSpec spec = new ReaderTypeSpec(classElement);
                JavaFile javaFile = spec.build();
                javaFile.writeTo(filer);
            } catch (Exception ioe) {
                ioe.printStackTrace();
                messager.printMessage(Diagnostic.Kind.ERROR,
                        String.format("Failed to generate the Reader class for %s, got exception: %s",
                            classElement.getQualifiedName().toString(),
                            ioe.getMessage()),
                        annotatedElement);
                env.errorRaised();
                return true;
            }
        }
        return false;
    }

}
