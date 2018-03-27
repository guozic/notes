package com.guozi.annotation;

import java.lang.annotation.*;

/**
 * @author guozi
 * @date 2018-03-26 10:27
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyController {
    /**
     * 表示给controller注册别名
     * @return
     */
    String value() default "";

}
