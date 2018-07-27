package com.rigger.android.annotation;

import com.rigger.android.annotation.internal.IntentMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Edgar on 2018/7/20.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
@IntentMethod(name = "getStringExtra")
public @interface StringParams {

    public String key();
}