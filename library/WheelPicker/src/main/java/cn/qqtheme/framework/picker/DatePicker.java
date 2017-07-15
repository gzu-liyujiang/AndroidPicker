package cn.qqtheme.framework.picker;

import android.app.Activity;

/**
 * 日期选择器
 *
 * @author 李玉江[QQ:1032694760]
 * @since 2015/12/14
 */
public class DatePicker extends DateTimePicker {

    public DatePicker(Activity activity) {
        this(activity, YEAR_MONTH_DAY);
    }

    /**
     * @see #YEAR_MONTH_DAY
     * @see #YEAR_MONTH
     * @see #MONTH_DAY
     */
    public DatePicker(Activity activity, @DateMode int mode) {
        super(activity, mode, NONE);
    }

    /**
     * @deprecated use {@link #setLabel(String, String, String)} instead
     */
    @Deprecated
    @Override
    public final void setLabel(String yearLabel, String monthLabel, String dayLabel, String hourLabel, String minuteLabel) {
        super.setLabel(yearLabel, monthLabel, dayLabel, hourLabel, minuteLabel);
    }

    /**
     * 设置年月日的单位
     */
    public void setLabel(String yearLabel, String monthLabel, String dayLabel) {
        super.setLabel(yearLabel, monthLabel, dayLabel, "", "");
    }

    /**
     * @deprecated use {@link #setRangeStart(int, int, int)} instead
     */
    @Deprecated
    @Override
    public final void setDateRangeStart(int startYear, int startMonth, int startDay) {
        super.setDateRangeStart(startYear, startMonth, startDay);
    }

    /**
     * @deprecated use {@link #setRangeEnd(int, int, int)} instead
     */
    @Deprecated
    @Override
    public final void setDateRangeEnd(int endYear, int endMonth, int endDay) {
        super.setDateRangeEnd(endYear, endMonth, endDay);
    }

    /**
     * @deprecated use {@link #setRangeStart(int, int)} instead
     */
    @Deprecated
    @Override
    public final void setDateRangeStart(int startYearOrMonth, int startMonthOrDay) {
        super.setDateRangeStart(startYearOrMonth, startMonthOrDay);
    }

    /**
     * @deprecated use {@link #setRangeEnd(int, int)} instead
     */
    @Deprecated
    @Override
    public final void setDateRangeEnd(int endYearOrMonth, int endMonthOrDay) {
        super.setDateRangeEnd(endYearOrMonth, endMonthOrDay);
    }

    /**
     * @deprecated nonsupport
     */
    @Deprecated
    @Override
    public void setTimeRangeStart(int startHour, int startMinute) {
        throw new UnsupportedOperationException("Time range nonsupport");
    }

    /**
     * @deprecated nonsupport
     */
    @Deprecated
    @Override
    public void setTimeRangeEnd(int endHour, int endMinute) {
        throw new UnsupportedOperationException("Time range nonsupport");
    }

    /**
     * 设置年份范围
     *
     * @deprecated use setRangeStart and setRangeEnd instead
     */
    @Deprecated
    public void setRange(int startYear, int endYear) {
        super.setRange(startYear, endYear);
    }

    /**
     * 设置范围：开始的年月日
     */
    public void setRangeStart(int startYear, int startMonth, int startDay) {
        super.setDateRangeStart(startYear, startMonth, startDay);
    }

    /**
     * 设置范围：结束的年月日
     */
    public void setRangeEnd(int endYear, int endMonth, int endDay) {
        super.setDateRangeEnd(endYear, endMonth, endDay);
    }

    /**
     * 设置范围：开始的年月日
     */
    public void setRangeStart(int startYearOrMonth, int startMonthOrDay) {
        super.setDateRangeStart(startYearOrMonth, startMonthOrDay);
    }

    /**
     * 设置范围：结束的年月日
     */
    public void setRangeEnd(int endYearOrMonth, int endMonthOrDay) {
        super.setDateRangeEnd(endYearOrMonth, endMonthOrDay);
    }

    /**
     * @deprecated use {@link #setSelectedItem(int, int, int)} instead
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
     * 设置默认选中的年月日
     */
    public void setSelectedItem(int year, int month, int day) {
        super.setSelectedItem(year, month, day, 0, 0);
    }

    /**
     * 设置默认选中的年月或者月日
     */
    public void setSelectedItem(int yearOrMonth, int monthOrDay) {
        super.setSelectedItem(yearOrMonth, monthOrDay, 0, 0);
    }

    /**
     * @deprecated use {@link #setOnWheelListener(OnWheelListener)} instead
     */
    @Deprecated
    @Override
    public final void setOnWheelListener(DateTimePicker.OnWheelListener onWheelListener) {
        super.setOnWheelListener(onWheelListener);
    }

    public void setOnWheelListener(final OnWheelListener listener) {
        if (null == listener) {
            return;
        }
        super.setOnWheelListener(new DateTimePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String year) {
                listener.onYearWheeled(index, year);
            }

            @Override
            public void onMonthWheeled(int index, String month) {
                listener.onMonthWheeled(index, month);
            }

            @Override
            public void onDayWheeled(int index, String day) {
                listener.onDayWheeled(index, day);
            }

            @Override
            public void onHourWheeled(int index, String hour) {

            }

            @Override
            public void onMinuteWheeled(int index, String minute) {

            }
        });
    }

    /**
     * @deprecated use {@link #setOnDatePickListener(OnDatePickListener)} instead
     */
    @Deprecated
    @Override
    public final void setOnDateTimePickListener(OnDateTimePickListener listener) {
        super.setOnDateTimePickListener(listener);
    }

    public void setOnDatePickListener(final OnDatePickListener listener) {
        if (null == listener) {
            return;
        }
        if (listener instanceof OnYearMonthDayPickListener) {
            super.setOnDateTimePickListener(new OnYearMonthDayTimePickListener() {
                @Override
                public void onDateTimePicked(String year, String month, String day, String hour, String minute) {
                    ((OnYearMonthDayPickListener) listener).onDatePicked(year, month, day);
                }
            });
        } else if (listener instanceof OnYearMonthPickListener) {
            super.setOnDateTimePickListener(new OnYearMonthTimePickListener() {
                @Override
                public void onDateTimePicked(String year, String month, String hour, String minute) {
                    ((OnYearMonthPickListener) listener).onDatePicked(year, month);
                }
            });
        } else if (listener instanceof OnMonthDayPickListener) {
            super.setOnDateTimePickListener(new OnMonthDayTimePickListener() {
                @Override
                public void onDateTimePicked(String month, String day, String hour, String minute) {
                    ((OnMonthDayPickListener) listener).onDatePicked(month, day);
                }
            });
        }
    }

    protected interface OnDatePickListener {

    }

    public interface OnYearMonthDayPickListener extends OnDatePickListener {

        void onDatePicked(String year, String month, String day);

    }

    public interface OnYearMonthPickListener extends OnDatePickListener {

        void onDatePicked(String year, String month);

    }

    public interface OnMonthDayPickListener extends OnDatePickListener {

        void onDatePicked(String month, String day);

    }

    public interface OnWheelListener {

        void onYearWheeled(int index, String year);

        void onMonthWheeled(int index, String month);

        void onDayWheeled(int index, String day);

    }

}
