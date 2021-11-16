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
import com.github.gzuliyujiang.wheelpicker.annotation.TimeMode;
import com.github.gzuliyujiang.wheelpicker.contract.DateFormatter;
import com.github.gzuliyujiang.wheelpicker.contract.OnDatimeSelectedListener;
import com.github.gzuliyujiang.wheelpicker.contract.TimeFormatter;
import com.github.gzuliyujiang.wheelpicker.entity.DatimeEntity;
import com.github.gzuliyujiang.wheelpicker.impl.SimpleDateFormatter;
import com.github.gzuliyujiang.wheelpicker.impl.SimpleTimeFormatter;
import com.github.gzuliyujiang.wheelview.annotation.ScrollState;
import com.github.gzuliyujiang.wheelview.widget.NumberWheelView;
import com.github.gzuliyujiang.wheelview.widget.WheelView;

import java.util.ArrayList;
import java.util.List;

/**
 * 日期时间滚轮控件
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2019/5/14 15:26
 */
@SuppressWarnings("unused")
public class DatimeWheelLayout extends BaseWheelLayout {
    private DateWheelLayout dateWheelLayout;
    private TimeWheelLayout timeWheelLayout;
    private DatimeEntity startValue;
    private DatimeEntity endValue;
    private OnDatimeSelectedListener onDatimeSelectedListener;

    public DatimeWheelLayout(Context context) {
        super(context);
    }

    public DatimeWheelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DatimeWheelLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DatimeWheelLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected int provideLayoutRes() {
        return R.layout.wheel_picker_datime;
    }

    @Override
    protected List<WheelView> provideWheelViews() {
        List<WheelView> list = new ArrayList<>();
        list.addAll(dateWheelLayout.provideWheelViews());
        list.addAll(timeWheelLayout.provideWheelViews());
        return list;
    }

    @Override
    protected void onInit(@NonNull Context context) {
        dateWheelLayout = findViewById(R.id.wheel_picker_date_wheel);
        timeWheelLayout = findViewById(R.id.wheel_picker_time_wheel);
    }

    @Override
    protected void onAttributeSet(@NonNull Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DatimeWheelLayout);
        setDateMode(typedArray.getInt(R.styleable.DatimeWheelLayout_wheel_dateMode, DateMode.YEAR_MONTH_DAY));
        setTimeMode(typedArray.getInt(R.styleable.DatimeWheelLayout_wheel_timeMode, TimeMode.HOUR_24_NO_SECOND));
        String yearLabel = typedArray.getString(R.styleable.DatimeWheelLayout_wheel_yearLabel);
        String monthLabel = typedArray.getString(R.styleable.DatimeWheelLayout_wheel_monthLabel);
        String dayLabel = typedArray.getString(R.styleable.DatimeWheelLayout_wheel_dayLabel);
        setDateLabel(yearLabel, monthLabel, dayLabel);
        String hourLabel = typedArray.getString(R.styleable.DatimeWheelLayout_wheel_hourLabel);
        String minuteLabel = typedArray.getString(R.styleable.DatimeWheelLayout_wheel_minuteLabel);
        String secondLabel = typedArray.getString(R.styleable.DatimeWheelLayout_wheel_secondLabel);
        typedArray.recycle();
        setTimeLabel(hourLabel, minuteLabel, secondLabel);
        setDateFormatter(new SimpleDateFormatter());
        setTimeFormatter(new SimpleTimeFormatter(timeWheelLayout));
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE && startValue == null && endValue == null) {
            setRange(DatimeEntity.now(), DatimeEntity.yearOnFuture(30), DatimeEntity.now());
        }
    }

    @Override
    public void onWheelSelected(WheelView view, int position) {
        dateWheelLayout.onWheelSelected(view, position);
        timeWheelLayout.onWheelSelected(view, position);
        if (onDatimeSelectedListener == null) {
            return;
        }
        timeWheelLayout.post(new Runnable() {
            @Override
            public void run() {
                onDatimeSelectedListener.onDatimeSelected(dateWheelLayout.getSelectedYear(),
                        dateWheelLayout.getSelectedMonth(), dateWheelLayout.getSelectedDay(),
                        timeWheelLayout.getSelectedHour(), timeWheelLayout.getSelectedMinute(),
                        timeWheelLayout.getSelectedSecond());
            }
        });
    }

    @Override
    public void onWheelScrolled(WheelView view, int offset) {
        dateWheelLayout.onWheelScrolled(view, offset);
        timeWheelLayout.onWheelScrolled(view, offset);
    }

    @Override
    public void onWheelScrollStateChanged(WheelView view, @ScrollState int state) {
        dateWheelLayout.onWheelScrollStateChanged(view, state);
        timeWheelLayout.onWheelScrollStateChanged(view, state);
    }

    @Override
    public void onWheelLoopFinished(WheelView view) {
        dateWheelLayout.onWheelLoopFinished(view);
        timeWheelLayout.onWheelLoopFinished(view);
    }

    public void setDateMode(@DateMode int dateMode) {
        dateWheelLayout.setDateMode(dateMode);
    }

    public void setTimeMode(@TimeMode int timeMode) {
        timeWheelLayout.setTimeMode(timeMode);
    }

    /**
     * 设置日期时间范围
     */
    public void setRange(DatimeEntity startValue, DatimeEntity endValue) {
        setRange(startValue, endValue, null);
    }

    /**
     * 设置日期时间范围
     */
    public void setRange(DatimeEntity startValue, DatimeEntity endValue, DatimeEntity defaultValue) {
        if (startValue == null) {
            startValue = DatimeEntity.now();
        }
        if (endValue == null) {
            endValue = DatimeEntity.yearOnFuture(10);
        }
        if (defaultValue == null) {
            defaultValue = startValue;
        }
        dateWheelLayout.setRange(startValue.getDate(), endValue.getDate(), defaultValue.getDate());
        timeWheelLayout.setRange(null, null, defaultValue.getTime());
        this.startValue = startValue;
        this.endValue = endValue;
    }

    public void setDefaultValue(DatimeEntity defaultValue) {
        if (defaultValue == null) {
            defaultValue = DatimeEntity.now();
        }
        dateWheelLayout.setDefaultValue(defaultValue.getDate());
        timeWheelLayout.setDefaultValue(defaultValue.getTime());
    }

    public void setDateFormatter(DateFormatter dateFormatter) {
        dateWheelLayout.setDateFormatter(dateFormatter);
    }

    public void setTimeFormatter(TimeFormatter timeFormatter) {
        timeWheelLayout.setTimeFormatter(timeFormatter);
    }

    public void setDateLabel(CharSequence year, CharSequence month, CharSequence day) {
        dateWheelLayout.setDateLabel(year, month, day);
    }

    public void setTimeLabel(CharSequence hour, CharSequence minute, CharSequence second) {
        timeWheelLayout.setTimeLabel(hour, minute, second);
    }

    public void setOnDatimeSelectedListener(OnDatimeSelectedListener onDatimeSelectedListener) {
        this.onDatimeSelectedListener = onDatimeSelectedListener;
    }

    public void setResetWhenLinkage(boolean dateResetWhenLinkage, boolean timeResetWhenLinkage) {
        dateWheelLayout.setResetWhenLinkage(dateResetWhenLinkage);
        timeWheelLayout.setResetWhenLinkage(timeResetWhenLinkage);
    }

    public final DatimeEntity getStartValue() {
        return startValue;
    }

    public final DatimeEntity getEndValue() {
        return endValue;
    }

    public final DateWheelLayout getDateWheelLayout() {
        return dateWheelLayout;
    }

    public final TimeWheelLayout getTimeWheelLayout() {
        return timeWheelLayout;
    }

    public final NumberWheelView getYearWheelView() {
        return dateWheelLayout.getYearWheelView();
    }

    public final NumberWheelView getMonthWheelView() {
        return dateWheelLayout.getMonthWheelView();
    }

    public final NumberWheelView getDayWheelView() {
        return dateWheelLayout.getDayWheelView();
    }

    public final NumberWheelView getHourWheelView() {
        return timeWheelLayout.getHourWheelView();
    }

    public final NumberWheelView getMinuteWheelView() {
        return timeWheelLayout.getMinuteWheelView();
    }

    public final NumberWheelView getSecondWheelView() {
        return timeWheelLayout.getSecondWheelView();
    }

    public final WheelView getMeridiemWheelView() {
        return timeWheelLayout.getMeridiemWheelView();
    }

    public final TextView getYearLabelView() {
        return dateWheelLayout.getYearLabelView();
    }

    public final TextView getMonthLabelView() {
        return dateWheelLayout.getMonthLabelView();
    }

    public final TextView getDayLabelView() {
        return dateWheelLayout.getDayLabelView();
    }

    public final TextView getHourLabelView() {
        return timeWheelLayout.getHourLabelView();
    }

    public final TextView getMinuteLabelView() {
        return timeWheelLayout.getMinuteLabelView();
    }

    public final TextView getSecondLabelView() {
        return timeWheelLayout.getSecondLabelView();
    }

    public final int getSelectedYear() {
        return dateWheelLayout.getSelectedYear();
    }

    public final int getSelectedMonth() {
        return dateWheelLayout.getSelectedMonth();
    }

    public final int getSelectedDay() {
        return dateWheelLayout.getSelectedDay();
    }

    public final int getSelectedHour() {
        return timeWheelLayout.getSelectedHour();
    }

    public final int getSelectedMinute() {
        return timeWheelLayout.getSelectedMinute();
    }

    public final int getSelectedSecond() {
        return timeWheelLayout.getSelectedSecond();
    }

}
