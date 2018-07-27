package com.android.rigger.processor;

import com.google.auto.common.MoreElements;
import com.google.common.collect.ImmutableList;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * Created by Edgar on 2018/7/27.
 */
public class InjectSet {

    public static final ClassName ACTIVITY_TYPE = ClassName.get("android.app","Activity");
    public static final ClassName INTENT_TYPE = ClassName.get("android.content","Intent");

    private ClassName className;
    private ImmutableList.Builder<FieldBinding> fieldBindingBuilder = ImmutableList.builder();

    private InjectSet(ClassName className) {
        this.className = className;
    }

    public JavaFile generateJavaFile() {
        return JavaFile.builder(className.packageName(),createInjectClass()).build();
    }

    private TypeSpec createInjectClass() {
        MethodSpec.Builder methodSpecBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(className,"object")
                .addParameter(ACTIVITY_TYPE,"activity")
                .addCode("final $T intent = activity.getIntent();\n",INTENT_TYPE);
        ImmutableList<FieldBinding> fieldBindings = fieldBindingBuilder.build();
        for (FieldBinding fieldBinding : fieldBindings) {
            methodSpecBuilder.addCode(fieldBinding.render());
        }
        return TypeSpec.classBuilder(className.simpleName()+"_inject")
                .addModifiers(Modifier.PUBLIC)
                .addMethod(methodSpecBuilder.build())
                .build();
    }

    public void addField(FieldBinding fieldBinding) {
        fieldBindingBuilder.add(fieldBinding);
    }

    public final String getPackageName() {
        return className.packageName();
    }

    public final String getClassName() {
        return className.simpleName();
    }

    static InjectSet newInject(TypeElement enclosingElement) {
        String packageName = MoreElements.getPackage(enclosingElement).getQualifiedName().toString();
        String clazzName = enclosingElement.getQualifiedName().toString().substring(
                packageName.length() + 1).replace('.', '$');
        ClassName className = ClassName.get(packageName,clazzName);
        return new InjectSet(className);
    }
}