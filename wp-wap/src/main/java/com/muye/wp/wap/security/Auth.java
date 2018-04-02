package com.muye.wp.wap.security;

import com.muye.wp.common.cons.UserType;

import java.lang.annotation.*;

/**
 * Created by muye on 18/2/28.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Auth {

    UserType[] value() default {};

    //是否需要通过实名验证
    boolean cret() default false;
}
