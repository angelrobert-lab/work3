package com.annotation;

import java.lang.annotation.*;

/**
 * 登录用户信息
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginUser {

}
