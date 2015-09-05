/**
 * Copyright (c) 2015 coderyuan.com. All Rights Reserved.
 *
 * YyFramework
 *
 * RequestPath.java created on 17:03
 *
 * @author 国正
 * @version 1.0.0
 * @since 2015/9/5 0005
 */
package com.coderyuan.yyframework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RequestPath
 *
 * @author 国正
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestPath {

    String value() default "/";
}
