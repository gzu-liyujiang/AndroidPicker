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

package com.github.gzuliyujiang.calendarpicker;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;

import com.github.gzuliyujiang.calendarpicker.core.CalendarAdapter;
import com.github.gzuliyujiang.calendarpicker.core.CalendarView;
import com.github.gzuliyujiang.calendarpicker.core.ColorScheme;
import com.github.gzuliyujiang.calendarpicker.core.DateUtils;
import com.github.gzuliyujiang.calendarpicker.core.FestivalProvider;
import com.github.gzuliyujiang.calendarpicker.core.OnDateSelectedListener;
import com.github.gzuliyujiang.dialog.DialogConfig;
import com.github.gzuliyujiang.dialog.DialogStyle;
import com.github.gzuliyujiang.dialog.ModalDialog;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日历日期选择器，基于`https://github.com/oxsource/calendar`
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2019/4/30 13:36
 */
@SuppressWarnings({"unused", "deprecation"})
public class CalendarPicker extends ModalDialog implements OnDateSelectedListener {
    private CalendarView calendarView;
    private CalendarAdapter calendarAdapter;
    private ColorScheme colorScheme;
    private boolean singleMode = false;
    private FestivalProvider festivalProvider;
    private Date minDate, maxDate;
    private Date selectDate, startDate, endDate;
    private String noteFrom, noteTo;
    private OnSingleDatePickListener onSingleDatePickListener;
    private OnRangeDatePickListener onRangeDatePickListener;
    private boolean initialized = false;

    public CalendarPicker(Activity activity) {
        super(activity);
    }

    public CalendarPicker(@NonNull Activity activity, @StyleRes int themeResId) {
        super(activity, themeResId);
    }

    @NonNull
    @Override
    protected View createBodyView() {
        calendarView = new CalendarView(activity);
        return calendarView;
    }

    @Override
    protected void initView() {
        super.initView();
        setHeight((int) (activity.getResources().getDisplayMetrics().heightPixels * 0.6f));
        switch (DialogConfig.getDialogStyle()) {
            case DialogStyle.Default:
            case DialogStyle.Two:
                headerView.setVisibility(View.VISIBLE);
                break;
            default:
                headerView.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void initData() {
        super.initData();
        initialized = true;
        if (minDate == null && maxDate == null) {
            Date currentDate = new Date(System.currentTimeMillis());
            Calendar minCalendar = DateUtils.calendar(currentDate);
            minCalendar.add(Calendar.MONTH, -12);
            minCalendar.set(Calendar.DAY_OF_MONTH, DateUtils.maxDaysOfMonth(minCalendar.getTime()));
            minDate = minCalendar.getTime();
            Calendar maxCalendar = DateUtils.calendar(currentDate);
            maxCalendar.setTime(currentDate);
            maxCalendar.add(Calendar.MONTH, 12);
            maxCalendar.set(Calendar.DAY_OF_MONTH, DateUtils.maxDaysOfMonth(maxCalendar.getTime()));
            maxDate = maxCalendar.getTime();
        }
        calendarAdapter = calendarView.getAdapter();
        calendarAdapter.setOnCalendarSelectedListener(this);
        refreshData();
    }

    @Override
    protected void onCancel() {

    }

    @Override
    protected void onOk() {
        if (singleMode && selectDate == null) {
            return;
        }
        boolean rangeNotSelected = startDate == null || endDate == null;
        if (!singleMode && rangeNotSelected) {
            return;
        }
        dismiss();
        if (onSingleDatePickListener != null) {
            onSingleDatePickListener.onSingleDatePicked(selectDate);
        }
        if (onRangeDatePickListener != null) {
            onRangeDatePickListener.onRangeDatePicked(startDate, endDate);
        }
    }

    @Override
    public void onSingleSelected(@NonNull Date date) {
        selectDate = date;
    }

    @Override
    public void onRangeSelected(@NonNull Date start, @NonNull Date end) {
        startDate = start;
        endDate = end;
    }

    /**
     * 设置配色方案
     */
    public void setColorScheme(ColorScheme colorScheme) {
        if (colorScheme == null) {
            colorScheme = new ColorScheme();
        }
        this.colorScheme = colorScheme;
        if (initialized) {
            refreshData();
        }
    }

    /**
     * 设置日期范围选择回调
     */
    public void setOnRangeDatePickListener(OnRangeDatePickListener onRangeDatePickListener) {
        this.singleMode = false;
        this.onRangeDatePickListener = onRangeDatePickListener;
        if (initialized) {
            refreshData();
        }
    }

    /**
     * 设置单个日期选择回调
     */
    public void setOnSingleDatePickListener(OnSingleDatePickListener onSingleDatePickListener) {
        this.singleMode = true;
        this.onSingleDatePickListener = onSingleDatePickListener;
        if (initialized) {
            refreshData();
        }
    }

    /**
     * 设置日期范围
     */
    public void setRangeDate(Date minDate, Date maxDate) {
        this.minDate = DateUtils.min(minDate, maxDate);
        this.maxDate = DateUtils.max(minDate, maxDate);
        if (initialized) {
            refreshData();
        }
    }

    /**
     * 设置日期范围为当前年月之后的几个月
     */
    public void setRangeDateOnFuture(int offsetMonth) {
        if (offsetMonth < 0) {
            offsetMonth = 0;
        }
        minDate = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MONTH, offsetMonth);
        calendar.set(Calendar.DAY_OF_MONTH, DateUtils.maxDaysOfMonth(calendar.getTime()));
        maxDate = calendar.getTime();
        if (initialized) {
            refreshData();
        }
    }

    /**
     * 设置默认选择的日期时间戳（单个日期选择模式）
     */
    public void setSelectedDate(long timeInMillis) {
        setSelectedDate(new Date(timeInMillis));
    }

    /**
     * 设置默认选择的日期（单个日期选择模式）
     */
    public void setSelectedDate(Date date) {
        this.selectDate = date;
        if (initialized) {
            refreshData();
        }
    }

    /**
     * 设置默认选择的日期时间戳（日期范围选择模式）
     */
    public void setSelectedDate(long timeInMillisStart, long timeInMillisEnd) {
        setSelectedDate(new Date(Math.min(timeInMillisStart, timeInMillisEnd)),
                new Date(Math.max(timeInMillisStart, timeInMillisEnd)));
    }

    /**
     * 设置默认选择的日期（日期范围选择模式）
     */
    public void setSelectedDate(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        if (initialized) {
            refreshData();
        }
    }

    /**
     * 设置选择区间提示语
     */
    public void setIntervalNotes(String noteFrom, String noteTo) {
        this.noteFrom = noteFrom;
        this.noteTo = noteTo;
        if (initialized) {
            refreshData();
        }
    }

    /**
     * 设置是否展示节日文本
     */
    public void setFestivalProvider(FestivalProvider festivalProvider) {
        this.festivalProvider = festivalProvider;
        if (initialized) {
            refreshData();
        }
    }

    private void refreshData() {
        calendarView.setColorScheme(colorScheme);
        calendarAdapter.notify(false);
        calendarAdapter.single(singleMode);
        calendarAdapter.festivalProvider(festivalProvider);
        if (singleMode) {
            startDate = selectDate;
            endDate = selectDate;
        }
        calendarAdapter.valid(minDate, maxDate);
        calendarAdapter.select(startDate, endDate);
        calendarAdapter.range(minDate, maxDate);
        if (!TextUtils.isEmpty(noteFrom) && !TextUtils.isEmpty(noteTo)) {
            calendarAdapter.intervalNotes(noteFrom, noteTo);
        }
        calendarAdapter.refresh();
        scrollToSelectedPosition();
    }

    private void scrollToSelectedPosition() {
        calendarView.post(new Runnable() {
            @Override
            public void run() {
                int position = calendarAdapter.getDatePosition(startDate);
                position = Math.max(position, 0);
                position = Math.min(position, calendarAdapter.getItemCount() - 1);
                calendarView.getLayoutManager().scrollToPositionWithOffset(position, 0);
            }
        });
    }

    public final CalendarView getCalendarView() {
        return calendarView;
    }

}
