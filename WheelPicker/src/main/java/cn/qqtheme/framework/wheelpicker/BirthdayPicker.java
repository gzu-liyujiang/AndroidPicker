package cn.qqtheme.framework.wheelpicker;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import cn.qqtheme.framework.wheelview.annotation.DateMode;
import cn.qqtheme.framework.wheelview.annotation.TimeMode;
import cn.qqtheme.framework.wheelview.entity.DateEntity;
import cn.qqtheme.framework.wheelpicker.interfaces.impl.BirthdayDateFormatter;

import java.util.Calendar;

/**
 * 出生日期滚轮选择
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/5/14 14:31
 * @since 2.0
 */
public class BirthdayPicker extends DateTimePicker {
    private static final int MAX_AGE = 100;

    public BirthdayPicker(FragmentActivity activity) {
        super(activity, DateMode.YEAR_MONTH_DAY, TimeMode.NONE);
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        DateEntity startValue = new DateEntity(currentYear - MAX_AGE, 1, 1);
        DateEntity endValue = new DateEntity(currentYear, currentMonth, currentDay);
        setRange(startValue, endValue);
        setDateFormatter(new BirthdayDateFormatter());
    }

    @Override
    public void onViewCreated(@NonNull View contentView) {
        super.onViewCreated(contentView);
        getWheelLayout().setDisplayYear(true);
        getWheelLayout().setDisplayMonth(true);
        getWheelLayout().setDisplayDay(true);
        getWheelLayout().setDisplayHour(false);
        getWheelLayout().setDisplayMinute(false);
        getWheelLayout().setDisplaySecond(false);
        getCancelTextView().setVisibility(View.GONE);
        getConfirmTextView().setText("完成");
    }

}
