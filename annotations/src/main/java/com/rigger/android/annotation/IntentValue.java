package com.rigger.android.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Edgar on 2018/7/30.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface IntentValue {

    String name();

    FieldType fieldType() default FieldType.Basic;

    int defInt() default 0;
    float defFloat() default 0.0f;
    double defDouble() default 0.0;
    short defShort() default 0;
    byte defByte() default 0x0;
    char defChar() default 0;
    long defLong() default 0;
    boolean defBol() default false;
    String defString() default "";
}