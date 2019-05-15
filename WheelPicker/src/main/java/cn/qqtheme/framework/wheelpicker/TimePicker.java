package cn.qqtheme.framework.wheelpicker;

import android.support.v4.app.FragmentActivity;

import cn.qqtheme.framework.wheelview.annotation.DateMode;
import cn.qqtheme.framework.wheelview.annotation.TimeMode;

/**
 * 时间选择器
 *
 * @author liyujiang
 * @date 2019/5/15 17:44
 */
public class TimePicker extends DateTimePicker {

    public TimePicker(FragmentActivity activity, @TimeMode int timeMode) {
        super(activity, DateMode.NONE, timeMode);
    }

}
