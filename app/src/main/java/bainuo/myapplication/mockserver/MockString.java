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
 * Created by panhongchao on 15/12/2.
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MockString {
    String value();
}
