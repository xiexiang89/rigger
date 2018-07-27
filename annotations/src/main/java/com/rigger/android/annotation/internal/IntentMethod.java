package com.rigger.android.annotation.internal;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Edgar on 2018/7/27.
 */
@Retention(RUNTIME)
@Target(ANNOTATION_TYPE)
public @interface IntentMethod {

    String name();

    boolean hasDefault() default false;
}