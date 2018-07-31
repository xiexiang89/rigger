package com.android.rigger.processor;

import com.rigger.android.annotation.IntentValue;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeName;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

public class RiggerIntentProcessor extends AbstractProcessor {

    private Messager messager;
    private Filer mFiler;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        messager = processingEnvironment.getMessager();
        mFiler = processingEnvironment.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        messager.printMessage(Diagnostic.Kind.NOTE, "start process");
        Map<TypeElement, InjectSet> injectSetMap = collectAnnotation(roundEnvironment);
        for (InjectSet injectSet : injectSetMap.values()) {
            messager.printMessage(Diagnostic.Kind.NOTE,
                    String.format("packageName:%s,className:%s", injectSet.getPackageName(), injectSet.getClassName()));
            JavaFile javaFile = injectSet.generateJavaFile();
            try {
                javaFile.writeTo(mFiler);
            } catch (IOException e) {
                e.printStackTrace();
                messager.printMessage(Diagnostic.Kind.ERROR, "write failed:" + e.getMessage());
            }
        }
        return true;
    }

    private Map<TypeElement, InjectSet> collectAnnotation(RoundEnvironment roundEnvironment) {
        Map<TypeElement, InjectSet> injectMap = new HashMap<>();
        //collect string params
        for (Element element : roundEnvironment.getElementsAnnotatedWith(IntentValue.class)) {
            IntentValue annotation = element.getAnnotation(IntentValue.class);
            parserAnnotation(injectMap, element,annotation);
        }
        return injectMap;
    }

    private void parserAnnotation(Map<TypeElement, InjectSet> injectSetMap, Element element,IntentValue intentValue) {
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
        InjectSet injectSet = injectSetMap.get(enclosingElement);
        if (injectSet == null) {
            injectSet = InjectSet.newInject(enclosingElement);
            injectSetMap.put(enclosingElement, injectSet);
        }
        final String elementName = element.getSimpleName().toString();
        TypeMirror elementType = element.asType();
        FieldBinding.Kind kind = null;
        TypeMirror fieldType = elementType;
        final TypeKind typeKind = elementType.getKind();
        if (typeKind == TypeKind.ARRAY) {
            ArrayType arrayType = (ArrayType) elementType;
            kind = FieldBinding.Kind.array;
            fieldType = arrayType.getComponentType();
        } else if (typeKind == TypeKind.DECLARED) {
            DeclaredType declaredType = (DeclaredType) elementType;
            messager.printMessage(Diagnostic.Kind.NOTE,"declaredType:"+declaredType);
            List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
            if (typeArguments.size() == 1) {
                kind = FieldBinding.Kind.arrayList;
                fieldType = typeArguments.get(0);
            } else {
                fieldType = declaredType;
            }
        }
        TypeName typeName = Utils.getTypeName(fieldType,intentValue.valueType());
        if (typeName == null) {
            throw new IllegalArgumentException("unrecognized field types.");
        }
        TypeValue typeValue = TypeValue.Factory.createTypeValue(typeName);
        if (typeValue != null) {
            injectSet.addField(new FieldBinding(elementName,typeName, kind, intentValue.name(),typeValue.value(intentValue)));
        } else {
            injectSet.addField(new FieldBinding(elementName, typeName, kind, intentValue.name()));
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new HashSet<>();
        annotations.add(IntentValue.class.getName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
