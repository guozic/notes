package com.guozi.annotation;

import java.lang.annotation.*;

/**
 * @author guozi
 * @date 2018-03-26 10:27
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyRequestMapping {
    /**
     * 表示访问该方法的url
     * @return
     */
    String value() default "";
}
