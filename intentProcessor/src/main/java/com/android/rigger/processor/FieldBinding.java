package com.android.rigger.processor;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;

/**
 * Created by Edgar on 2018/7/27.
 */
public class FieldBinding {

    public enum Kind {

        array("Array"),
        arrayList("ArrayList");

        private String kindName;

        private Kind(String kindName) {
            this.kindName = kindName;
        }
    }

    private String name;
    private TypeName typeName;
    private Kind kind;
    private String key;
    private Object defaultValue;
    private boolean hasDefaultValue;

    public FieldBinding(String name,TypeName typeName,Kind kind,String key,Object defaultValue) {
        this.name = name;
        this.typeName = typeName;
        this.key = key;
        this.defaultValue = defaultValue;
        this.hasDefaultValue = true;
        this.kind = kind;
    }

    public FieldBinding(String name,TypeName typeName,Kind kind,String key) {
        this.name = name;
        this.typeName = typeName;
        this.key = key;
        this.kind = kind;
    }

    public CodeBlock render() {
        CodeBlock.Builder codeBlock = CodeBlock.builder()
                .add("object.$L = ",name);
        if (!hasDefaultValue) {
            codeBlock.add("$T.$L(intent,$S);\n",InjectSet.INTENT_UTILS,getIntentMethod(),key);
        } else {
            if (typeName == InjectSet.STRING_TYPE || String.class.isInstance(defaultValue)) {
                codeBlock.add("$T.$L(intent,$S,$S);\n",InjectSet.INTENT_UTILS,getIntentMethod(),key,defaultValue);
            } else if (typeName == TypeName.CHAR){
                codeBlock.add("$T.$L(intent,$S,\'$L\');\n",InjectSet.INTENT_UTILS,getIntentMethod(),key,defaultValue);
            } else {
                codeBlock.add("$T.$L(intent,$S,$L);\n",InjectSet.INTENT_UTILS,getIntentMethod(),key,defaultValue);
            }
        }
        return codeBlock.build();
    }

    private String getIntentMethod() {
        String methodName = null;
        if (TypeName.BOOLEAN.equals(typeName)) {
            methodName = "getBoolean";
        } else if (TypeName.CHAR.equals(typeName)) {
            methodName = "getChar";
        } else if (TypeName.BYTE.equals(typeName)) {
            methodName = "getByte";
        } else if (TypeName.SHORT.equals(typeName)) {
            methodName = "getShort";
        } else if (TypeName.INT.equals(typeName)) {
            methodName = "getInt";
        } else if (TypeName.DOUBLE.equals(typeName)) {
            methodName = "getDouble";
        } else if (TypeName.LONG.equals(typeName)) {
            methodName = "getLong";
        } else if (TypeName.FLOAT.equals(typeName)) {
            methodName = "getFloat";
        } else if (InjectSet.STRING_TYPE.equals(typeName)) {
            methodName = "getString";
        } else if (InjectSet.SERIALIZABLE_TYPE.equals(typeName)) {
            methodName = "getSerializable";
        } else if (InjectSet.PARCELABLE_TYPE.equals(typeName)) {
            methodName = "getParcelable";
        } else if (InjectSet.CHAR_SEQUENCE_TYPE.equals(typeName)) {
            methodName = "getCharSequence";
        }
        if (methodName == null) {
            throw new IllegalArgumentException(String.format("%s unrecognized field types",typeName));
        }
        if (kind != null) {
            methodName += kind.kindName;
        }
        return methodName;
    }
}