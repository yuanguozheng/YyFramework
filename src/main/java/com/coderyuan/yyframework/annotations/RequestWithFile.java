/**
 * Copyright (c) 2015 coderyuan.com. All Rights Reserved.
 *
 * YyFramework
 *
 * RequestWithFile.java created on 下午2:15
 *
 * @author yuanguozheng
 * @version 1.0.0
 * @since 15/9/7
 */
package com.coderyuan.yyframework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RequestWithFile
 *
 * @author yuanguozheng
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestWithFile {
}
