package cn.qqtheme.framework.wheelpicker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import cn.qqtheme.framework.popup.AbstractConfirmPopup;
import cn.qqtheme.framework.wheelview.annotation.DateMode;
import cn.qqtheme.framework.wheelview.annotation.TimeMode;
import cn.qqtheme.framework.wheelview.entity.DateTimeEntity;
import cn.qqtheme.framework.wheelview.interfaces.DateFormatter;
import cn.qqtheme.framework.wheelview.interfaces.OnDateSelectedListener;
import cn.qqtheme.framework.wheelview.interfaces.OnTimeSelectedListener;
import cn.qqtheme.framework.wheelview.interfaces.TimeFormatter;
import cn.qqtheme.framework.wheelpicker.interfaces.impl.SimpleDateFormatter;
import cn.qqtheme.framework.wheelpicker.interfaces.impl.SimpleTimeFormatter;
import cn.qqtheme.framework.wheelview.widget.DateTimeWheelLayout;

/**
 * 日期时间滚轮选择，参见 https://github.com/gzu-liyujiang/AndroidPicker
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/5/13 20:07
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class DateTimePicker extends AbstractConfirmPopup<DateTimeWheelLayout> {

    private DateTimeWheelLayout wheelLayout;

    private int wheelStyleRes = R.style.WheelDateTime;

    private OnDateSelectedListener onDateSelectedListener;
    private OnTimeSelectedListener onTimeSelectedListener;

    private int dateMode;
    private int timeMode;

    private DateTimeEntity startValue;
    private DateTimeEntity endValue;
    private DateTimeEntity defaultValue;

    private DateFormatter dateFormatter = new SimpleDateFormatter();
    private TimeFormatter timeFormatter = new SimpleTimeFormatter();

    private CharSequence yearLabel;
    private CharSequence monthLabel;
    private CharSequence dayLabel;
    private CharSequence hourLabel;
    private CharSequence minuteLabel;
    private CharSequence secondLabel;

    public DateTimePicker(FragmentActivity activity) {
        this(activity, DateMode.YEAR_MONTH_DAY, TimeMode.HOUR_24_SECOND);
    }

    public DateTimePicker(FragmentActivity activity, @DateMode int dateMode, @TimeMode int timeMode) {
        super(activity);
        this.dateMode = dateMode;
        this.timeMode = timeMode;
    }

    public void setWheelStyle(@StyleRes int styleRes) {
        this.wheelStyleRes = styleRes;
    }

    /**
     * 设置日期时间范围
     */
    public <T extends DateTimeEntity> void setRange(T startValue, T endValue) {
        this.startValue = startValue;
        this.endValue = endValue;
    }

    public <T extends DateTimeEntity> void setDefaultValue(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setDateFormatter(DateFormatter dateFormatter) {
        this.dateFormatter = dateFormatter;
    }

    public void setTimeFormatter(TimeFormatter timeFormatter) {
        this.timeFormatter = timeFormatter;
    }

    public void setDateLabel(CharSequence year, CharSequence month, CharSequence day) {
        this.yearLabel = year;
        this.monthLabel = month;
        this.dayLabel = day;
    }

    public void setTimeLabel(CharSequence hour, CharSequence minute, CharSequence second) {
        this.hourLabel = hour;
        this.minuteLabel = minute;
        this.secondLabel = second;
    }

    /**
     * 设置日期选择回调
     */
    public void setOnDateSelectedListener(OnDateSelectedListener onDateSelectedListener) {
        this.onDateSelectedListener = onDateSelectedListener;
    }

    /**
     * 设置时间选择回调
     */
    public void setOnTimeSelectedListener(OnTimeSelectedListener onTimeSelectedListener) {
        this.onTimeSelectedListener = onTimeSelectedListener;
    }

    @NonNull
    @Override
    protected DateTimeWheelLayout createBodyView(Context context) {
        View view = View.inflate(context, R.layout.popup_wheel_date_time, null);
        wheelLayout = view.findViewById(R.id.date_time_wheel_layout);
        return wheelLayout;
    }

    @Override
    public void onViewCreated(@NonNull View contentView) {
        super.onViewCreated(contentView);
        wheelLayout.setStyle(wheelStyleRes);
        wheelLayout.setMode(dateMode, timeMode);
        if (startValue == null) {
            startValue = DateTimeEntity.now();
        }
        if (endValue == null) {
            endValue = DateTimeEntity.hundredYearsOnFuture();
        }
        wheelLayout.setRange(startValue, endValue, defaultValue);
        wheelLayout.setDateFormatter(dateFormatter);
        wheelLayout.setTimeFormatter(timeFormatter);
        wheelLayout.setDateLabel(yearLabel, monthLabel, dayLabel);
        wheelLayout.setTimeLabel(hourLabel, minuteLabel, secondLabel);
    }

    @Override
    protected void onConfirm() {
        super.onConfirm();
        if (onDateSelectedListener != null) {
            int year = wheelLayout.getSelectedYear();
            int month = wheelLayout.getSelectedMonth();
            int day = wheelLayout.getSelectedDay();
            onDateSelectedListener.onItemSelected(year, month, day);
        }
        if (onTimeSelectedListener != null) {
            int hour = wheelLayout.getSelectedHour();
            int minute = wheelLayout.getSelectedMinute();
            int second = wheelLayout.getSelectedSecond();
            onTimeSelectedListener.onItemSelected(hour, minute, second);
        }
    }

    public final DateTimeWheelLayout getWheelLayout() {
        return wheelLayout;
    }

}
