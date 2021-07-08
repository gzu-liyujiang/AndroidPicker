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
import com.github.gzuliyujiang.wheelpicker.entity.TimeEntity;
import com.github.gzuliyujiang.wheelpicker.impl.SimpleDateFormatter;
import com.github.gzuliyujiang.wheelpicker.impl.SimpleTimeFormatter;
import com.github.gzuliyujiang.wheelview.annotation.ItemTextAlign;
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
    private int timeMode = TimeMode.HOUR_24_HAS_SECOND;
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
    protected int[] provideStyleableRes() {
        return R.styleable.DatimeWheelLayout;
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
        setDateFormatter(new SimpleDateFormatter());
        setTimeFormatter(new SimpleTimeFormatter());
        setRange(DatimeEntity.now(), DatimeEntity.yearOnFuture(30));
    }

    @Override
    protected void onAttributeSet(@NonNull Context context, @NonNull TypedArray typedArray) {
        float density = context.getResources().getDisplayMetrics().density;
        setTextSize(typedArray.getDimensionPixelSize(R.styleable.DatimeWheelLayout_wheel_itemTextSize,
                (int) (15 * context.getResources().getDisplayMetrics().scaledDensity)));
        setVisibleItemCount(typedArray.getInt(R.styleable.DatimeWheelLayout_wheel_visibleItemCount, 5));
        setSameWidthEnabled(typedArray.getBoolean(R.styleable.DatimeWheelLayout_wheel_sameWidthEnabled, false));
        setMaxWidthText(typedArray.getString(R.styleable.DatimeWheelLayout_wheel_maxWidthText));
        setSelectedTextColor(typedArray.getColor(R.styleable.DatimeWheelLayout_wheel_itemTextColorSelected, 0xFF000000));
        setTextColor(typedArray.getColor(R.styleable.DatimeWheelLayout_wheel_itemTextColor, 0xFF888888));
        setItemSpace(typedArray.getDimensionPixelSize(R.styleable.DatimeWheelLayout_wheel_itemSpace,
                (int) (20 * density)));
        setCyclicEnabled(typedArray.getBoolean(R.styleable.DatimeWheelLayout_wheel_cyclicEnabled, false));
        setIndicatorEnabled(typedArray.getBoolean(R.styleable.DatimeWheelLayout_wheel_indicatorEnabled, false));
        setIndicatorColor(typedArray.getColor(R.styleable.DatimeWheelLayout_wheel_indicatorColor, 0xFFEE3333));
        setIndicatorSize(typedArray.getDimension(R.styleable.DatimeWheelLayout_wheel_indicatorSize, 1 * density));
        setCurvedIndicatorSpace(typedArray.getDimensionPixelSize(R.styleable.DatimeWheelLayout_wheel_curvedIndicatorSpace, (int) (1 * density)));
        setCurtainEnabled(typedArray.getBoolean(R.styleable.DatimeWheelLayout_wheel_curtainEnabled, false));
        setCurtainColor(typedArray.getColor(R.styleable.DatimeWheelLayout_wheel_curtainColor, 0x88FFFFFF));
        setAtmosphericEnabled(typedArray.getBoolean(R.styleable.DatimeWheelLayout_wheel_atmosphericEnabled, false));
        setCurvedEnabled(typedArray.getBoolean(R.styleable.DatimeWheelLayout_wheel_curvedEnabled, false));
        setCurvedMaxAngle(typedArray.getInteger(R.styleable.DatimeWheelLayout_wheel_curvedMaxAngle, 90));
        setTextAlign(typedArray.getInt(R.styleable.DatimeWheelLayout_wheel_itemTextAlign, ItemTextAlign.CENTER));
        setDateMode(typedArray.getInt(R.styleable.DatimeWheelLayout_wheel_dateMode, DateMode.YEAR_MONTH_DAY));
        setTimeMode(typedArray.getInt(R.styleable.DatimeWheelLayout_wheel_timeMode, TimeMode.HOUR_24_NO_SECOND));
        String yearLabel = typedArray.getString(R.styleable.DatimeWheelLayout_wheel_yearLabel);
        String monthLabel = typedArray.getString(R.styleable.DatimeWheelLayout_wheel_monthLabel);
        String dayLabel = typedArray.getString(R.styleable.DatimeWheelLayout_wheel_dayLabel);
        setDateLabel(yearLabel, monthLabel, dayLabel);
        String hourLabel = typedArray.getString(R.styleable.DatimeWheelLayout_wheel_hourLabel);
        String minuteLabel = typedArray.getString(R.styleable.DatimeWheelLayout_wheel_minuteLabel);
        String secondLabel = typedArray.getString(R.styleable.DatimeWheelLayout_wheel_secondLabel);
        setTimeLabel(hourLabel, minuteLabel, secondLabel);
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

    public void setDateMode(int dateMode) {
        dateWheelLayout.setDateMode(dateMode);
    }

    public void setTimeMode(int timeMode) {
        timeWheelLayout.setTimeMode(timeMode);
        this.timeMode = timeMode;
    }

    /**
     * 设置日期时间范围
     */
    public void setRange(@NonNull DatimeEntity startValue, @NonNull DatimeEntity endValue) {
        setRange(startValue, endValue, null);
    }

    /**
     * 设置日期时间范围
     */
    public void setRange(@NonNull DatimeEntity startValue, @NonNull DatimeEntity endValue,
                         @Nullable DatimeEntity defaultValue) {
        if (defaultValue == null) {
            defaultValue = startValue;
        }
        dateWheelLayout.setRange(startValue.getDate(), endValue.getDate(), defaultValue.getDate());
        TimeEntity startTime = TimeEntity.target(0, 0, 0);
        int timeHourMax;
        if (timeMode == TimeMode.HOUR_12_NO_SECOND || timeMode == TimeMode.HOUR_12_HAS_SECOND) {
            timeHourMax = 12;
        } else {
            timeHourMax = 23;
        }
        TimeEntity endTime = TimeEntity.target(timeHourMax, 59, 59);
        timeWheelLayout.setRange(startTime, endTime, defaultValue.getTime());
        this.startValue = startValue;
        this.endValue = endValue;
    }

    public void setDefaultValue(@NonNull final DatimeEntity defaultValue) {
        if (startValue == null) {
            startValue = DatimeEntity.now();
        }
        if (endValue == null) {
            endValue = DatimeEntity.yearOnFuture(30);
        }
        postDelayed(new Runnable() {
            @Override
            public void run() {
                setRange(startValue, endValue, defaultValue);
            }
        }, 200);
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

    public final DatimeEntity getStartValue() {
        return startValue;
    }

    public final DatimeEntity getEndValue() {
        return endValue;
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
