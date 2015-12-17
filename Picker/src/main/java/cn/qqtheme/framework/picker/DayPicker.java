package cn.qqtheme.framework.picker;

import android.app.Activity;

import cn.qqtheme.framework.helper.Common;

/**
 * 天数选择器
 *
 * @author 李玉江[QQ:1032694760]
 * @since 2015/12/15
 * Created By Android Studio
 */
public class DayPicker extends OptionPicker {

    public DayPicker(Activity activity, int year, int month) {
        super(activity, new String[]{});
        //需要根据年份及月份动态计算天数
        int maxDays = Common.calculateDaysInMonth(year, month);
        for (int i = 1; i <= maxDays; i++) {
            options.add(Common.fillZore(i));
        }
    }

}