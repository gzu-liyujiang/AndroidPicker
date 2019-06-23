package cn.qqtheme.androidpicker.custom;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import cn.qqtheme.framework.wheelpicker.DateTimePicker;
import cn.qqtheme.framework.wheelview.annotation.DateMode;
import cn.qqtheme.framework.wheelview.annotation.TimeMode;

/**
 * 360系统自带日历风格的日期时间选择器
 */
public class DateTimeLike360OSPicker extends DateTimePicker {

    public DateTimeLike360OSPicker(FragmentActivity activity) {
        super(activity, DateMode.NONE, TimeMode.HOUR_24);
    }

    @Override
    public void onViewCreated(@NonNull View contentView) {
        super.onViewCreated(contentView);
    }

}
