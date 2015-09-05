/**
 * Copyright (C) 2015 coderyuan.com. All Rights Reserved.
 * <p>
 * WlanApp
 * <p>
 * ParamsUtils.java created on 2015年6月24日
 *
 * @author yuanguozheng
 * @version v1.0.0
 * @since 2015年6月24日
 */
package com.coderyuan.yyframework.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author yuanguozheng
 */
public class ParamsUtils {

    public static boolean isAllNotBlank(String... param) {
        for (String string : param) {
            if (StringUtils.isBlank(string)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAllBlank(String... param) {
        for (String string : param) {
            if (StringUtils.isNotBlank(string)) {
                return false;
            }
        }
        return true;
    }
}
