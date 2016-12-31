package cn.qqtheme.framework.picker;

import android.app.Activity;

/**
 * 时间选择器
 *
 * @author 李玉江[QQ:1032694760]
 * @since 2015/12/14
 */
public class TimePicker extends DateTimePicker {

    public TimePicker(Activity activity) {
        this(activity, HOUR_24);
    }

    /**
     * @see #HOUR_24
     * @see #HOUR_12
     */
    public TimePicker(Activity activity, @TimeMode int mode) {
        super(activity, NONE, mode);
    }

    /**
     * @deprecated use {@link #setLabel(String, String)} instead
     */
    @Deprecated
    @Override
    public final void setLabel(String yearLabel, String monthLabel, String dayLabel, String hourLabel, String minuteLabel) {
        super.setLabel(yearLabel, monthLabel, dayLabel, hourLabel, minuteLabel);
    }

    /**
     * 设置时间显示的单位
     */
    public void setLabel(String hourLabel, String minuteLabel) {
        super.setLabel("", "", "", hourLabel, minuteLabel);
    }

    /**
     * @deprecated nonsupport
     */
    @Deprecated
    @Override
    public final void setDateRangeStart(int startYear, int startMonth, int startDay) {
        throw new UnsupportedOperationException("Date range nonsupport");
    }

    /**
     * @deprecated nonsupport
     */
    @Deprecated
    @Override
    public final void setDateRangeEnd(int endYear, int endMonth, int endDay) {
        throw new UnsupportedOperationException("Date range nonsupport");
    }

    /**
     * @deprecated nonsupport
     */
    @Deprecated
    @Override
    public final void setDateRangeStart(int startYearOrMonth, int startMonthOrDay) {
        throw new UnsupportedOperationException("Date range nonsupport");
    }

    /**
     * @deprecated nonsupport
     */
    @Deprecated
    @Override
    public final void setDateRangeEnd(int endYearOrMonth, int endMonthOrDay) {
        throw new UnsupportedOperationException("Data range nonsupport");
    }

    /**
     * @deprecated use {@link #setRangeStart(int, int)} instead
     */
    @Deprecated
    @Override
    public void setTimeRangeStart(int startHour, int startMinute) {
        super.setTimeRangeStart(startHour, startMinute);
    }

    /**
     * @deprecated use {@link #setRangeEnd(int, int)} instead
     */
    @Deprecated
    @Override
    public void setTimeRangeEnd(int endHour, int endMinute) {
        super.setTimeRangeEnd(endHour, endMinute);
    }

    /**
     * @deprecated use setRangeStart and setRangeEnd instead
     */
    @Deprecated
    public void setRange(int startHour, int endHour) {
        super.setTimeRangeStart(startHour, 0);
        super.setTimeRangeEnd(endHour, 59);
    }


    /**
     * 设置范围：开始的时分
     */
    public void setRangeStart(int startHour, int startMinute) {
        super.setTimeRangeStart(startHour, startMinute);
    }

    /**
     * 设置范围：结束的时分
     */
    public void setRangeEnd(int endHour, int endMinute) {
        super.setTimeRangeEnd(endHour, endMinute);
    }

    /**
     * @deprecated use {@link #setSelectedItem(int, int)} instead
     */
    @Deprecated
    @Override
    public final void setSelectedItem(int year, int month, int day, int hour, int minute) {
        super.setSelectedItem(year, month, day, hour, minute);
    }

    /**
     * @deprecated use {@link #setSelectedItem(int, int)} instead
     */
    @Deprecated
    @Override
    public final void setSelectedItem(int yearOrMonth, int monthOrDay, int hour, int minute) {
        super.setSelectedItem(yearOrMonth, monthOrDay, hour, minute);
    }

    /**
     * 设置默认选中的时间
     */
    public void setSelectedItem(int hour, int minute) {
        super.setSelectedItem(0, 0, hour, minute);
    }

    /**
     * @deprecated use {@link #setOnWheelListener(OnWheelListener)} instead
     */
    @Deprecated
    @Override
    public final void setOnWheelListener(DateTimePicker.OnWheelListener onWheelListener) {
        super.setOnWheelListener(onWheelListener);
    }

    /**
     * 设置滑动监听器
     */
    public void setOnWheelListener(final OnWheelListener listener) {
        if (null == listener) {
            return;
        }
        super.setOnWheelListener(new DateTimePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String year) {
            }

            @Override
            public void onMonthWheeled(int index, String month) {
            }

            @Override
            public void onDayWheeled(int index, String day) {
            }

            @Override
            public void onHourWheeled(int index, String hour) {
                listener.onHourWheeled(index, hour);
            }

            @Override
            public void onMinuteWheeled(int index, String minute) {
                listener.onMinuteWheeled(index, minute);
            }
        });
    }

    /**
     * @deprecated use {@link #setOnTimePickListener(OnTimePickListener)} instead
     */
    @Deprecated
    @Override
    public final void setOnDateTimePickListener(OnDateTimePickListener listener) {
        super.setOnDateTimePickListener(listener);
    }

    public void setOnTimePickListener(final OnTimePickListener listener) {
        if (null == listener) {
            return;
        }
        super.setOnDateTimePickListener(new DateTimePicker.OnTimePickListener() {
            @Override
            public void onDateTimePicked(String hour, String minute) {
                listener.onTimePicked(hour, minute);
            }
        });
    }

    public interface OnTimePickListener {

        void onTimePicked(String hour, String minute);

    }

    public interface OnWheelListener {

        void onHourWheeled(int index, String hour);

        void onMinuteWheeled(int index, String minute);

    }

}
