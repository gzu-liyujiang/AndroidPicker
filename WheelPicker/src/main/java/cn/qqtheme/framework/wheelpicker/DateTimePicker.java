package cn.qqtheme.framework.wheelpicker;

import android.content.Context;
import android.support.annotation.NonNull;
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
import cn.qqtheme.framework.wheelview.interfaces.impl.SimpleDateFormatter;
import cn.qqtheme.framework.wheelview.interfaces.impl.SimpleTimeFormatter;
import cn.qqtheme.framework.wheelview.widget.DateTimeWheelView;

/**
 * 日期时间滚轮选择，参见 https://github.com/gzu-liyujiang/AndroidPicker
 *
 * @author liyujiang
 * @date 2019/5/13 20:07
 */
public class DateTimePicker extends AbstractConfirmPopup<DateTimeWheelView> {

    private DateTimeWheelView dateTimeWheelView;

    private OnDateSelectedListener onDateSelectedListener;
    private OnTimeSelectedListener onTimeSelectedListener;

    private int dateMode;
    private int timeMode;

    private DateTimeEntity rangeStart;
    private DateTimeEntity rangeEnd;
    private DateTimeEntity defaultValue;

    private DateFormatter dateFormatter = new SimpleDateFormatter();
    private TimeFormatter timeFormatter = new SimpleTimeFormatter();

    private CharSequence yearLabel;
    private CharSequence monthLabel;
    private CharSequence dayLabel;
    private CharSequence hourLabel;
    private CharSequence minuteLabel;

    public DateTimePicker(FragmentActivity activity, @DateMode int dateMode, @TimeMode int timeMode) {
        super(activity);
        this.dateMode = dateMode;
        this.timeMode = timeMode;
    }

    /**
     * 设置日期时间范围
     */
    public void setRange(DateTimeEntity rangeStart, DateTimeEntity rangeEnd) {
        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
    }

    public void setDefaultValue(DateTimeEntity defaultValue) {
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

    public void setTimeLabel(CharSequence hour, CharSequence minute) {
        this.hourLabel = hour;
        this.minuteLabel = minute;
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
    protected DateTimeWheelView createBodyView(Context context) {
        View view = View.inflate(context, R.layout.popup_wheel_date_time, null);
        dateTimeWheelView = view.findViewById(R.id.date_time_wheel_view);
        return dateTimeWheelView;
    }

    @Override
    public void onViewCreated(@NonNull View contentView) {
        super.onViewCreated(contentView);
        dateTimeWheelView.setMode(dateMode, timeMode);
        dateTimeWheelView.setRange(rangeStart, rangeEnd, defaultValue);
        dateTimeWheelView.setDateFormatter(dateFormatter);
        dateTimeWheelView.setTimeFormatter(timeFormatter);
        dateTimeWheelView.setDateLabel(yearLabel, monthLabel, dayLabel);
        dateTimeWheelView.setTimeLabel(hourLabel, minuteLabel);
    }

    @Override
    protected void onConfirm() {
        super.onConfirm();
        if (onDateSelectedListener != null) {
            int year = dateTimeWheelView.getSelectedYear();
            int month = dateTimeWheelView.getSelectedMonth();
            int day = dateTimeWheelView.getSelectedDay();
            onDateSelectedListener.onDateSelected(year, month, day);
        }
        if (onTimeSelectedListener != null) {
            int hour = dateTimeWheelView.getSelectedHour();
            int minute = dateTimeWheelView.getSelectedMinute();
            onTimeSelectedListener.onTimeSelected(hour, minute);
        }
    }

    public final DateTimeWheelView getDateTimeWheelView() {
        return dateTimeWheelView;
    }

}
