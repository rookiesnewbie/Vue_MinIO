package com.example.minio.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义自定义注解
 */
@Target({ElementType.METHOD}) //作用与方法上
@Retention(RetentionPolicy.RUNTIME)
public @interface Checking {

    boolean checkParams() default true;

}
