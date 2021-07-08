/*
 * Copyright (c) 2016-present 贵州纳雍穿青人李裕江<1032694760@qq.com>
 *
 * The software is licensed under the Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *     http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 * PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package com.github.gzuliyujiang.wheelpicker.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.gzuliyujiang.wheelpicker.R;
import com.github.gzuliyujiang.wheelpicker.annotation.DateMode;
import com.github.gzuliyujiang.wheelpicker.contract.DateFormatter;
import com.github.gzuliyujiang.wheelpicker.contract.OnDateSelectedListener;
import com.github.gzuliyujiang.wheelpicker.entity.DateEntity;
import com.github.gzuliyujiang.wheelpicker.impl.SimpleDateFormatter;
import com.github.gzuliyujiang.wheelview.annotation.ItemTextAlign;
import com.github.gzuliyujiang.wheelview.contract.WheelFormatter;
import com.github.gzuliyujiang.wheelview.widget.NumberWheelView;
import com.github.gzuliyujiang.wheelview.widget.WheelView;

import java.util.Arrays;
import java.util.List;

/**
 * 日期滚轮控件
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2021/6/5 16:12
 */
@SuppressWarnings("unused")
public class DateWheelLayout extends BaseWheelLayout {
    private NumberWheelView yearWheelView;
    private NumberWheelView monthWheelView;
    private NumberWheelView dayWheelView;
    private TextView yearLabelView;
    private TextView monthLabelView;
    private TextView dayLabelView;
    private DateEntity startValue;
    private DateEntity endValue;
    private Integer selectedYear;
    private Integer selectedMonth;
    private Integer selectedDay;
    private OnDateSelectedListener onDateSelectedListener;

    public DateWheelLayout(Context context) {
        super(context);
    }

    public DateWheelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DateWheelLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DateWheelLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected int provideLayoutRes() {
        return R.layout.wheel_picker_date;
    }

    @Override
    protected int[] provideStyleableRes() {
        return R.styleable.DateWheelLayout;
    }

    @Override
    protected List<WheelView> provideWheelViews() {
        return Arrays.asList(yearWheelView, monthWheelView, dayWheelView);
    }

    @Override
    protected void onInit(@NonNull Context context) {
        yearWheelView = findViewById(R.id.wheel_picker_date_year_wheel);
        monthWheelView = findViewById(R.id.wheel_picker_date_month_wheel);
        dayWheelView = findViewById(R.id.wheel_picker_date_day_wheel);
        yearLabelView = findViewById(R.id.wheel_picker_date_year_label);
        monthLabelView = findViewById(R.id.wheel_picker_date_month_label);
        dayLabelView = findViewById(R.id.wheel_picker_date_day_label);
        setDateFormatter(new SimpleDateFormatter());
        setRange(DateEntity.today(), DateEntity.yearOnFuture(30));
    }

    @Override
    protected void onAttributeSet(@NonNull Context context, @NonNull TypedArray typedArray) {
        float density = context.getResources().getDisplayMetrics().density;
        setTextSize(typedArray.getDimensionPixelSize(R.styleable.DateWheelLayout_wheel_itemTextSize,
                (int) (15 * context.getResources().getDisplayMetrics().scaledDensity)));
        setVisibleItemCount(typedArray.getInt(R.styleable.DateWheelLayout_wheel_visibleItemCount, 5));
        setSameWidthEnabled(typedArray.getBoolean(R.styleable.DateWheelLayout_wheel_sameWidthEnabled, false));
        setMaxWidthText(typedArray.getString(R.styleable.DateWheelLayout_wheel_maxWidthText));
        setSelectedTextColor(typedArray.getColor(R.styleable.DateWheelLayout_wheel_itemTextColorSelected, 0xFF000000));
        setTextColor(typedArray.getColor(R.styleable.DateWheelLayout_wheel_itemTextColor, 0xFF888888));
        setItemSpace(typedArray.getDimensionPixelSize(R.styleable.DateWheelLayout_wheel_itemSpace,
                (int) (20 * density)));
        setCyclicEnabled(typedArray.getBoolean(R.styleable.DateWheelLayout_wheel_cyclicEnabled, false));
        setIndicatorEnabled(typedArray.getBoolean(R.styleable.DateWheelLayout_wheel_indicatorEnabled, false));
        setIndicatorColor(typedArray.getColor(R.styleable.DateWheelLayout_wheel_indicatorColor, 0xFFEE3333));
        setIndicatorSize(typedArray.getDimension(R.styleable.DateWheelLayout_wheel_indicatorSize, 1 * density));
        setCurvedIndicatorSpace(typedArray.getDimensionPixelSize(R.styleable.DateWheelLayout_wheel_curvedIndicatorSpace, (int) (1 * density)));
        setCurtainEnabled(typedArray.getBoolean(R.styleable.DateWheelLayout_wheel_curtainEnabled, false));
        setCurtainColor(typedArray.getColor(R.styleable.DateWheelLayout_wheel_curtainColor, 0x88FFFFFF));
        setAtmosphericEnabled(typedArray.getBoolean(R.styleable.DateWheelLayout_wheel_atmosphericEnabled, false));
        setCurvedEnabled(typedArray.getBoolean(R.styleable.DateWheelLayout_wheel_curvedEnabled, false));
        setCurvedMaxAngle(typedArray.getInteger(R.styleable.DateWheelLayout_wheel_curvedMaxAngle, 90));
        setTextAlign(typedArray.getInt(R.styleable.DateWheelLayout_wheel_itemTextAlign, ItemTextAlign.CENTER));
        setDateMode(typedArray.getInt(R.styleable.DateWheelLayout_wheel_dateMode, DateMode.YEAR_MONTH_DAY));
        String yearLabel = typedArray.getString(R.styleable.DateWheelLayout_wheel_yearLabel);
        String monthLabel = typedArray.getString(R.styleable.DateWheelLayout_wheel_monthLabel);
        String dayLabel = typedArray.getString(R.styleable.DateWheelLayout_wheel_dayLabel);
        setDateLabel(yearLabel, monthLabel, dayLabel);
    }

    @Override
    public void onWheelSelected(WheelView view, int position) {
        int id = view.getId();
        if (id == R.id.wheel_picker_date_year_wheel) {
            selectedYear = (Integer) yearWheelView.getItem(position);
            selectedMonth = null;
            selectedDay = null;
            changeMonth(selectedYear);
            dateSelectedCallback();
            return;
        }
        if (id == R.id.wheel_picker_date_month_wheel) {
            selectedMonth = (Integer) monthWheelView.getItem(position);
            selectedDay = null;
            changeDay(selectedYear, selectedMonth);
            dateSelectedCallback();
            return;
        }
        if (id == R.id.wheel_picker_date_day_wheel) {
            selectedDay = (Integer) dayWheelView.getItem(position);
            dateSelectedCallback();
        }
    }

    private void dateSelectedCallback() {
        if (onDateSelectedListener == null) {
            return;
        }
        dayWheelView.post(new Runnable() {
            @Override
            public void run() {
                onDateSelectedListener.onDateSelected(selectedYear, selectedMonth, selectedDay);
            }
        });
    }

    public void setDateMode(int dateMode) {
        if (dateMode == DateMode.NONE) {
            yearWheelView.setVisibility(View.GONE);
            yearLabelView.setVisibility(View.GONE);
            monthWheelView.setVisibility(View.GONE);
            monthLabelView.setVisibility(View.GONE);
            dayWheelView.setVisibility(View.GONE);
            dayLabelView.setVisibility(View.GONE);
            return;
        }
        if (dateMode == DateMode.MONTH_DAY) {
            yearWheelView.setVisibility(View.GONE);
            yearLabelView.setVisibility(View.GONE);
            return;
        }
        if (dateMode == DateMode.YEAR_MONTH) {
            dayWheelView.setVisibility(View.GONE);
            dayLabelView.setVisibility(View.GONE);
        }
    }

    /**
     * 设置日期时间范围
     */
    public void setRange(@NonNull DateEntity startValue, @NonNull DateEntity endValue) {
        setRange(startValue, endValue, null);
    }

    /**
     * 设置日期时间范围
     */
    public void setRange(@NonNull DateEntity startValue, @NonNull DateEntity endValue,
                         @Nullable DateEntity defaultValue) {
        if (endValue.toTimeInMillis() < startValue.toTimeInMillis()) {
            throw new IllegalArgumentException("Ensure the start date is less than the end date");
        }
        this.startValue = startValue;
        this.endValue = endValue;
        if (defaultValue != null) {
            if (defaultValue.toTimeInMillis() < startValue.toTimeInMillis() ||
                    defaultValue.toTimeInMillis() > endValue.toTimeInMillis()) {
                throw new IllegalArgumentException("The default date is out of range");
            }
            selectedYear = defaultValue.getYear();
            selectedMonth = defaultValue.getMonth();
            selectedDay = defaultValue.getDay();
        }
        changeYear();
    }

    public void setDefaultValue(@NonNull final DateEntity defaultValue) {
        if (startValue == null) {
            startValue = DateEntity.today();
        }
        if (endValue == null) {
            endValue = DateEntity.yearOnFuture(30);
        }
        postDelayed(new Runnable() {
            @Override
            public void run() {
                setRange(startValue, endValue, defaultValue);
            }
        }, 200);

    }

    public void setDateFormatter(final DateFormatter dateFormatter) {
        if (dateFormatter == null) {
            return;
        }
        yearWheelView.setFormatter(new WheelFormatter() {
            @Override
            public String formatItem(@NonNull Object value) {
                return dateFormatter.formatYear((Integer) value);
            }
        });
        monthWheelView.setFormatter(new WheelFormatter() {
            @Override
            public String formatItem(@NonNull Object value) {
                return dateFormatter.formatMonth((Integer) value);
            }
        });
        dayWheelView.setFormatter(new WheelFormatter() {
            @Override
            public String formatItem(@NonNull Object value) {
                return dateFormatter.formatDay((Integer) value);
            }
        });
    }

    public void setDateLabel(CharSequence year, CharSequence month, CharSequence day) {
        yearLabelView.setText(year);
        monthLabelView.setText(month);
        dayLabelView.setText(day);
    }

    public void setOnDateSelectedListener(OnDateSelectedListener onDateSelectedListener) {
        this.onDateSelectedListener = onDateSelectedListener;
    }

    public final DateEntity getStartValue() {
        return startValue;
    }

    public final DateEntity getEndValue() {
        return endValue;
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

    public final TextView getYearLabelView() {
        return yearLabelView;
    }

    public final TextView getMonthLabelView() {
        return monthLabelView;
    }

    public final TextView getDayLabelView() {
        return dayLabelView;
    }

    public final int getSelectedYear() {
        return (int) yearWheelView.getCurrentItem();
    }

    public final int getSelectedMonth() {
        return (int) monthWheelView.getCurrentItem();
    }

    public final int getSelectedDay() {
        return (int) dayWheelView.getCurrentItem();
    }

    private void changeYear() {
        final int min = Math.min(startValue.getYear(), endValue.getYear());
        final int max = Math.max(startValue.getYear(), endValue.getYear());
        if (selectedYear == null) {
            selectedYear = min;
        }
        yearWheelView.setRange(min, max, 1);
        yearWheelView.setDefaultValue(selectedYear);
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
        monthWheelView.setRange(min, max, 1);
        monthWheelView.setDefaultValue(selectedMonth);
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
            max = getTotalDaysInMonth(year, month);
        }
        //结束年月相同情况
        else if (year == endValue.getYear() && month == endValue.getMonth()) {
            min = 1;
            max = endValue.getDay();
        } else {
            min = 1;
            max = getTotalDaysInMonth(year, month);
        }
        if (selectedDay == null) {
            selectedDay = min;
        }
        dayWheelView.setRange(min, max, 1);
        dayWheelView.setDefaultValue(selectedDay);
    }

    /**
     * 根据年份及月份获取每月的天数，类似于{@link java.util.Calendar#getActualMaximum(int)}
     */
    private int getTotalDaysInMonth(int year, int month) {
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                // 大月月份为31天
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                // 小月月份为30天
                return 30;
            case 2:
                // 二月需要判断是否闰年
                if (year <= 0) {
                    return 29;
                }
                // 是否闰年：能被4整除但不能被100整除；能被400整除；
                boolean isLeap = (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
                if (isLeap) {
                    return 29;
                } else {
                    return 28;
                }
            default:
                return 30;
        }
    }

}
