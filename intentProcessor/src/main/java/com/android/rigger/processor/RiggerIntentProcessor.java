package com.android.rigger.processor;

import com.google.auto.common.MoreElements;
import com.rigger.android.annotation.IntParams;
import com.rigger.android.annotation.StringParams;
import com.rigger.android.annotation.internal.IntentMethod;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

public class RiggerIntentProcessor extends AbstractProcessor{

    private Class[] annotationArray = {
            IntParams.class,
            StringParams.class};
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
        messager.printMessage(Diagnostic.Kind.NOTE,"start process");
        Map<TypeElement,InjectSet> injectSetMap = collectAnnotation(roundEnvironment);
        for (InjectSet injectSet : injectSetMap.values()) {
            messager.printMessage(Diagnostic.Kind.NOTE,
                    String.format("packageName:%s,className:%s",injectSet.getPackageName(),injectSet.getClassName()));
            JavaFile javaFile = injectSet.generateJavaFile();
            try {
                javaFile.writeTo(mFiler);
            } catch (IOException e) {
                e.printStackTrace();
                messager.printMessage(Diagnostic.Kind.ERROR,"write failed:"+e.getMessage());
            }
        }
//        for (Element element : roundEnvironment.getElementsAnnotatedWith(IntParams.class)) {
//            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
//            String packageName = MoreElements.getPackage(enclosingElement).getQualifiedName().toString();
//            String className = enclosingElement.getQualifiedName().toString().substring(
//                    packageName.length() + 1).replace('.', '$');
//
//            MethodSpec constructorMethod = MethodSpec.constructorBuilder()
//                    .addParameter(InjectSet.ACTIVITY_TYPE,"activity")
//                    .addCode("activity.")
//                    .build();
//            TypeSpec typeSpec = TypeSpec.classBuilder(className+"_inject")
//                    .addModifiers(Modifier.FINAL, Modifier.PUBLIC)
//                    .addMethod(constructorMethod).build();
//            JavaFile javaFile = JavaFile.builder(packageName, typeSpec).build();
//            try {
//                javaFile.writeTo(mFiler);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        return true;
    }

    private Map<TypeElement,InjectSet> collectAnnotation(RoundEnvironment roundEnvironment) {
        Map<TypeElement,InjectSet> injectMap = new HashMap<>();
        //collect string params
        for (Element element : roundEnvironment.getElementsAnnotatedWith(StringParams.class)) {
            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
            InjectSet injectSet = injectMap.get(enclosingElement);
            if (injectSet == null) {
                injectSet = InjectSet.newInject(enclosingElement);
                injectMap.put(enclosingElement,injectSet);
            }
            TypeMirror elementType = element.asType();
            IntentMethod intentMethod = findIntentMethod(StringParams.class);
            StringParams stringParams = element.getAnnotation(StringParams.class);
            messager.printMessage(Diagnostic.Kind.NOTE,intentMethod.name());
            injectSet.addField(new FieldBinding(element.getSimpleName().toString(),
                    TypeName.get(elementType),
                    stringParams.key(),
                    "",
                    intentMethod.name(),
                    intentMethod.hasDefault()));
        }
        return injectMap;
    }

    private IntentMethod findIntentMethod(Class<?> annotationClass) {
        return annotationClass.getAnnotation(IntentMethod.class);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new HashSet<>();
        annotations.add(IntParams.class.getName());
        annotations.add(StringParams.class.getName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
