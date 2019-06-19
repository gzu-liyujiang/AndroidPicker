package cn.qqtheme.framework.wheelview.util;

import android.support.annotation.NonNull;

/**
 * 日期时间处理
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/5/14 17:26
 */
@SuppressWarnings("unused")
public class DateTimeUtils {

    /**
     * 根据年份及月份获取每月的天数，类似于{@code Calendar.getActualMaximum()}
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
            String prefix = "0";
            if (text.startsWith(prefix)) {
                text = text.substring(1);
            }
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

}
