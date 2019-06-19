package cn.qqtheme.framework.wheelview.entity;

import java.util.Calendar;

/**
 * 日期时间数据实体
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/5/14 17:30
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class DateTimeEntity {
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;

    public static DateTimeEntity now() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return new DateTimeEntity(year, month, day, hour, minute);
    }

    public static DateTimeEntity hundredYearsOnFuture() {
        DateTimeEntity entity = now();
        entity.setYear(entity.getYear() + 100);
        return entity;
    }

    public DateTimeEntity(int year, int month, int day, int hour, int minute, int second) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public DateTimeEntity(int year, int month, int day, int hour, int minute) {
        this(year, month, day, hour, minute, 0);
    }

    public DateTimeEntity(int month, int day, int hour, int minute) {
        this(Calendar.getInstance().get(Calendar.YEAR), month, day, hour, minute, 0);
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

}
