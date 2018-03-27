package com.guozi.annotation;

import java.lang.annotation.*;

/**
 * @author guozi
 * @date 2018-03-26 10:29
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyRequestParam {
    /**
     * 表示参数的别名，必填
     * @return
     */
    String value();
}
