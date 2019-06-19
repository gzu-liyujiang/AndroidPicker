package cn.qqtheme.framework.wheelview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.qqtheme.framework.toolkit.CqrDateTime;
import cn.qqtheme.framework.toolkit.CqrDensity;
import cn.qqtheme.framework.wheelview.R;
import cn.qqtheme.framework.wheelview.annotation.DateMode;
import cn.qqtheme.framework.wheelview.annotation.ItemAlign;
import cn.qqtheme.framework.wheelview.annotation.TimeMode;
import cn.qqtheme.framework.wheelview.entity.DateTimeEntity;
import cn.qqtheme.framework.wheelview.interfaces.DateFormatter;
import cn.qqtheme.framework.wheelview.interfaces.NumberFormatter;
import cn.qqtheme.framework.wheelview.interfaces.OnWheelChangedListener;
import cn.qqtheme.framework.wheelview.interfaces.OnWheelSelectedListener;
import cn.qqtheme.framework.wheelview.interfaces.TimeFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 日期时间滚轮控件
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/5/14 15:26
 * @since 2.0
 */
@SuppressWarnings("unused")
public class DateTimeWheelLayout extends LinearLayout implements OnWheelSelectedListener<Integer>,
        OnWheelChangedListener {

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

    private Integer selectedYear;
    private Integer selectedMonth;
    private Integer selectedDay;
    private Integer selectedHour;
    private Integer selectedMinute;
    private Integer selectedSecond;

    private int timeMode = TimeMode.HOUR_24_SECOND;

    private boolean displayYear = true;
    private boolean displayMonth = true;
    private boolean displayDay = true;
    private boolean displayHour = true;
    private boolean displayMinute = true;
    private boolean displaySecond = true;

    private AttributeSet attrs;

    public DateTimeWheelLayout(Context context) {
        this(context, null);
    }

    public DateTimeWheelLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.WheelDateTimeStyle);
    }

    public DateTimeWheelLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, R.style.WheelDateTime);
    }

    public DateTimeWheelLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        this.attrs = attrs;
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
        initAttrs(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setStyle(@StyleRes int style) {
        if (attrs == null) {
            throw new RuntimeException("Please use " + getClass().getSimpleName() + " in xml");
        }
        initAttrs(getContext(), attrs, R.attr.WheelDateTimeStyle, style);
        requestLayout();
        postInvalidate();
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
        if (defaultValue != null) {
            selectedYear = defaultValue.getYear();
            selectedMonth = defaultValue.getMonth();
            selectedDay = defaultValue.getDay();
            selectedHour = defaultValue.getHour();
            selectedMinute = defaultValue.getMinute();
            selectedSecond = defaultValue.getSecond();
        }
        changeYear();
    }

    public void setDateFormatter(final DateFormatter dateFormatter) {
        if (dateFormatter == null) {
            return;
        }
        wvYear.setFormatter(new NumberFormatter<Integer>() {
            @Override
            public String format(Integer value) {
                return dateFormatter.formatYear(value);
            }
        });
        wvMonth.setFormatter(new NumberFormatter<Integer>() {
            @Override
            public String format(Integer value) {
                return dateFormatter.formatMonth(value);
            }
        });
        wvDay.setFormatter(new NumberFormatter<Integer>() {
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
        wvHour.setFormatter(new NumberFormatter<Integer>() {
            @Override
            public String format(Integer value) {
                return timeFormatter.formatHour(value);
            }
        });
        wvMinute.setFormatter(new NumberFormatter<Integer>() {
            @Override
            public String format(Integer value) {
                return timeFormatter.formatMinute(value);
            }
        });
        wvSecond.setFormatter(new NumberFormatter<Integer>() {
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
        return wvYear.getCurrentItem();
    }

    public final int getSelectedMonth() {
        return wvMonth.getCurrentItem();
    }

    public final int getSelectedDay() {
        return wvDay.getCurrentItem();
    }

    public final int getSelectedHour() {
        return wvHour.getCurrentItem();
    }

    public final int getSelectedMinute() {
        return wvMinute.getCurrentItem();
    }

    public final int getSelectedSecond() {
        return wvSecond.getCurrentItem();
    }

    private void setWheelListener() {
        wvYear.setWheelSelectedListener(this);
        wvMonth.setWheelSelectedListener(this);
        wvDay.setWheelSelectedListener(this);
        wvHour.setWheelSelectedListener(this);
        wvMinute.setWheelSelectedListener(this);
        wvSecond.setWheelSelectedListener(this);

        wvYear.setWheelChangedListener(this);
        wvMonth.setWheelChangedListener(this);
        wvDay.setWheelChangedListener(this);
        wvHour.setWheelChangedListener(this);
        wvMinute.setWheelChangedListener(this);
        wvSecond.setWheelChangedListener(this);
    }

    @Override
    public void onItemSelected(WheelView wheelView, int position, Integer item) {
        if (wheelView instanceof YearWheelView) {
            selectedYear = item;
            selectedMonth = null;
            selectedDay = null;
            selectedHour = null;
            selectedMinute = null;
            selectedSecond = null;
            changeMonth(item);
        } else if (wheelView instanceof MonthWheelView) {
            selectedMonth = item;
            selectedDay = null;
            selectedHour = null;
            selectedMinute = null;
            selectedSecond = null;
            changeDay(selectedYear, item);
        } else if (wheelView instanceof DayWheelView) {
            selectedDay = item;
            selectedHour = null;
            selectedMinute = null;
            selectedSecond = null;
            changeHour(selectedYear, selectedMonth, item);
        } else if (wheelView instanceof HourWheelView) {
            selectedHour = item;
            selectedMinute = null;
            selectedSecond = null;
            changeMinute(selectedYear, selectedMonth, selectedDay, item);
        } else if (wheelView instanceof MinuteWheelView) {
            selectedMinute = item;
            selectedSecond = null;
            changeSecond(selectedYear, selectedMonth, selectedDay, selectedHour, item);
        } else if (wheelView instanceof SecondWheelView) {
            selectedSecond = item;
        }
    }

    @Override
    public void onWheelScrolled(WheelView wheelView, int offset) {

    }

    @Override
    public void onWheelSelected(WheelView wheelView, int position) {

    }

    @Override
    public void onWheelScrollStateChanged(WheelView wheelView, int state) {
        // 除当前操作的滚轮外，其他滚轮在有滚轮滚动时设置触摸事件不可用，
        // 防止快速来回切换多个滚轮可能会出现某些项显示为空的问题
        setEnabled(state == WheelView.SCROLL_STATE_IDLE);
        wheelView.setEnabled(true);
    }

    private void changeYear() {
        final int min = Math.min(startValue.getYear(), endValue.getYear());
        final int max = Math.max(startValue.getYear(), endValue.getYear());
        if (selectedYear == null) {
            selectedYear = min;
        }
        wvYear.setRange(min, max, selectedYear);
        changeMonth(selectedYear);
    }

    private void changeMonth(int year) {
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
        if (selectedMonth == null) {
            selectedMonth = min;
        }
        wvMonth.setRange(min, max, selectedMonth);
        changeDay(year, selectedMonth);
    }

    private void changeDay(int year, int month) {
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
        if (selectedDay == null) {
            selectedDay = min;
        }
        wvDay.setRange(min, max, selectedDay);
        changeHour(year, month, selectedDay);
    }

    private void changeHour(int year, int month, int day) {
        final int min, max;
        //开始日子和结束日子相同（即只有一个日子，这种情况建议使用时分模式）
        if (startValue.getYear() == endValue.getYear() &&
                startValue.getMonth() == endValue.getMonth() &&
                startValue.getDay() == endValue.getDay()) {
            min = Math.min(startValue.getHour(), endValue.getHour());
            max = Math.max(startValue.getHour(), endValue.getHour());
        }
        //当前所选日子和开始日子相同
        else if (startValue.getYear() == endValue.getYear() &&
                startValue.getMonth() == endValue.getMonth() &&
                day == startValue.getDay()) {
            min = startValue.getHour();
            max = timeMode == TimeMode.HOUR_12 ? 12 : 23;
        }
        //当前所选日子和结束日子相同
        else if (startValue.getYear() == endValue.getYear() &&
                startValue.getMonth() == endValue.getMonth() &&
                day == endValue.getDay()) {
            min = 0;
            max = endValue.getHour();
        }
        //当前所选日子在开始日子和结束日子之间
        else {
            min = 0;
            max = timeMode == TimeMode.HOUR_12 ? 12 : 23;
        }
        if (selectedHour == null) {
            selectedHour = min;
        }
        wvHour.setRange(min, max, selectedHour);
        changeMinute(year, month, day, selectedHour);
    }

    private void changeMinute(int year, int month, int day, int hour) {
        final int min, max;
        //开始日时及结束日时相同情况
        if (startValue.getYear() == endValue.getYear() &&
                startValue.getMonth() == endValue.getMonth() &&
                day == startValue.getDay() && hour == startValue.getHour()
                && day == endValue.getDay() && hour == endValue.getHour()) {
            min = startValue.getMinute();
            max = endValue.getMinute();
        }
        //开始日时相同情况
        else if (startValue.getYear() == endValue.getYear() &&
                startValue.getMonth() == endValue.getMonth() &&
                day == startValue.getDay() && hour == startValue.getHour()) {
            min = startValue.getMinute();
            max = 59;
        }
        //结束日时相同情况
        else if (startValue.getYear() == endValue.getYear() &&
                startValue.getMonth() == endValue.getMonth() &&
                day == endValue.getDay() && hour == endValue.getHour()) {
            min = 0;
            max = endValue.getMinute();
        } else {
            min = 0;
            max = 59;
        }
        if (selectedMinute == null) {
            selectedMinute = min;
        }
        wvMinute.setRange(min, max, selectedMinute);
        changeSecond(year, month, day, hour, selectedMinute);
    }

    private void changeSecond(int year, int month, int day, int hour, int minute) {
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
        if (selectedSecond == null) {
            selectedSecond = min;
        }
        wvSecond.setRange(min, max, selectedSecond);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        for (WheelView wheelView : wheelViews) {
            wheelView.setEnabled(enabled);
        }
    }

    public void setCurtain(boolean hasCurtain) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setCurtain(hasCurtain);
        }
    }

    public void setCurtainColor(@ColorInt int color) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setCurtainColor(color);
        }
    }

    public void setAtmospheric(boolean hasAtmospheric) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setAtmospheric(hasAtmospheric);
        }
    }

    public void setCurved(boolean curved) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setCurved(curved);
        }
    }

    public void setItemSpace(int space) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setItemSpace(space);
        }
    }

    public void setCyclic(boolean cyclic) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setCyclic(cyclic);
        }
    }

    public void setIndicator(boolean hasIndicator) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setIndicator(hasIndicator);
        }
    }

    public void setIndicatorSize(int size) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setIndicatorSize(size);
        }
    }

    public void setIndicatorColor(@ColorInt int color) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setIndicatorColor(color);
        }
    }

    public void setTextSize(int textSize) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setTextSize(textSize);
        }
    }

    public void setSameWidth(boolean hasSameWidth) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setSameWidth(hasSameWidth);
        }
    }

    public void setDefaultItemPosition(int position) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setDefaultItemPosition(position);
        }
    }

    public void setMaxWidthTextPosition(int position) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setMaxWidthTextPosition(position);
        }
    }

    public void setMaxWidthText(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        for (WheelView wheelView : wheelViews) {
            wheelView.setMaxWidthText(text);
        }
    }

    public void setSelectedTextColor(int selectedTextColor) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setSelectedTextColor(selectedTextColor);
        }
    }

    public void setTextColor(int textColor) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setTextColor(textColor);
        }
    }

    public void setVisibleItemCount(int visibleItemCount) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setVisibleItemCount(visibleItemCount);
        }
    }

    public void setItemAlign(@ItemAlign int align) {
        for (WheelView wheelView : wheelViews) {
            wheelView.setItemAlign(align);
        }
    }

    public void setDisplayYear(boolean displayYear) {
        this.displayYear = displayYear;
        wvYear.setVisibility(displayYear ? VISIBLE : GONE);
    }

    public void setDisplayMonth(boolean displayMonth) {
        this.displayMonth = displayMonth;
        wvMonth.setVisibility(displayMonth ? VISIBLE : GONE);
    }

    public void setDisplayDay(boolean displayDay) {
        this.displayDay = displayDay;
        wvDay.setVisibility(displayDay ? VISIBLE : GONE);
    }

    public void setDisplayHour(boolean displayHour) {
        this.displayHour = displayHour;
        wvHour.setVisibility(displayHour ? VISIBLE : GONE);
    }

    public void setDisplayMinute(boolean displayMinute) {
        this.displayMinute = displayMinute;
        wvMinute.setVisibility(displayMinute ? VISIBLE : GONE);
    }

    public void setDisplaySecond(boolean displaySecond) {
        this.displaySecond = displaySecond;
        wvSecond.setVisibility(displaySecond ? VISIBLE : GONE);
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        if (attrs == null) {
            return;
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DateTimeWheelLayout,
                defStyleAttr, defStyleRes);

        setTextSize(a.getDimensionPixelSize(R.styleable.DateTimeWheelLayout_wheel_itemTextSize,
                CqrDensity.sp2px(context, 20)));
        setVisibleItemCount(a.getInt(R.styleable.DateTimeWheelLayout_wheel_itemVisibleCount, 5));
        setDefaultItemPosition(a.getInt(R.styleable.DateTimeWheelLayout_wheel_itemDefaultPosition, 0));
        setSameWidth(a.getBoolean(R.styleable.DateTimeWheelLayout_wheel_hasSameWidth, false));
        setMaxWidthTextPosition(a.getInt(R.styleable.DateTimeWheelLayout_wheel_maxWidthTextPosition, 0));
        setMaxWidthText(a.getString(R.styleable.DateTimeWheelLayout_wheel_maxWidthText));
        setSelectedTextColor(a.getColor(R.styleable.DateTimeWheelLayout_wheel_itemTextColorSelected, 0xFF000000));
        setTextColor(a.getColor(R.styleable.DateTimeWheelLayout_wheel_itemTextColor, 0xFF888888));
        setItemSpace(a.getDimensionPixelSize(R.styleable.DateTimeWheelLayout_wheel_itemSpace,
                CqrDensity.dp2px(context, 15)));
        setCyclic(a.getBoolean(R.styleable.DateTimeWheelLayout_wheel_cyclic, false));
        setIndicator(a.getBoolean(R.styleable.DateTimeWheelLayout_wheel_indicator, false));
        setIndicatorColor(a.getColor(R.styleable.DateTimeWheelLayout_wheel_indicatorColor, 0xFFEE3333));
        setIndicatorSize(a.getDimensionPixelSize(R.styleable.DateTimeWheelLayout_wheel_indicatorSize,
                CqrDensity.sp2px(context, 1)));
        setCurtain(a.getBoolean(R.styleable.DateTimeWheelLayout_wheel_curtain, false));
        setCurtainColor(a.getColor(R.styleable.DateTimeWheelLayout_wheel_curtainColor, 0x88FFFFFF));
        setAtmospheric(a.getBoolean(R.styleable.DateTimeWheelLayout_wheel_atmospheric, false));
        setCurved(a.getBoolean(R.styleable.DateTimeWheelLayout_wheel_curved, false));
        setItemAlign(a.getInt(R.styleable.DateTimeWheelLayout_wheel_itemAlign, ItemAlign.CENTER));
        setDisplayYear(a.getBoolean(R.styleable.DateTimeWheelLayout_wheel_displayYear, displayYear));
        setDisplayMonth(a.getBoolean(R.styleable.DateTimeWheelLayout_wheel_displayMonth, displayMonth));
        setDisplayDay(a.getBoolean(R.styleable.DateTimeWheelLayout_wheel_displayDay, displayDay));
        setDisplayHour(a.getBoolean(R.styleable.DateTimeWheelLayout_wheel_displayHour, displayHour));
        setDisplayMinute(a.getBoolean(R.styleable.DateTimeWheelLayout_wheel_displayMinute, displayMinute));
        setDisplaySecond(a.getBoolean(R.styleable.DateTimeWheelLayout_wheel_displaySecond, displaySecond));

        a.recycle();
    }

}
