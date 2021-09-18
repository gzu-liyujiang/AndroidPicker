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

package com.github.gzuliyujiang.fallback.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.gzuliyujiang.calendarpicker.CalendarPicker;
import com.github.gzuliyujiang.calendarpicker.OnRangeDatePickListener;
import com.github.gzuliyujiang.calendarpicker.OnSingleDatePickListener;
import com.github.gzuliyujiang.calendarpicker.core.CalendarView;
import com.github.gzuliyujiang.calendarpicker.core.ColorScheme;
import com.github.gzuliyujiang.calendarpicker.core.DateUtils;
import com.github.gzuliyujiang.fallback.R;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日历日期选择器
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2019/6/23
 */
public class CalendarPickerActivity extends BackAbleActivity {
    private long startTimeInMillis, endTimeInMillis, singleTimeInMillis;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker_calendar);
        CalendarView calendarView = findViewById(R.id.calendar_picker_body);
        Date minDate = new Date(System.currentTimeMillis() - 5 * android.text.format.DateUtils.DAY_IN_MILLIS);
        Calendar calendar = DateUtils.calendar(minDate);
        calendar.add(Calendar.MONTH, 3);
        Date maxDate = calendar.getTime();
        calendarView.getAdapter()
                .notify(false)
                .single(false)
                .valid(minDate, maxDate)
                .intervalNotes("开始", "结束")
                .select(minDate.getTime(), minDate.getTime() + 5 * android.text.format.DateUtils.DAY_IN_MILLIS)
                .range(minDate, maxDate)
                .refresh();
    }

    public void onCalendarColorScheme(View view) {
        CalendarPicker picker = new CalendarPicker(this);
        Date currentDate = new Date(System.currentTimeMillis());
        if (startTimeInMillis == 0 && endTimeInMillis == 0) {
            startTimeInMillis = currentDate.getTime() - 3 * android.text.format.DateUtils.DAY_IN_MILLIS;
            endTimeInMillis = currentDate.getTime() + 3 * android.text.format.DateUtils.DAY_IN_MILLIS;
        }
        picker.setSelectedDate(startTimeInMillis, endTimeInMillis);
        picker.setColorScheme(new ColorScheme()
                .daySelectBackgroundColor(0xFF00CC00)
                .dayStressTextColor(0xFF00AA00));
        picker.setOnRangeDatePickListener(new OnRangeDatePickListener() {
            @Override
            public void onRangeDatePicked(@NonNull Date startDate, @NonNull Date endDate) {
                startTimeInMillis = startDate.getTime();
                endTimeInMillis = endDate.getTime();
                Toast.makeText(getApplicationContext(), DateFormat.getDateTimeInstance().format(startDate)
                        + "\n" + DateFormat.getDateTimeInstance().format(endDate), Toast.LENGTH_SHORT).show();
            }
        });
        picker.show();
    }

    public void onCalendarDateRange(View view) {
        CalendarPicker picker = new CalendarPicker(this);
        Date currentDate = new Date(System.currentTimeMillis());
        Calendar minCalendar = DateUtils.calendar(currentDate);
        minCalendar.add(Calendar.MONTH, -12);
        minCalendar.set(Calendar.DAY_OF_MONTH, DateUtils.maxDaysOfMonth(minCalendar.getTime()));
        Date minDate = minCalendar.getTime();
        Calendar maxCalendar = DateUtils.calendar(currentDate);
        maxCalendar.setTime(currentDate);
        maxCalendar.add(Calendar.MONTH, 12);
        maxCalendar.set(Calendar.DAY_OF_MONTH, DateUtils.maxDaysOfMonth(maxCalendar.getTime()));
        Date maxDate = maxCalendar.getTime();
        picker.setRangeDate(minDate, maxDate);
        if (startTimeInMillis == 0 && endTimeInMillis == 0) {
            startTimeInMillis = currentDate.getTime() - 3 * android.text.format.DateUtils.DAY_IN_MILLIS;
            endTimeInMillis = currentDate.getTime() + 3 * android.text.format.DateUtils.DAY_IN_MILLIS;
        }
        picker.setSelectedDate(startTimeInMillis, endTimeInMillis);
        picker.setOnRangeDatePickListener(new OnRangeDatePickListener() {
            @Override
            public void onRangeDatePicked(@NonNull Date startDate, @NonNull Date endDate) {
                startTimeInMillis = startDate.getTime();
                endTimeInMillis = endDate.getTime();
                Toast.makeText(getApplicationContext(), DateFormat.getDateTimeInstance().format(startDate)
                        + "\n" + DateFormat.getDateTimeInstance().format(endDate), Toast.LENGTH_SHORT).show();
            }
        });
        picker.show();
    }

    public void onCalendarDateSingle(View view) {
        CalendarPicker picker = new CalendarPicker(this);
        picker.setRangeDateOnFuture(3);
        if (singleTimeInMillis == 0) {
            singleTimeInMillis = System.currentTimeMillis();
        }
        picker.setSelectedDate(singleTimeInMillis);
        picker.setOnSingleDatePickListener(new OnSingleDatePickListener() {
            @Override
            public void onSingleDatePicked(@NonNull Date date) {
                singleTimeInMillis = date.getTime();
                Toast.makeText(getApplicationContext(), DateFormat.getDateTimeInstance().format(date), Toast.LENGTH_SHORT).show();
            }
        });
        picker.show();
    }

}

