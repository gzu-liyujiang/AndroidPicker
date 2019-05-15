package cn.qqtheme.framework.wheelview.interfaces.impl;

import cn.qqtheme.framework.toolkit.CqrDateTime;
import cn.qqtheme.framework.wheelview.interfaces.DateFormatter;

/**
 * 简单的日期格式化
 *
 * @author liyujiang
 * @date 2019/5/15 18:11
 */
public class SimpleDateFormatter implements DateFormatter {

    @Override
    public String formatYear(int year) {
        return String.valueOf(year);
    }

    @Override
    public String formatMonth(int month) {
        return CqrDateTime.fillZero(month);
    }

    @Override
    public String formatDay(int day) {
        return CqrDateTime.fillZero(day);
    }

}
