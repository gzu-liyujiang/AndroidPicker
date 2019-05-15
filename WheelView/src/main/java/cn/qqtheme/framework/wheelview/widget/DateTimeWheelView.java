package cn.qqtheme.framework.wheelview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.qqtheme.framework.wheelview.R;
import cn.qqtheme.framework.wheelview.annotation.DateMode;
import cn.qqtheme.framework.wheelview.annotation.TimeMode;
import cn.qqtheme.framework.wheelview.entity.DateTimeEntity;
import cn.qqtheme.framework.wheelview.interfaces.DateFormatter;
import cn.qqtheme.framework.wheelview.interfaces.NumberFormatter;
import cn.qqtheme.framework.wheelview.interfaces.OnWheelSelectedListener;
import cn.qqtheme.framework.wheelview.interfaces.TimeFormatter;
import cn.qqtheme.framework.toolkit.CqrDateTime;
import cn.qqtheme.framework.toolkit.CqrDensity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * 日期时间滚轮控件，参见 https://github.com/gzu-liyujiang/AndroidPicker
 *
 * @author liyujiang
 * @date 2019/5/14 15:26
 */
public class DateTimeWheelView extends LinearLayout {
    private static final int YEAR_OFFSET = 50;
    private static final int MONTH_MIN = 1;
    private static final int MONTH_MAX = 12;
    private static final int DAY_MIN = 1;
    private static final int HOUR_MIN = 0;
    private static final int HOUR_MAX = 23;
    private static final int MINUTE_MIN = 0;
    private static final int MINUTE_MAX = 59;

    private NumberWheelView yearWheelView;
    private NumberWheelView monthWheelView;
    private NumberWheelView dayWheelView;
    private NumberWheelView hourWheelView;
    private NumberWheelView minuteWheelView;

    private TextView tvYearLabel;
    private TextView tvMonthLabel;
    private TextView tvDayLabel;
    private TextView tvHourLabel;
    private TextView tvMinuteLabel;

    private List<NumberWheelView> wheelViews = new ArrayList<>();

    private DateTimeEntity rangeStart;
    private DateTimeEntity rangeEnd;
    private DateTimeEntity defaultValue;

    private boolean displayYears = true;
    private boolean displayMonths = true;
    private boolean displayDays = true;
    private boolean displayHours = true;
    private boolean displayMinutes = true;

    public DateTimeWheelView(Context context) {
        this(context, null);
    }

    public DateTimeWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        inflate(context, R.layout.wheel_date_time, this);

        yearWheelView = findViewById(R.id.year_wheel_view);
        monthWheelView = findViewById(R.id.month_wheel_view);
        dayWheelView = findViewById(R.id.day_wheel_view);
        hourWheelView = findViewById(R.id.hour_wheel_view);
        minuteWheelView = findViewById(R.id.minute_wheel_view);

        tvYearLabel = findViewById(R.id.tv_year_label);
        tvMonthLabel = findViewById(R.id.tv_month_label);
        tvDayLabel = findViewById(R.id.tv_day_label);
        tvHourLabel = findViewById(R.id.tv_hour_label);
        tvMinuteLabel = findViewById(R.id.tv_minute_label);

        wheelViews.addAll(Arrays.asList(
                yearWheelView, monthWheelView, dayWheelView,
                hourWheelView, minuteWheelView
        ));
        initDefaultData();

        yearWheelView.setRange(rangeStart.getYear(), rangeEnd.getYear(), defaultValue.getYear());
        monthWheelView.setRange(rangeStart.getMonth(), rangeEnd.getMonth(), defaultValue.getMonth());
        dayWheelView.setRange(rangeStart.getDay(), rangeEnd.getDay(), defaultValue.getDay());
        hourWheelView.setRange(rangeStart.getHour(), rangeEnd.getHour(), defaultValue.getHour());
        minuteWheelView.setRange(rangeStart.getMinute(), rangeEnd.getMinute(), defaultValue.getMinute());

        initAttrs(context, attrs);
    }

    private void initDefaultData() {
        int defaultYear = Calendar.getInstance().get(Calendar.YEAR);
        int minYear = defaultYear - YEAR_OFFSET;
        int maxYear = defaultYear + YEAR_OFFSET;
        int defaultMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        Calendar calendarDay = Calendar.getInstance();
        int defaultDay = calendarDay.get(Calendar.DAY_OF_MONTH);
        int maxDay = calendarDay.getActualMaximum(Calendar.DAY_OF_MONTH);
        int defaultHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int defaultMinute = Calendar.getInstance().get(Calendar.MINUTE);

        rangeStart = new DateTimeEntity(minYear, MONTH_MIN, DAY_MIN, HOUR_MIN, MINUTE_MIN);
        rangeEnd = new DateTimeEntity(maxYear, MONTH_MAX, maxDay, HOUR_MAX, MINUTE_MAX);
        defaultValue = new DateTimeEntity(defaultYear, defaultMonth, defaultDay, defaultHour, defaultMinute);
    }

    public void setMode(@DateMode int dateMode, @TimeMode int timeMode) {
        if (dateMode == DateMode.NONE || dateMode == DateMode.MONTH_DAY) {
            yearWheelView.setVisibility(View.GONE);
        }
        if (dateMode == DateMode.NONE) {
            monthWheelView.setVisibility(View.GONE);
        }
        if (dateMode == DateMode.NONE || dateMode == DateMode.YEAR_MONTH) {
            dayWheelView.setVisibility(View.GONE);
        }
        if (timeMode == TimeMode.NONE) {
            hourWheelView.setVisibility(View.GONE);
            minuteWheelView.setVisibility(View.GONE);
        }
    }

    /**
     * 设置日期时间范围
     */
    public void setRange(DateTimeEntity rangeStart, DateTimeEntity rangeEnd) {
        setRange(rangeStart, rangeEnd, null);
    }

    /**
     * 设置日期时间范围
     */
    public void setRange(DateTimeEntity rangeStart, DateTimeEntity rangeEnd, DateTimeEntity defaultValue) {
        if (rangeStart != null && rangeEnd != null) {
            this.rangeStart = rangeStart;
            this.rangeEnd = rangeEnd;
            this.defaultValue = defaultValue;
        }
        changeYear();
        changeMonth();
        changeDay();
        changeHour();
        changeMinute();
    }

    public void setDateFormatter(final DateFormatter dateFormatter) {
        if (dateFormatter == null) {
            return;
        }
        yearWheelView.setNumberFormatter(new NumberFormatter() {
            @Override
            public String format(float value) {
                return dateFormatter.formatYear((int) value);
            }
        });
        monthWheelView.setNumberFormatter(new NumberFormatter() {
            @Override
            public String format(float value) {
                return dateFormatter.formatMonth((int) value);
            }
        });
        dayWheelView.setNumberFormatter(new NumberFormatter() {
            @Override
            public String format(float value) {
                return dateFormatter.formatDay((int) value);
            }
        });
    }

    public void setTimeFormatter(final TimeFormatter timeFormatter) {
        if (timeFormatter == null) {
            return;
        }
        hourWheelView.setNumberFormatter(new NumberFormatter() {
            @Override
            public String format(float value) {
                return timeFormatter.formatHour((int) value);
            }
        });
        minuteWheelView.setNumberFormatter(new NumberFormatter() {
            @Override
            public String format(float value) {
                return timeFormatter.formatMinute((int) value);
            }
        });
    }

    public void setDateLabel(CharSequence year, CharSequence month, CharSequence day) {
        this.tvYearLabel.setText(year);
        this.tvMonthLabel.setText(month);
        this.tvDayLabel.setText(day);
    }

    public void setTimeLabel(CharSequence hour, CharSequence minute) {
        this.tvHourLabel.setText(hour);
        this.tvMinuteLabel.setText(minute);
    }

    public final NumberWheelView getYearWheelView() {
        return yearWheelView;
    }

    public final NumberWheelView getMonthWheelView() {
        return monthWheelView;
    }

    public final NumberWheelView getDayWheelView() {
        return dayWheelView;
    }

    public final NumberWheelView getHourWheelView() {
        return hourWheelView;
    }

    public final NumberWheelView getMinuteWheelView() {
        return minuteWheelView;
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

    public final int getSelectedYear() {
        return (int) yearWheelView.getCurrentValue();
    }

    public final int getSelectedMonth() {
        return (int) monthWheelView.getCurrentValue();
    }

    public final int getSelectedDay() {
        return (int) dayWheelView.getCurrentValue();
    }

    public final int getSelectedHour() {
        return (int) hourWheelView.getCurrentValue();
    }

    public final int getSelectedMinute() {
        return (int) minuteWheelView.getCurrentValue();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        yearWheelView.setOnWheelSelectedListener(new AbstractWheelSelectedListener() {
            @Override
            public void onItemSelected(WheelView wheelView, int position, String item) {
                changeMonth();
                changeDay();
                changeHour();
                changeMinute();
            }
        });
        monthWheelView.setOnWheelSelectedListener(new AbstractWheelSelectedListener() {
            @Override
            public void onItemSelected(WheelView wheelView, int position, String item) {
                changeDay();
                changeHour();
                changeMinute();
            }
        });
        dayWheelView.setOnWheelSelectedListener(new AbstractWheelSelectedListener() {
            @Override
            public void onItemSelected(WheelView wheelView, int position, String item) {
                changeHour();
                changeMinute();
            }
        });
        hourWheelView.setOnWheelSelectedListener(new AbstractWheelSelectedListener() {
            @Override
            public void onItemSelected(WheelView wheelView, int position, String item) {
                changeMinute();
            }
        });
    }

    private void changeYear() {
        int min = Math.min(rangeStart.getYear(), rangeEnd.getYear());
        int max = Math.max(rangeStart.getYear(), rangeEnd.getYear());
        if (defaultValue == null) {
            yearWheelView.setRange(min, max);
        } else {
            yearWheelView.setRange(min, max, defaultValue.getYear());
        }
    }

    private void changeMonth() {
        int year = (int) yearWheelView.getCurrentValue();
        int min, max;
        //开始年份和结束年份相同（即只有一个年份，这种情况建议使用月日模式）
        if (rangeStart.getYear() == rangeEnd.getYear()) {
            min = Math.min(rangeStart.getMonth(), rangeEnd.getMonth());
            max = Math.max(rangeStart.getMonth(), rangeEnd.getMonth());
        }
        //当前所选年份和开始年份相同
        else if (year == rangeStart.getYear()) {
            min = rangeStart.getMonth();
            max = MONTH_MAX;
        }
        //当前所选年份和结束年份相同
        else if (year == rangeEnd.getYear()) {
            min = MONTH_MIN;
            max = rangeEnd.getMonth();
        }
        //当前所选年份在开始年份和结束年份之间
        else {
            min = MONTH_MIN;
            max = MONTH_MAX;
        }
        if (defaultValue == null) {
            monthWheelView.setRange(min, max);
        } else {
            monthWheelView.setRange(min, max, defaultValue.getMonth());
        }
    }

    private void changeDay() {
        int year = (int) yearWheelView.getCurrentValue();
        int month = (int) monthWheelView.getCurrentValue();
        int min, max;
        //开始年月及结束年月相同情况
        if (year == rangeStart.getYear() && month == rangeStart.getMonth()
                && year == rangeEnd.getYear() && month == rangeEnd.getMonth()) {
            min = rangeStart.getDay();
            max = rangeEnd.getDay();
        }
        //开始年月相同情况
        else if (year == rangeStart.getYear() && month == rangeStart.getMonth()) {
            min = rangeStart.getDay();
            max = CqrDateTime.getTotalDaysInMonth(year, month);
        }
        //结束年月相同情况
        else if (year == rangeEnd.getYear() && month == rangeEnd.getMonth()) {
            min = DAY_MIN;
            max = rangeEnd.getDay();
        } else {
            min = DAY_MIN;
            max = CqrDateTime.getTotalDaysInMonth(year, month);
        }
        if (defaultValue == null) {
            dayWheelView.setRange(min, max);
        } else {
            dayWheelView.setRange(min, max, defaultValue.getDay());
        }
    }

    private void changeHour() {
        int day = (int) dayWheelView.getCurrentValue();
        int min, max;
        //开始日子和结束日子相同（即只有一个日子，这种情况建议使用时分模式）
        if (rangeStart.getDay() == rangeEnd.getDay()) {
            min = Math.min(rangeStart.getHour(), rangeEnd.getHour());
            max = Math.max(rangeStart.getHour(), rangeEnd.getHour());
        }
        //当前所选日子和开始日子相同
        else if (day == rangeStart.getDay()) {
            min = rangeStart.getHour();
            max = HOUR_MAX;
        }
        //当前所选日子和结束日子相同
        else if (day == rangeEnd.getDay()) {
            min = HOUR_MIN;
            max = rangeEnd.getHour();
        }
        //当前所选日子在开始日子和结束日子之间
        else {
            min = HOUR_MIN;
            max = HOUR_MAX;
        }
        if (defaultValue == null) {
            hourWheelView.setRange(min, max);
        } else {
            hourWheelView.setRange(min, max, defaultValue.getHour());
        }
    }

    private void changeMinute() {
        int day = (int) dayWheelView.getCurrentValue();
        int hour = (int) hourWheelView.getCurrentValue();
        int min, max;
        //开始日时及结束日时相同情况
        if (day == rangeStart.getDay() && hour == rangeStart.getHour()
                && day == rangeEnd.getDay() && hour == rangeEnd.getHour()) {
            min = rangeStart.getMinute();
            max = rangeEnd.getMinute();
        }
        //开始日时相同情况
        else if (day == rangeStart.getDay() && hour == rangeStart.getHour()) {
            min = rangeStart.getMinute();
            max = MINUTE_MAX;
        }
        //结束日时相同情况
        else if (day == rangeEnd.getDay() && hour == rangeEnd.getHour()) {
            min = MINUTE_MIN;
            max = rangeEnd.getDay();
        } else {
            min = MINUTE_MIN;
            max = MINUTE_MAX;
        }
        if (defaultValue == null) {
            minuteWheelView.setRange(min, max);
        } else {
            minuteWheelView.setRange(min, max, defaultValue.getMinute());
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        for (NumberWheelView wheelView : wheelViews) {
            wheelView.setEnabled(enabled);
        }
    }

    public void setCurved(boolean curved) {
        for (NumberWheelView wheelView : wheelViews) {
            wheelView.setCurved(curved);
        }
    }

    public void setCyclic(boolean cyclic) {
        for (NumberWheelView wheelView : wheelViews) {
            wheelView.setCyclic(cyclic);
        }
    }

    public void setTextSize(int textSize) {
        for (NumberWheelView wheelView : wheelViews) {
            wheelView.setItemTextSize(textSize);
        }
    }

    public void setSelectedTextColor(int selectedTextColor) {
        for (NumberWheelView wheelView : wheelViews) {
            wheelView.setSelectedItemTextColor(selectedTextColor);
        }
    }

    public void setTextColor(int textColor) {
        for (NumberWheelView wheelView : wheelViews) {
            wheelView.setItemTextColor(textColor);
        }
    }

    public void setVisibleItemCount(int visibleItemCount) {
        for (NumberWheelView wheelView : wheelViews) {
            wheelView.setVisibleItemCount(visibleItemCount);
        }
    }

    public void setDisplayYears(boolean displayYears) {
        this.displayYears = displayYears;
        yearWheelView.setVisibility(displayYears ? VISIBLE : GONE);
    }

    public void setDisplayMonths(boolean displayMonths) {
        this.displayMonths = displayMonths;
        monthWheelView.setVisibility(displayMonths ? VISIBLE : GONE);
    }

    public void setDisplayDays(boolean displayDays) {
        this.displayDays = displayDays;
        dayWheelView.setVisibility(displayDays ? VISIBLE : GONE);
    }

    public void setDisplayHours(boolean displayHours) {
        this.displayHours = displayHours;
        hourWheelView.setVisibility(displayHours ? VISIBLE : GONE);
    }

    public void setDisplayMinutes(boolean displayMinutes) {
        this.displayMinutes = displayMinutes;
        minuteWheelView.setVisibility(displayMinutes ? VISIBLE : GONE);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DateTimeWheelView);

        setTextColor(a.getColor(R.styleable.DateTimeWheelView_wheel_dt_textColor, 0xFF999999));
        setSelectedTextColor(a.getColor(R.styleable.DateTimeWheelView_wheel_dt_textColorSelected, 0xFF000000));
        setTextSize(a.getDimensionPixelSize(R.styleable.DateTimeWheelView_wheel_dt_textSize, CqrDensity.sp2px(context, 19)));
        setCurved(a.getBoolean(R.styleable.DateTimeWheelView_wheel_dt_curved, false));
        setCyclic(a.getBoolean(R.styleable.DateTimeWheelView_wheel_dt_cyclic, false));
        setVisibleItemCount(a.getInt(R.styleable.DateTimeWheelView_wheel_dt_visibleItemCount, 7));
        setDisplayYears(a.getBoolean(R.styleable.DateTimeWheelView_wheel_dt_displayYears, displayYears));
        setDisplayMonths(a.getBoolean(R.styleable.DateTimeWheelView_wheel_dt_displayMonths, displayMonths));
        setDisplayDays(a.getBoolean(R.styleable.DateTimeWheelView_wheel_dt_displayDays, displayDays));
        setDisplayHours(a.getBoolean(R.styleable.DateTimeWheelView_wheel_dt_displayHours, displayHours));
        setDisplayMinutes(a.getBoolean(R.styleable.DateTimeWheelView_wheel_dt_displayMinutes, displayMinutes));

        a.recycle();
    }

    static abstract class AbstractWheelSelectedListener implements OnWheelSelectedListener<String> {
        @Override
        public void onCurrentItemOfScroll(WheelView wheelView, int position) {
        }

    }

}
