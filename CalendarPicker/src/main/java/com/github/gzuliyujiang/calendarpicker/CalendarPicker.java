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

import com.github.gzuliyujiang.basepicker.ConfirmPicker;
import com.github.gzuliyujiang.calendarpicker.calendar.adapter.CalendarAdapter;
import com.github.gzuliyujiang.calendarpicker.calendar.protocol.OnCalendarSelectListener;
import com.github.gzuliyujiang.calendarpicker.calendar.utils.DateUtils;
import com.github.gzuliyujiang.calendarpicker.calendar.view.CalendarView;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日历日期选择器
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2019/4/30 13:36
 */
@SuppressWarnings("unused")
public class CalendarPicker extends ConfirmPicker implements OnCalendarSelectListener {
    private CalendarView calendarView;
    private CalendarAdapter calendarAdapter;
    private boolean rangePick = true;
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
    protected View createBodyView(@NonNull Activity activity) {
        View view = View.inflate(activity, R.layout.calendar_picker, null);
        calendarView = view.findViewById(R.id.calendar_picker_body);
        return view;
    }

    @Override
    protected void initData() {
        super.initData();
        initialized = true;
        setHeight((int) (calendarView.getResources().getDisplayMetrics().heightPixels * 0.6));
        calendarAdapter = calendarView.getAdapter();
        calendarAdapter.setOnCalendarSelectListener(this);
        refreshData();
    }

    @Override
    protected void onCancel() {

    }

    @Override
    protected void onOk() {
        if (!rangePick && selectDate == null) {
            return;
        }
        boolean rangeNotSelected = startDate == null || endDate == null;
        if (rangePick && rangeNotSelected) {
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
    public void onSingleSelect(@NonNull Date date) {
        selectDate = date;
    }

    @Override
    public void onDoubleSelect(@NonNull Date before, @NonNull Date after) {
        startDate = before;
        endDate = after;
    }

    public void setOnRangeDatePickListener(OnRangeDatePickListener onRangeDatePickListener) {
        this.rangePick = true;
        this.onRangeDatePickListener = onRangeDatePickListener;
        if (initialized) {
            refreshData();
        }
    }

    public void setOnSingleDatePickListener(OnSingleDatePickListener onSingleDatePickListener) {
        this.rangePick = false;
        this.onSingleDatePickListener = onSingleDatePickListener;
        if (initialized) {
            refreshData();
        }
    }

    public void setRangeDate(Date minDate, Date maxDate) {
        this.minDate = DateUtils.min(minDate, maxDate);
        this.maxDate = DateUtils.max(minDate, maxDate);
        if (initialized) {
            refreshData();
        }
    }

    public void setRangeDateOnFuture(int offsetMonth) {
        minDate = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTime(minDate);
        calendar.add(Calendar.MONTH, offsetMonth);
        calendar.set(Calendar.DAY_OF_MONTH, DateUtils.maxDaysOfMonth(calendar.getTime()));
        maxDate = calendar.getTime();
        if (initialized) {
            refreshData();
        }
    }

    public void setSelectedDate(long timeInMillis) {
        this.selectDate = new Date(timeInMillis);
        if (initialized) {
            refreshData();
        }
    }

    public void setSelectedDate(long timeInMillisStart, long timeInMillisEnd) {
        this.startDate = new Date(Math.min(timeInMillisStart, timeInMillisEnd));
        this.endDate = new Date(Math.max(timeInMillisStart, timeInMillisEnd));
        if (initialized) {
            refreshData();
        }
    }

    public void setIntervalNotes(String noteFrom, String noteTo) {
        this.noteFrom = noteFrom;
        this.noteTo = noteTo;
        if (initialized) {
            refreshData();
        }
    }

    private void refreshData() {
        if (!TextUtils.isEmpty(noteFrom) && !TextUtils.isEmpty(noteTo)) {
            calendarAdapter.intervalNotes(noteFrom, noteTo);
        }
        calendarAdapter.single(!rangePick);
        if (!rangePick) {
            startDate = selectDate;
        }
        calendarAdapter.setRange(minDate, maxDate, true, false);
        calendarAdapter.valid(minDate, maxDate);
        calendarAdapter.select(startDate, endDate);
    }

}
