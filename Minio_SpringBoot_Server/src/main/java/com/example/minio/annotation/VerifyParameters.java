package com.example.minio.annotation;

import com.example.minio.enums.VerifyRegexEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author LiTeng
 * @Date 2023/10/13 14:10
 * Version 1.0
 * @Description 自定义参数校验注解
 */

@Target({ElementType.PARAMETER,ElementType.FIELD}) //作用于参数上或字段上
@Retention(RetentionPolicy.RUNTIME)
public @interface VerifyParameters {

    /**
     * 验证规则
     */

    VerifyRegexEnum regex() default VerifyRegexEnum.NO;

    //最小长度
    int min() default -1;

    //最大长度
    int max() default -1;


    //是否必填
    boolean required() default false;
}
