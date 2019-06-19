package cn.qqtheme.framework.toolkit;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 日期时间工具类
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2015/8/5
 */
@SuppressWarnings("WeakerAccess")
public class CqrDateTime extends android.text.format.DateUtils {
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String MMDDHHMM = "MM-dd HH:mm";
    public static final String YHH_MM = "HH:mm";

    /**
     * SimpleDateFormat不是线程安全的，以下是线程安全实例化操作
     */
    private static final ThreadLocal<SimpleDateFormat> THREAD_LOCAL = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("", Locale.PRC);
        }
    };

    protected CqrDateTime() {
        throw new UnsupportedOperationException("You can't instantiate me");
    }

    /**
     * 根据年份及月份获取每月的天数
     *
     * @see Calendar#getActualMaximum(int)
     */
    public static int getTotalDaysInMonth(int year, int month) {
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                // 大月月份为31天
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                // 小月月份为30天
                return 30;
            case 2:
                // 二月需要判断是否闰年
                if (year <= 0) {
                    return 29;
                }
                // 是否闰年：能被4整除但不能被100整除；能被400整除；
                boolean isLeap = (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
                if (isLeap) {
                    return 29;
                } else {
                    return 28;
                }
            default:
                return 30;
        }
    }

    /**
     * 月日时分秒，0-9前补0
     */
    @NonNull
    public static String fillZero(int number) {
        return number < 10 ? "0" + number : "" + number;
    }

    /**
     * 截取掉前缀0以便转换为整数
     *
     * @see #fillZero(int)
     */
    public static int trimZero(@NonNull String text) {
        try {
            if (text.startsWith("0")) {
                text = text.substring(1);
            }
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 功能：判断日期是否相同。
     * 参见：http://www.cnblogs.com/myzhijie/p/3330970.html
     *
     * @param dateStart 比较的日期
     * @param dateEnd   比较的日期
     * @return boolean 如果在返回true，否则返回false。
     */
    public static boolean isSameDay(Date dateStart, Date dateEnd) {
        if (dateStart == null || dateEnd == null) {
            throw new IllegalArgumentException("date is null");
        }
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(dateStart);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(dateEnd);
        return (startCalendar.get(Calendar.ERA) == endCalendar.get(Calendar.ERA) &&
                startCalendar.get(Calendar.YEAR) == endCalendar.get(Calendar.YEAR) &&
                startCalendar.get(Calendar.DAY_OF_YEAR) == endCalendar.get(Calendar.DAY_OF_YEAR));
    }

    public static boolean isSameDay(long start, long end) {
        return isSameDay(new Date(start), new Date(end));
    }

    /**
     * 判断是否闰年
     *
     * @param year 年份
     * @return {@code true}: 闰年<br>{@code false}: 平年
     */
    public static boolean isLeapYear(int year) {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
    }

    /**
     * 判断是否闰年
     *
     * @param date Date类型时间
     * @return {@code true}: 闰年<br>{@code false}: 平年
     */
    public static boolean isLeapYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        return isLeapYear(year);
    }

    /**
     * 将yyyy-MM-dd HH:mm:ss字符串转换成日期<br/>
     *
     * @param dateStr 时间字符串
     * @param pattern 当前时间字符串的格式。
     * @return Date 日期 ,转换异常时返回null。
     */
    public static Date parseDate(String dateStr, String pattern) {
        try {
            SimpleDateFormat dateFormat = obtainDateFormat(pattern);
            Date date = dateFormat.parse(dateStr);
            return new Date(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将yyyy-MM-dd HH:mm:ss字符串转换成日期<br/>
     *
     * @param dateStr yyyy-MM-dd HH:mm:ss字符串
     * @return Date 日期 ,转换异常时返回null。
     */
    public static Date parseDate(String dateStr) {
        return parseDate(dateStr, YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 将指定的日期转换为一定格式的字符串
     */
    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat dateFormat = obtainDateFormat(pattern);
        return dateFormat.format(date);
    }

    /**
     * 将当前日期转换为一定格式的字符串
     */
    public static String formatDate(String pattern) {
        return formatDate(Calendar.getInstance(Locale.PRC).getTime(), pattern);
    }

    /**
     * 得到13位时间戳（精确到毫秒）
     */
    public static long parseStamp(String stamp) {
        if (TextUtils.isEmpty(stamp)) {
            return 0L;
        }
        if (stamp.length() == 10) {
            //10位时间戳精确到秒，13位时间戳精确到毫秒
            stamp += "000";
        }
        try {
            return Long.parseLong(stamp);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String stampToDate(String stamp, String pattern) {
        return stampToDate(parseStamp(stamp), pattern);
    }

    public static String stampToDate(long stamp, String pattern) {
        if (String.valueOf(stamp).length() == 10) {
            //转为13位时间戳
            stamp *= 1000;
        }
        SimpleDateFormat dateFormat = obtainDateFormat(pattern);
        return dateFormat.format(new Date(stamp));
    }

    @NonNull
    private static SimpleDateFormat obtainDateFormat(String pattern) {
        SimpleDateFormat dateFormat = THREAD_LOCAL.get();
        if (dateFormat == null) {
            dateFormat = new SimpleDateFormat(pattern, Locale.CHINA);
        } else {
            dateFormat.applyPattern(pattern);
        }
        return dateFormat;
    }

    /**
     * 此方法输入所要转换的时间输入例如（"2017-07-01 16:30:00"）返回时间戳
     */
    public static long dateToStamp(String date, String pattern) {
        if (TextUtils.isEmpty(date)) {
            return 0;
        }
        try {
            SimpleDateFormat dateFormat = obtainDateFormat(pattern);
            return dateFormat.parse(date).getTime();
        } catch (ParseException e) {
            return 0;
        }
    }

    public static int stampDiffDay(long startTimestamp, long endTimestamp) {
        double oneDay = 1000 * 60 * 60 * 24;
        double diffDay = (endTimestamp - startTimestamp) / oneDay;
        if (diffDay > 0 && diffDay < 1) {
            diffDay = 1;
        }
        return (int) diffDay;
    }

    public static String stampToWeek(long stamp) {
        String str = "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(stamp);
        //范围为1-7， 1=周日、7=周六，其他类推
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        switch (week) {
            case Calendar.SUNDAY:
                str = "周日";
                break;
            case Calendar.MONDAY:
                str = "周一";
                break;
            case Calendar.TUESDAY:
                str = "周二";
                break;
            case Calendar.WEDNESDAY:
                str = "周三";
                break;
            case Calendar.THURSDAY:
                str = "周四";
                break;
            case Calendar.FRIDAY:
                str = "周五";
                break;
            case Calendar.SATURDAY:
                str = "周六";
                break;
            default:
                str = stampToDate(stamp, "EEEE").replace("星期", "周");
                break;
        }
        return str;
    }

    /**
     * 获取一个时间区间内的日期
     */
    public static List<Date> findDates(Date dBegin, Date dEnd) {
        List<Date> dates = new ArrayList<>();
        dates.add(dBegin);
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(dEnd);
        // 测试此日期是否在指定日期之后
        while (dEnd.after(calBegin.getTime())) {
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            dates.add(calBegin.getTime());
        }
        return dates;
    }

    /**
     * 获取一个时间区间内的日期
     */
    public static List<Date> findDates(long dBegin, long dEnd) {
        return findDates(new Date(dBegin), new Date(dEnd));
    }

    /**
     * 获取生肖
     *
     * @param date Date类型时间
     * @return 生肖
     */
    public static String getChineseZodiac(Date date) {
        final String[] chineseZodiac = {"猴", "鸡", "狗", "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return chineseZodiac[cal.get(Calendar.YEAR) % 12];
    }

    /**
     * 获取生肖
     *
     * @param year 年
     * @return 生肖
     */
    public static String getChineseZodiac(int year) {
        final String[] chineseZodiac = {"猴", "鸡", "狗", "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊"};
        return chineseZodiac[year % 12];
    }

    /**
     * 获取星座
     *
     * @param month 月
     * @param day   日
     * @return 星座
     */
    public static String getZodiac(int month, int day) {
        final String[] zodiac = {"水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座"};
        final int[] ZODIAC_FLAGS = {20, 19, 21, 21, 21, 22, 23, 23, 23, 24, 23, 22};
        return zodiac[day >= ZODIAC_FLAGS[month - 1]
                ? month - 1
                : (month + 10) % 12];
    }

    /**
     * 获取星座
     *
     * @param date Date类型时间
     * @return 星座
     */
    public static String getZodiac(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return getZodiac(month, day);
    }

    /**
     * 格式化友好的时间差显示方式
     *
     * @param millis 开始时间戳
     */
    public static String getTimeSpanByNow1(long millis) {
        long now = System.currentTimeMillis();
        long span = now - millis;
        if (span < 1000) {
            return "刚刚";
        } else if (span < MINUTE_IN_MILLIS) {
            return String.format("%s秒前", span / SECOND_IN_MILLIS);
        } else if (span < HOUR_IN_MILLIS) {
            return String.format("%s分钟前", span / MINUTE_IN_MILLIS);
        } else if (span < DAY_IN_MILLIS) {
            return String.format("%s小时前", span / HOUR_IN_MILLIS);
        } else if (span < WEEK_IN_MILLIS) {
            return String.format("%s天前", span / DAY_IN_MILLIS);
        } else if (span < DAY_IN_MILLIS * 30) {
            return String.format("%s周前", span / WEEK_IN_MILLIS);
        } else if (span < YEAR_IN_MILLIS) {
            return String.format("%s月前", span / DAY_IN_MILLIS * 30);
        } else {
            return String.format("%s年前", span / YEAR_IN_MILLIS);
        }
    }

    /**
     * 格式化友好的时间差显示方式
     *
     * @param millis 开始时间戳
     */
    public static String getTimeSpanByNow2(long millis) {
        long now = System.currentTimeMillis();
        long span = now - millis;
        long day = span / DAY_IN_MILLIS;
        if (day == 0) {
            long hour = span / HOUR_IN_MILLIS;
            if (hour <= 4) {
                return String.format("凌晨%tR", millis);
            } else if (hour > 4 && hour <= 6) {
                return String.format("早上%tR", millis);
            } else if (hour > 6 && hour <= 11) {
                return String.format("上午%tR", millis);
            } else if (hour > 11 && hour <= 13) {
                return String.format("中午%tR", millis);
            } else if (hour > 13 && hour <= 18) {
                return String.format("下午%tR", millis);
            } else if (hour > 18 && hour <= 19) {
                return String.format("傍晚%tR", millis);
            } else if (hour > 19 && hour <= 24) {
                return String.format("晚上%tR", millis);
            } else {
                return String.format("今天%tR", millis);
            }
        } else if (day == 1) {
            return String.format("昨天%tR", millis);
        } else if (day == 2) {
            return String.format("前天%tR", millis);
        } else {
            return String.format("%tF", millis);
        }
    }

}
