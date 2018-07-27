package com.android.rigger.processor;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;

/**
 * Created by Edgar on 2018/7/27.
 */
public class FieldBinding {

    private String name;
    private TypeName typeName;
    private String key;
    private String methodName;
    private Object defaultValue;
    private boolean hasDefaultValue;

    public FieldBinding(String name,TypeName typeName,String key,Object defaultValue,
                        String methodName,boolean hasDefaultValue) {
        this.name = name;
        this.typeName = typeName;
        this.key = key;
        this.defaultValue = defaultValue;
        this.methodName = methodName;
        this.hasDefaultValue = hasDefaultValue;
    }

    public CodeBlock render() {
        CodeBlock.Builder codeBlock = CodeBlock.builder()
                .add("object.$L = ",name);
        if (!hasDefaultValue) {
            codeBlock.add("intent.$L($S);\n",methodName,key);
        }
        return codeBlock.build();
    }
}