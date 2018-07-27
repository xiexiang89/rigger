package com.rigger.android.annotation;

import com.rigger.android.annotation.internal.IntentMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Created by Edgar on 2018/7/27.
 */
@Retention(CLASS)
@Target(ElementType.FIELD)
@IntentMethod(name = "getBooleanExtra",hasDefault = true)
public @interface BooleanParams {

    String key();

    boolean defaultValue() default false;
}
