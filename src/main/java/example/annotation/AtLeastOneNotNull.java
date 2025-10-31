package com.annotation;

import java.lang.annotation.*;

/**
 * 校验多个字段至少有一个不为空
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AtLeastOneNotNull {
    // 需要校验的字段名数组
    String[] fields();

    // 校验失败的提示信息
    String message() default "至少有一个字段不能为空";
}