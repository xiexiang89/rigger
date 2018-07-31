package com.android.rigger.processor;

import com.rigger.android.annotation.ValueType;
import com.squareup.javapoet.TypeName;

import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

/**
 * Created by Edgar on 2018/7/27.
 */
public class Utils {

    private Utils() {}

    static TypeName getTypeName(TypeMirror typeMirror, ValueType valueType) {
        final String typeStr = typeMirror.toString();
        if (Utils.isString(typeStr)) {
            return InjectSet.STRING_TYPE;
        } else if (valueType == ValueType.CharSequence) {
            return InjectSet.CHAR_SEQUENCE_TYPE;
        } else if (valueType == ValueType.Parcelable) {
            return InjectSet.PARCELABLE_TYPE;
        } else if (valueType == ValueType.Serializable) {
            return InjectSet.SERIALIZABLE_TYPE;
        } else {
            return TypeName.get(typeMirror).unbox();
        }
    }

    static boolean isBasicType(TypeMirror typeMirror) {
        final TypeKind kind = typeMirror.getKind();
        return kind == TypeKind.CHAR |
                kind == TypeKind.SHORT |
                kind == TypeKind.BOOLEAN |
                kind == TypeKind.INT |
                kind == TypeKind.BYTE |
                kind == TypeKind.FLOAT |
                kind == TypeKind.DOUBLE |
                kind == TypeKind.LONG;
    }

    static boolean isString(String type) {
        return "java.lang.String".equals(type);
    }
}