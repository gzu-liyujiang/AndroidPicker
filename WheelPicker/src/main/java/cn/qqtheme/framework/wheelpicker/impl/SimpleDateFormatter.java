package cn.qqtheme.framework.wheelpicker.impl;

import cn.qqtheme.framework.wheelview.contract.DateFormatter;
import cn.qqtheme.framework.wheelview.util.DateTimeUtils;

/**
 * 简单的日期格式化
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/5/15 18:11
 */
public class SimpleDateFormatter implements DateFormatter {

    @Override
    public String formatYear(int year) {
        return String.valueOf(year);
    }

    @Override
    public String formatMonth(int month) {
        return DateTimeUtils.fillZero(month);
    }

    @Override
    public String formatDay(int day) {
        return DateTimeUtils.fillZero(day);
    }

}
