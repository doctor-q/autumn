package cc.doctor.framework.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by doctor on 2017/3/8.
 */
public class DateUtils {

    public static final long SECOND = 1000L;
    public static final long MINUTE = 60 * SECOND;
    public static final long HOUR = 60 * MINUTE;
    public static final long DAY = 24 * HOUR;

    public static final String yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss";
    public static final String yyyyMMdd = "yyyy-MM-dd";
    public static final String yyyyMM = "yyyy-MM";
    public static final String yMdHms = "yyyyMMddHHmmss";

    private static ThreadLocal<SimpleDateFormat> simpleDateFormatThreadLocal = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat();
        }
    };
    public static String format(Date date, String pattern) {
        SimpleDateFormat simpleDateFormat = simpleDateFormatThreadLocal.get();
        simpleDateFormat.applyPattern(pattern);
        return simpleDateFormat.format(date);
    }

    public static Date parse(String dateStr, String pattern) {
        SimpleDateFormat simpleDateFormat = simpleDateFormatThreadLocal.get();
        simpleDateFormat.applyPattern(pattern);
        try {
            return simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toYMDHMS(Date date) {
        return format(date,  yMdHms);
    }

    public static String toYMDHMS(long timestamp) {
        return toYMDHMS(new Date(timestamp));
    }

    public static String toYYMMddHHmmss(Date date) {
        return format(date, yyyyMMddHHmmss);
    }

    public static String toYYMMddHHmmss(long timestamp) {
        return toYYMMddHHmmss(new Date(timestamp));
    }

    public static String toYYMMdd(Date date) {
        return format(date, yyyyMMdd);
    }

    public static String toYYMM(Date date) {
        return format(date, yyyyMM);
    }

    public static String year(Date date) {
        return format(date, "yyyy");
    }

    public static String month(Date date) {
        return format(date, "MM");
    }

    public static String date(Date date) {
        return format(date, "dd");
    }

    public static String hour(Date date) {
        int hourInt = hourInt(date);
        return hourInt >= 10 ? String.valueOf(hourInt) : "0" + hourInt;
    }

    public static String minute(Date date) {
        int minuteInt = minuteInt(date);
        return minuteInt >= 10 ? String.valueOf(minuteInt) : "0" + minuteInt;
    }

    public static String second(Date date) {
        int secondInt = secondInt(date);
        return secondInt >= 10 ? String.valueOf(secondInt) : "0" + secondInt;
    }

    public static int hourInt(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return instance.get(Calendar.HOUR_OF_DAY);
    }

    public static int minuteInt(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return instance.get(Calendar.MINUTE);
    }

    public static int secondInt(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return instance.get(Calendar.SECOND);
    }

    public static Date hourTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        setHour(calendar);
        return calendar.getTime();
    }

    public static Date dayTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        setDay(calendar);
        return calendar.getTime();
    }

    public static Date monthTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        setMonth(calendar);
        return calendar.getTime();
    }

    public static void setMonth(Calendar calendar) {
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
    }

    public static void setDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
    }

    public static void setHour(Calendar calendar) {
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
    }

    public static Date nextHour(Date date) {
        return nextHours(date, 1);
    }

    public static Date nextHours(Date date, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, n);
        return calendar.getTime();
    }

    public static Date nextDay(Date date) {
        return nextDays(date, 1);
    }

    public static Date nextDays(Date date, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, n);
        return calendar.getTime();
    }

    /**
     * 当月的最后一天
     *
     * @param date 当前月
     */
    public static Date lastDayOfMonth(Date date) {
        Date monthTime = monthTime(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(monthTime);
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }

    public static long unixTimetamp(Date date) {
        return date.getTime() / 1000;
    }

    /**
     * 纳秒时间戳
     */
    public static long nanoTimestamp(Date date) {
        return date.getTime() * 1000000;
    }

}
