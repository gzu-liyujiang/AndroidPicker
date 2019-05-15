package cn.qqtheme.framework.wheelview.entity;

import java.util.Calendar;

/**
 * 日期时间实体
 *
 * @author liyujiang
 * @date 2019/5/14 17:30
 */
public class DateTimeEntity {
    private int year = 2019;
    private int month = 5;
    private int day = 14;
    private int hour = 12;
    private int minute = 0;

    public static DateTimeEntity now() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return new DateTimeEntity(year, month, day, hour, minute);
    }

    public DateTimeEntity(int year, int month, int day, int hour, int minute) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    public DateTimeEntity(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public DateTimeEntity(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

}
