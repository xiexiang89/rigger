package com.android.rigger.processor;

import com.squareup.javapoet.TypeName;

import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

/**
 * Created by Edgar on 2018/7/27.
 */
public class Utils {

    private Utils() {}

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

    private static boolean isAssignableFrom(String type, String className) {
        try {
            return Class.forName(type).isAssignableFrom(Class.forName(className));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    static boolean isString(String type) {
        return "java.lang.String".equals(type);
    }
}