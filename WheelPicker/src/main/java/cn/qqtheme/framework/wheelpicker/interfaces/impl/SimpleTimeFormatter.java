package cn.qqtheme.framework.wheelpicker.interfaces.impl;

import cn.qqtheme.framework.toolkit.CqrDateTime;
import cn.qqtheme.framework.wheelview.interfaces.TimeFormatter;

/**
 * 简单的时间格式化
 *
 * @author liyujiang
 * @date 2019/5/15 18:13
 */
public class SimpleTimeFormatter implements TimeFormatter {

    @Override
    public String formatHour(int hour) {
        return CqrDateTime.fillZero(hour);
    }

    @Override
    public String formatMinute(int minute) {
        return CqrDateTime.fillZero(minute);
    }

    @Override
    public String formatSecond(int second) {
        return CqrDateTime.fillZero(second);
    }

}
