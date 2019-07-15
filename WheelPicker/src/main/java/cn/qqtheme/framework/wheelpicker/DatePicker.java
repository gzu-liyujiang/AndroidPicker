package cn.qqtheme.framework.wheelpicker;

import android.support.v4.app.FragmentActivity;

import cn.qqtheme.framework.wheelview.annotation.DateMode;
import cn.qqtheme.framework.wheelview.annotation.TimeMode;

/**
 * 日期选择器
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/5/15 17:51
 */
@SuppressWarnings("unused")
public class DatePicker extends DateTimePicker {

    public DatePicker(FragmentActivity activity) {
        this(activity, DateMode.YEAR_MONTH_DAY);
    }

    public DatePicker(FragmentActivity activity, @DateMode int dateMode) {
        super(activity, dateMode, TimeMode.NONE);
    }

}
