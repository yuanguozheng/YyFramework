/**
 * Copyright (C) 2015 coderyuan.com. All Rights Reserved.
 *
 * CoderyuanApiLib
 *
 * TimeUtils.java created on 2015年6月24日
 *
 * @author yuanguozheng
 * @version v1.0.0
 * @since 2015年6月24日
 */
package com.coderyuan.yyframework.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author yuanguozheng
 */
public class TimeUtils {

    private static final long SECOND_MILLISECONDS = 1000;
    private static final long MINUTE_MILLISECONDS = 60 * SECOND_MILLISECONDS;
    private static final long HOUR_MILLISECONDS = 60 * MINUTE_MILLISECONDS;
    private static final long DAY_MILLISECONDS = 24 * HOUR_MILLISECONDS;
    private static final long WEEK_MILLISECONDS = 7 * DAY_MILLISECONDS;

    private static final String DETAIL_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";

    private static SimpleDateFormat sFormat;

    public static String getDetailTime(Date date) {
        sFormat = new SimpleDateFormat(DETAIL_TIME_FORMAT, Locale.CHINA);
        return sFormat.format(date);
    }

    public static String getDetailTime() {
        return getDetailTime(new Date());
    }

    public static String getDate(Date date) {
        sFormat = new SimpleDateFormat(DATE_FORMAT, Locale.CHINA);
        return sFormat.format(date);
    }

    public static String getDate() {
        return getDate(new Date());
    }

    public static String getTime(Date date) {
        sFormat = new SimpleDateFormat(TIME_FORMAT, Locale.CHINA);
        return sFormat.format(date);
    }

    public static String getTime() {
        return getTime(new Date());
    }

    public static Date parseDate(String timeString) {
        sFormat = new SimpleDateFormat(DETAIL_TIME_FORMAT, Locale.CHINA);
        try {
            return sFormat.parse(timeString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static long getDurationMillisecond(Date date1, Date date2) {
        return date2.getTime() - date1.getTime();
    }

    public static int getDurationDay(Date date1, Date date2) {
        return (int) (getDurationMillisecond(date1, date2) / DAY_MILLISECONDS);
    }

    public static int getDurationHour(Date date1, Date date2) {
        return (int) (getDurationMillisecond(date1, date2) / HOUR_MILLISECONDS);
    }

    public static int getDurationMinute(Date date1, Date date2) {
        return (int) (getDurationMillisecond(date1, date2) / MINUTE_MILLISECONDS);
    }

    public static int getDurationSecond(Date date1, Date date2) {
        return (int) (getDurationMillisecond(date1, date2) / SECOND_MILLISECONDS);
    }
}
