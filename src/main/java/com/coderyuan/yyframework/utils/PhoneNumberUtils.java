/**
 * Copyright (C) 2015 coderyuan.com. All Rights Reserved.
 *
 * YyFramework
 *
 * PhoneNumberUtils.java created on 2015年7月9日
 *
 * @author yuanguozheng
 * @since 2015年7月9日
 * @version v1.0.0
 */
package com.coderyuan.yyframework.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yuanguozheng
 * 
 */
public class PhoneNumberUtils {

    public static boolean isMobileNo(String mobiles) {
        // Pattern p = Pattern.compile("^((13[0-9])|(14[5,7,9])|(15[^4,\\D])|(17[0,5,7,8])|(18[0,5-9]))\\d{8}$");
        Pattern p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
}
