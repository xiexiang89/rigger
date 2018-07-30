package com.android.rigger.processor;

import com.rigger.android.annotation.FieldType;
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
        TypeName typeName = null;
        if (Utils.isBasicType(fieldType)) {
            typeName = TypeName.get(fieldType);
        } else {
            if (Utils.isString(fieldType.toString())) {
                typeName = InjectSet.STRING_TYPE;
            } else {
                switch (intentValue.fieldType()) {
                    case Serializable:
                        typeName = InjectSet.SERIALIZABLE_TYPE;
                        break;
                    case Parcelable:
                        typeName = InjectSet.PARCELABLE_TYPE;
                        break;
                    case CharSequence:
                        typeName = InjectSet.CHAR_SEQUENCE_TYPE;
                        break;
                }
            }
        }
        if (typeName == null) {
            throw new IllegalArgumentException("unrecognized field types.");
        }
        boolean supportDefValue = isSupportDefaultValue(fieldType,intentValue);
        FieldBinding fieldBinding = supportDefValue ? new FieldBinding(elementName,typeName, kind, intentValue.name(),getDefaultValue(fieldType,intentValue)) :
                new FieldBinding(elementName, typeName, kind, intentValue.name());
        injectSet.addField(fieldBinding);
    }

    private Object getDefaultValue(TypeMirror typeMirror,IntentValue value) {
        final TypeKind kind = typeMirror.getKind();
        if (kind == TypeKind.BOOLEAN) {
            return value.defBol();
        } else if (kind == TypeKind.LONG) {
            return value.defLong();
        } else if (kind == TypeKind.INT) {
            return value.defInt();
        } else if (kind == TypeKind.DOUBLE) {
            return value.defDouble();
        } else if (kind == TypeKind.FLOAT) {
            return value.defFloat();
        } else if (kind == TypeKind.CHAR) {
            return value.defChar();
        } else if (kind == TypeKind.BYTE) {
            return value.defByte();
        } else if (kind == TypeKind.SHORT) {
            return value.defShort();
        } else if (Utils.isString(typeMirror.toString()) || value.fieldType() == FieldType.CharSequence) {
            return value.defString();
        }
        return null;
    }

    private boolean isSupportDefaultValue(TypeMirror typeMirror,IntentValue value) {
        return Utils.isBasicType(typeMirror) || Utils.isString(typeMirror.toString()) || value.fieldType() == FieldType.CharSequence;
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
