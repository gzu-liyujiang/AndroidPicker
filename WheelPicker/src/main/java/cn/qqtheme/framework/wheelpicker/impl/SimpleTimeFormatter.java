package cn.qqtheme.framework.wheelpicker.impl;

import cn.qqtheme.framework.wheelview.contract.TimeFormatter;
import cn.qqtheme.framework.wheelview.util.DateTimeUtils;

/**
 * 简单的时间格式化
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/5/15 18:13
 */
public class SimpleTimeFormatter implements TimeFormatter {

    @Override
    public String formatHour(int hour) {
        return DateTimeUtils.fillZero(hour);
    }

    @Override
    public String formatMinute(int minute) {
        return DateTimeUtils.fillZero(minute);
    }

    @Override
    public String formatSecond(int second) {
        return DateTimeUtils.fillZero(second);
    }

}
