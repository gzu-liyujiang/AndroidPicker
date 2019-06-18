package cn.qqtheme.framework.wheelview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.qqtheme.framework.toolkit.CqrDateTime;
import cn.qqtheme.framework.toolkit.CqrDensity;
import cn.qqtheme.framework.wheelview.R;
import cn.qqtheme.framework.wheelview.annotation.DateMode;
import cn.qqtheme.framework.wheelview.annotation.TimeMode;
import cn.qqtheme.framework.wheelview.entity.DateTimeEntity;
import cn.qqtheme.framework.wheelview.interfaces.DateFormatter;
import cn.qqtheme.framework.wheelview.interfaces.NumberFormatter;
import cn.qqtheme.framework.wheelview.interfaces.TimeFormatter;
import cn.qqtheme.framework.wheelview.interfaces.impl.AbstractWheelSelectedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * // FIXME: 2019/6/18 快速来回切换个滚轮可能会出现某些项显示为空问题
 * 日期时间滚轮控件
 *
 * @author liyujiang
 * @date 2019/5/14 15:26
 */
@SuppressWarnings("unused")
public class DateTimeWheelLayout extends LinearLayout {
    private static final int DELAY_BEFORE_CHECK_PAST = 100;

    private YearWheelView wvYear;
    private MonthWheelView wvMonth;
    private DayWheelView wvDay;
    private HourWheelView wvHour;
    private MinuteWheelView wvMinute;
    private SecondWheelView wvSecond;

    private TextView tvYearLabel;
    private TextView tvMonthLabel;
    private TextView tvDayLabel;
    private TextView tvHourLabel;
    private TextView tvMinuteLabel;
    private TextView tvSecondLabel;

    private List<WheelView> wheelViews = new ArrayList<>();

    private DateTimeEntity startValue;
    private DateTimeEntity endValue;

    private int yearIndex;
    private int monthIndex;
    private int dayIndex;
    private int hourIndex;
    private int minuteIndex;
    private int secondIndex;

    private int timeMode;

    private boolean displayYears = true;
    private boolean displayMonths = true;
    private boolean displayDays = true;
    private boolean displayHours = true;
    private boolean displayMinutes = true;
    private boolean displaySeconds = true;

    public DateTimeWheelLayout(Context context) {
        this(context, null);
    }

    public DateTimeWheelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        inflate(context, R.layout.include_date_time, this);

        wvYear = findViewById(R.id.year_wheel_view);
        wvMonth = findViewById(R.id.month_wheel_view);
        wvDay = findViewById(R.id.day_wheel_view);
        wvHour = findViewById(R.id.hour_wheel_view);
        wvMinute = findViewById(R.id.minute_wheel_view);
        wvSecond = findViewById(R.id.second_wheel_view);

        tvYearLabel = findViewById(R.id.tv_year_label);
        tvMonthLabel = findViewById(R.id.tv_month_label);
        tvDayLabel = findViewById(R.id.tv_day_label);
        tvHourLabel = findViewById(R.id.tv_hour_label);
        tvMinuteLabel = findViewById(R.id.tv_minute_label);
        tvSecondLabel = findViewById(R.id.tv_second_label);

        wheelViews.addAll(Arrays.asList(
                wvYear, wvMonth, wvDay,
                wvHour, wvMinute, wvSecond
        ));

        setWheelListener();
        initAttrs(context, attrs);
    }

    public void setMode(@DateMode int dateMode, @TimeMode int timeMode) {
        if (dateMode == DateMode.NONE || dateMode == DateMode.MONTH_DAY) {
            wvYear.setVisibility(View.GONE);
        }
        if (dateMode == DateMode.NONE) {
            wvMonth.setVisibility(View.GONE);
        }
        if (dateMode == DateMode.NONE || dateMode == DateMode.YEAR_MONTH) {
            wvDay.setVisibility(View.GONE);
        }
        if (timeMode == TimeMode.NONE) {
            wvHour.setVisibility(View.GONE);
            wvMinute.setVisibility(View.GONE);
            wvSecond.setVisibility(View.GONE);
        }
        if (timeMode != TimeMode.HOUR_24_SECOND) {
            wvSecond.setVisibility(View.GONE);
        }
        this.timeMode = timeMode;
    }

    /**
     * 设置日期时间范围
     */
    public <T extends DateTimeEntity> void setRange(@NonNull T startValue, @NonNull T endValue) {
        setRange(startValue, endValue, null);
    }

    /**
     * 设置日期时间范围
     */
    public <T extends DateTimeEntity> void setRange(@NonNull T startValue, @NonNull T endValue,
                                                    @Nullable T defaultValue) {
        this.startValue = startValue;
        this.endValue = endValue;
        changeDefaultData(defaultValue);
    }

    private <T extends DateTimeEntity> void changeDefaultData(@Nullable T defaultValue) {
        yearIndex = 0;
        monthIndex = 0;
        dayIndex = 0;
        hourIndex = 0;
        minuteIndex = 0;
        secondIndex = 0;
        changeYear();
    }

    public void setDateFormatter(final DateFormatter dateFormatter) {
        if (dateFormatter == null) {
            return;
        }
        wvYear.setNumberFormatter(new NumberFormatter<Integer>() {
            @Override
            public String format(Integer value) {
                return dateFormatter.formatYear(value);
            }
        });
        wvMonth.setNumberFormatter(new NumberFormatter<Integer>() {
            @Override
            public String format(Integer value) {
                return dateFormatter.formatMonth(value);
            }
        });
        wvDay.setNumberFormatter(new NumberFormatter<Integer>() {
            @Override
            public String format(Integer value) {
                return dateFormatter.formatDay(value);
            }
        });
    }

    public void setTimeFormatter(final TimeFormatter timeFormatter) {
        if (timeFormatter == null) {
            return;
        }
        wvHour.setNumberFormatter(new NumberFormatter<Integer>() {
            @Override
            public String format(Integer value) {
                return timeFormatter.formatHour(value);
            }
        });
        wvMinute.setNumberFormatter(new NumberFormatter<Integer>() {
            @Override
            public String format(Integer value) {
                return timeFormatter.formatMinute(value);
            }
        });
        wvSecond.setNumberFormatter(new NumberFormatter<Integer>() {
            @Override
            public String format(Integer value) {
                return timeFormatter.formatSecond(value);
            }
        });
    }

    public void setDateLabel(CharSequence year, CharSequence month, CharSequence day) {
        this.tvYearLabel.setText(year);
        this.tvMonthLabel.setText(month);
        this.tvDayLabel.setText(day);
    }

    public void setTimeLabel(CharSequence hour, CharSequence minute, CharSequence second) {
        this.tvHourLabel.setText(hour);
        this.tvMinuteLabel.setText(minute);
        this.tvSecondLabel.setText(second);
    }

    public final YearWheelView getYearWheelView() {
        return wvYear;
    }

    public final MonthWheelView getMonthWheelView() {
        return wvMonth;
    }

    public final DayWheelView getDayWheelView() {
        return wvDay;
    }

    public final HourWheelView getHourWheelView() {
        return wvHour;
    }

    public final MinuteWheelView getMinuteWheelView() {
        return wvMinute;
    }

    public final SecondWheelView getSecondWheelView() {
        return wvSecond;
    }

    public final TextView getYearLabelView() {
        return tvYearLabel;
    }

    public final TextView getTvMonthLabelView() {
        return tvMonthLabel;
    }

    public final TextView getDayLabelView() {
        return tvDayLabel;
    }

    public final TextView getHourLabelView() {
        return tvHourLabel;
    }

    public final TextView getMinuteLabelView() {
        return tvMinuteLabel;
    }

    public final TextView getSecondLabelView() {
        return tvSecondLabel;
    }

    public final int getSelectedYear() {
        return wvYear.getCurrentValue();
    }

    public final int getSelectedMonth() {
        return wvMonth.getCurrentValue();
    }

    public final int getSelectedDay() {
        return wvDay.getCurrentValue();
    }

    public final int getSelectedHour() {
        return wvHour.getCurrentValue();
    }

    public final int getSelectedMinute() {
        return wvMinute.getCurrentValue();
    }

    public final int getSelectedSecond() {
        return wvSecond.getCurrentValue();
    }

    private void setWheelListener() {
        wvYear.setOnWheelListener(new AbstractWheelSelectedListener() {
            @Override
            public void onItemSelected(WheelView wheelView, int position) {
                yearIndex = position;
                monthIndex = 0;
                dayIndex = 0;
                hourIndex = 0;
                minuteIndex = 0;
                secondIndex = 0;
                changeMonth();
            }
        });
        wvMonth.setOnWheelListener(new AbstractWheelSelectedListener() {
            @Override
            public void onItemSelected(WheelView wheelView, int position) {
                monthIndex = position;
                dayIndex = 0;
                hourIndex = 0;
                minuteIndex = 0;
                secondIndex = 0;
                changeDay();
            }
        });
        wvDay.setOnWheelListener(new AbstractWheelSelectedListener() {
            @Override
            public void onItemSelected(WheelView wheelView, int position) {
                dayIndex = position;
                hourIndex = 0;
                minuteIndex = 0;
                secondIndex = 0;
                changeHour();
            }
        });
        wvHour.setOnWheelListener(new AbstractWheelSelectedListener() {
            @Override
            public void onItemSelected(WheelView wheelView, int position) {
                hourIndex = position;
                minuteIndex = 0;
                secondIndex = 0;
                changeMinute();
            }
        });
        wvMinute.setOnWheelListener(new AbstractWheelSelectedListener() {
            @Override
            public void onItemSelected(WheelView wheelView, int position) {
                minuteIndex = position;
                secondIndex = 0;
                changeSecond();
            }
        });
        wvSecond.setOnWheelListener(new AbstractWheelSelectedListener() {
            @Override
            public void onItemSelected(WheelView wheelView, int position) {
                secondIndex = position;
            }
        });
    }

    private void changeYear() {
        final int min = Math.min(startValue.getYear(), endValue.getYear());
        final int max = Math.max(startValue.getYear(), endValue.getYear());
        wvYear.postDelayed(new Runnable() {
            @Override
            public void run() {
                wvYear.setRange(min, max);
                wvYear.setDefaultItemPosition(yearIndex);
                changeMonth();
            }
        }, DELAY_BEFORE_CHECK_PAST);
    }

    private void changeMonth() {
        int year = wvYear.getValueByPosition(yearIndex);
        final int min, max;
        //开始年份和结束年份相同（即只有一个年份，这种情况建议使用月日模式）
        if (startValue.getYear() == endValue.getYear()) {
            min = Math.min(startValue.getMonth(), endValue.getMonth());
            max = Math.max(startValue.getMonth(), endValue.getMonth());
        }
        //当前所选年份和开始年份相同
        else if (year == startValue.getYear()) {
            min = startValue.getMonth();
            max = 12;
        }
        //当前所选年份和结束年份相同
        else if (year == endValue.getYear()) {
            min = 1;
            max = endValue.getMonth();
        }
        //当前所选年份在开始年份和结束年份之间
        else {
            min = 1;
            max = 12;
        }
        wvMonth.postDelayed(new Runnable() {
            @Override
            public void run() {
                wvMonth.setRange(min, max);
                wvMonth.setDefaultItemPosition(monthIndex);
                changeDay();
            }
        }, DELAY_BEFORE_CHECK_PAST);
    }

    private void changeDay() {
        int year = wvYear.getValueByPosition(yearIndex);
        int month = wvMonth.getValueByPosition(monthIndex);
        final int min, max;
        //开始年月及结束年月相同情况
        if (year == startValue.getYear() && month == startValue.getMonth()
                && year == endValue.getYear() && month == endValue.getMonth()) {
            min = startValue.getDay();
            max = endValue.getDay();
        }
        //开始年月相同情况
        else if (year == startValue.getYear() && month == startValue.getMonth()) {
            min = startValue.getDay();
            max = CqrDateTime.getTotalDaysInMonth(year, month);
        }
        //结束年月相同情况
        else if (year == endValue.getYear() && month == endValue.getMonth()) {
            min = 1;
            max = endValue.getDay();
        } else {
            min = 1;
            max = CqrDateTime.getTotalDaysInMonth(year, month);
        }
        wvDay.postDelayed(new Runnable() {
            @Override
            public void run() {
                wvDay.setRange(min, max);
                wvDay.setDefaultItemPosition(dayIndex);
                changeHour();
            }
        }, DELAY_BEFORE_CHECK_PAST);
    }

    private void changeHour() {
        int day = wvDay.getValueByPosition(dayIndex);
        final int min, max;
        //开始日子和结束日子相同（即只有一个日子，这种情况建议使用时分模式）
        if (startValue.getDay() == endValue.getDay()) {
            min = Math.min(startValue.getHour(), endValue.getHour());
            max = Math.max(startValue.getHour(), endValue.getHour());
        }
        //当前所选日子和开始日子相同
        else if (day == startValue.getDay()) {
            min = startValue.getHour();
            max = timeMode == TimeMode.HOUR_12 ? 12 : 24;
        }
        //当前所选日子和结束日子相同
        else if (day == endValue.getDay()) {
            min = 0;
            max = endValue.getHour();
        }
        //当前所选日子在开始日子和结束日子之间
        else {
            min = 0;
            max = timeMode == TimeMode.HOUR_12 ? 12 : 24;
        }
        wvHour.postDelayed(new Runnable() {
            @Override
            public void run() {
                wvHour.setRange(min, max);
                wvHour.setDefaultItemPosition(hourIndex);
                changeMinute();
            }
        }, DELAY_BEFORE_CHECK_PAST);
    }

    private void changeMinute() {
        int day = wvDay.getValueByPosition(dayIndex);
        int hour = wvHour.getValueByPosition(hourIndex);
        final int min, max;
        //开始日时及结束日时相同情况
        if (day == startValue.getDay() && hour == startValue.getHour()
                && day == endValue.getDay() && hour == endValue.getHour()) {
            min = startValue.getMinute();
            max = endValue.getMinute();
        }
        //开始日时相同情况
        else if (day == startValue.getDay() && hour == startValue.getHour()) {
            min = startValue.getMinute();
            max = 59;
        }
        //结束日时相同情况
        else if (day == endValue.getDay() && hour == endValue.getHour()) {
            min = 0;
            max = endValue.getMinute();
        } else {
            min = 0;
            max = 59;
        }
        wvMinute.postDelayed(new Runnable() {
            @Override
            public void run() {
                wvMinute.setRange(min, max);
                wvMinute.setDefaultItemPosition(minuteIndex);
                changeSecond();
            }
        }, DELAY_BEFORE_CHECK_PAST);
    }

    private void changeSecond() {
        int hour = wvHour.getValueByPosition(hourIndex);
        int minute = wvMinute.getValueByPosition(minuteIndex);
        final int min, max;
        //开始时分及结束时分相同情况
        if (hour == startValue.getHour() && minute == startValue.getMinute()
                && hour == endValue.getHour() && minute == endValue.getMinute()) {
            min = startValue.getSecond();
            max = endValue.getSecond();
        }
        //开始时分相同情况
        else if (hour == startValue.getHour() && minute == startValue.getMinute()) {
            min = startValue.getSecond();
            max = 59;
        }
        //结束时分相同情况
        else if (hour == endValue.getHour() && minute == endValue.getMinute()) {
            min = 0;
            max = endValue.getSecond();
        } else {
            min = 0;
            max = 59;
        }
        wvSecond.postDelayed(new Runnable() {
            @Override
            public void run() {
                wvSecond.setRange(min, max);
                wvSecond.setDefaultItemPosition(secondIndex);
            }
        }, DELAY_BEFORE_CHECK_PAST);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        for (WheelView wheelView : wheelViews) {
            wheelView.setEnabled(enabled);
        }
    }

    public void setCurved(boolean curved) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setCurved(curved);
        }
    }

    public void setCyclic(boolean cyclic) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setCyclic(cyclic);
        }
    }

    public void setTextSize(int textSize) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setItemTextSize(textSize);
        }
    }

    public void setSelectedTextColor(int selectedTextColor) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setSelectedItemTextColor(selectedTextColor);
        }
    }

    public void setTextColor(int textColor) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setItemTextColor(textColor);
        }
    }

    public void setVisibleItemCount(int visibleItemCount) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setVisibleItemCount(visibleItemCount);
        }
    }

    public void setDisplayYears(boolean displayYears) {
        this.displayYears = displayYears;
        wvYear.setVisibility(displayYears ? VISIBLE : GONE);
    }

    public void setDisplayMonths(boolean displayMonths) {
        this.displayMonths = displayMonths;
        wvMonth.setVisibility(displayMonths ? VISIBLE : GONE);
    }

    public void setDisplayDays(boolean displayDays) {
        this.displayDays = displayDays;
        wvDay.setVisibility(displayDays ? VISIBLE : GONE);
    }

    public void setDisplayHours(boolean displayHours) {
        this.displayHours = displayHours;
        wvHour.setVisibility(displayHours ? VISIBLE : GONE);
    }

    public void setDisplayMinutes(boolean displayMinutes) {
        this.displayMinutes = displayMinutes;
        wvMinute.setVisibility(displayMinutes ? VISIBLE : GONE);
    }

    public void setDisplaySeconds(boolean displaySeconds) {
        this.displaySeconds = displaySeconds;
        wvSecond.setVisibility(displaySeconds ? VISIBLE : GONE);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DateTimeWheelLayout);

        setTextColor(a.getColor(R.styleable.DateTimeWheelLayout_datetime_wheel_textColor, 0xFF999999));
        setSelectedTextColor(a.getColor(R.styleable.DateTimeWheelLayout_datetime_wheel_textColorSelected, 0xFF000000));
        setTextSize(a.getDimensionPixelSize(R.styleable.DateTimeWheelLayout_datetime_wheel_textSize, CqrDensity.sp2px(context, 19)));
        setCurved(a.getBoolean(R.styleable.DateTimeWheelLayout_datetime_wheel_curved, false));
        setCyclic(a.getBoolean(R.styleable.DateTimeWheelLayout_datetime_wheel_cyclic, false));
        setVisibleItemCount(a.getInt(R.styleable.DateTimeWheelLayout_datetime_wheel_visibleItemCount, 7));
        setDisplayYears(a.getBoolean(R.styleable.DateTimeWheelLayout_datetime_wheel_displayYears, displayYears));
        setDisplayMonths(a.getBoolean(R.styleable.DateTimeWheelLayout_datetime_wheel_displayMonths, displayMonths));
        setDisplayDays(a.getBoolean(R.styleable.DateTimeWheelLayout_datetime_wheel_displayDays, displayDays));
        setDisplayHours(a.getBoolean(R.styleable.DateTimeWheelLayout_datetime_wheel_displayHours, displayHours));
        setDisplayMinutes(a.getBoolean(R.styleable.DateTimeWheelLayout_datetime_wheel_displayMinutes, displayMinutes));
        setDisplaySeconds(a.getBoolean(R.styleable.DateTimeWheelLayout_datetime_wheel_displaySeconds, displaySeconds));

        a.recycle();
    }

}
