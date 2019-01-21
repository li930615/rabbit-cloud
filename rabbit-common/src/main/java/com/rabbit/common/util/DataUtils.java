package com.rabbit.common.util;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Date;

/**
 * @ClassName DataUtils
 * @Description TODO
 * @Author LZQ
 * @Date 2019/1/19 19:42
 **/
public class DataUtils extends DateUtils {

    private static String[] parsePatterns = {"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM", "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    public static String getDate() {
        return getDate("yyyy-MM-dd");
    }

    public static String getDate(String pattern) {
        return DateFormatUtils.format(new Date(), pattern);
    }

    public static String formatDate(Date date, Object[] pattern) {
        String formatDate = null;
        if ((pattern != null) && (pattern.length > 0))
            formatDate = DateFormatUtils.format(date, pattern[0].toString());
        else {
            formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
        }
        return formatDate;
    }

    public static String formatDateTime(Date date) {
        return formatDate(date, new Object[]{"yyyy-MM-dd HH:mm:ss"});
    }

    public static String getTime() {
        return formatDate(new Date(), new Object[]{"HH:mm:ss"});
    }

    public static String getDateTime() {
        return formatDate(new Date(), new Object[]{"yyyy-MM-dd HH:mm:ss"});
    }

    public static String getYear() {
        return formatDate(new Date(), new Object[]{"yyyy"});
    }

    public static String getMonth() {
        return formatDate(new Date(), new Object[]{"MM"});
    }

    public static String getDay() {
        return formatDate(new Date(), new Object[]{"dd"});
    }

    public static String getWeek() {
        return formatDate(new Date(), new Object[]{"E"});
    }

    public static Date parseDate(Object str) {
        if (str == null)
            return null;
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
        }
        return null;
    }

    public static long pastDays(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / 86400000L;
    }

    public static long pastHour(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / 3600000L;
    }

    public static long pastMinutes(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / 60000L;
    }

    public static String formatDateTime(long timeMillis) {
        long day = timeMillis / 86400000L;
        long hour = timeMillis / 3600000L - day * 24L;
        long min = timeMillis / 60000L - day * 24L * 60L - hour * 60L;
        long s = timeMillis / 1000L - day * 24L * 60L * 60L - hour * 60L * 60L - min * 60L;
        long sss = timeMillis - day * 24L * 60L * 60L * 1000L - hour * 60L * 60L * 1000L - min * 60L * 1000L - s * 1000L;
        return new StringBuilder().append(day > 0L ? new StringBuilder().append(day).append(",").toString() : "").append(hour).append(":").append(min).append(":").append(s).append(".").append(sss).toString();
    }

    public static double getDistanceOfTwoDate(Date before, Date after) {
        long beforeTime = before.getTime();
        long afterTime = after.getTime();
        return (afterTime - beforeTime) / 86400000L;
    }

    public static Long formatDuring(Date begin, Date end) {
        return Long.valueOf(end.getTime() - begin.getTime());
    }

    public static String formatDuring(long mss) {
        long days = mss / 86400000L;
        long hours = mss % 86400000L / 3600000L;
        long minutes = mss % 3600000L / 60000L;

        return new StringBuilder().append(days).append("天").append(hours).append("小时").append(minutes).append("分钟").toString();
    }
}
