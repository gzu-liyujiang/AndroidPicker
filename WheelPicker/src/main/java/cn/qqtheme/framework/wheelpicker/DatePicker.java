package cn.qqtheme.framework.wheelpicker;

import android.support.v4.app.FragmentActivity;

import cn.qqtheme.framework.wheelview.annotation.DateMode;
import cn.qqtheme.framework.wheelview.annotation.TimeMode;

/**
 * 日期选择器
 *
 * @author liyujiang
 * @date 2019/5/15 17:51
 */
public class DatePicker extends DateTimePicker {

    public DatePicker(FragmentActivity activity, @DateMode int dateMode) {
        super(activity, dateMode, TimeMode.NONE);
    }

}
