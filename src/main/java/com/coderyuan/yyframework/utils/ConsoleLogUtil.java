/**
 * Copyright (c) 2015 coderyuan.com. All Rights Reserved.
 *
 * YyFramework
 *
 * ConsoleLogUtil.java created on 16:53
 *
 * @author yuanguozheng
 * @version 1.0.0
 * @since 2015/9/15 0015
 */
package com.coderyuan.yyframework.utils;

import com.coderyuan.yyframework.settings.Constants;

/**
 * ConsoleLogUtil
 *
 * @author yuanguozheng
 */
public class ConsoleLogUtil {

    private static boolean sDebug = false;

    public static void setDebug(boolean debug) {
        sDebug = debug;
    }

    public static void log(String log) {
        if (!sDebug) {
            return;
        }
        System.out.println(String.format("[%s] %s", TimeUtils.getFormatTime(Constants.LOG_TIME_FORMAT), log));
    }
}
