/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package bainuo.myapplication.mockserver;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by panhongchao on 15/12/3.
 */
@Documented
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MockObjArray {
    int size();

    /**
     * 在每个基本类型后面加上区分每项的编号，1.2.3.4...size()
     *
     * @return
     */
    boolean diffSuffix() default false;
}
