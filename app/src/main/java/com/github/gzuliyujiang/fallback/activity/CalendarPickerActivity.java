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
import androidx.fragment.app.FragmentActivity;

import com.github.gzuliyujiang.calendarpicker.CalendarPicker;
import com.github.gzuliyujiang.calendarpicker.OnRangeDatePickListener;
import com.github.gzuliyujiang.calendarpicker.OnSingleDatePickListener;
import com.github.gzuliyujiang.calendarpicker.calendar.adapter.CalendarAdapter;
import com.github.gzuliyujiang.calendarpicker.calendar.utils.DateUtils;
import com.github.gzuliyujiang.calendarpicker.calendar.view.CalendarView;
import com.github.gzuliyujiang.fallback.R;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日历日期选择器
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2019/6/23
 */
public class CalendarPickerActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker_calendar);
        CalendarView calendarView = findViewById(R.id.calendar_picker_body);
        CalendarAdapter calendarAdapter = calendarView.getAdapter();
        calendarAdapter.single(false);
        Date minDate = new Date(System.currentTimeMillis());
        Calendar calendar = DateUtils.calendar(minDate);
        calendar.add(Calendar.MONTH, 3);
        Date maxDate = calendar.getTime();
        calendarAdapter.setRange(minDate, maxDate, true, false);
        calendarAdapter.valid(minDate, maxDate);
        calendarAdapter.select(minDate.getTime(), minDate.getTime() + 5 * android.text.format.DateUtils.DAY_IN_MILLIS);
    }

    public void onCalendarDateRange(View view) {
        CalendarPicker picker = new CalendarPicker(this, R.style.SheetDialog);
        Date currentDate = new Date(System.currentTimeMillis());
        Calendar calendar1 = Calendar.getInstance(Locale.CHINA);
        calendar1.setTime(currentDate);
        calendar1.add(Calendar.MONTH, -12);
        calendar1.set(Calendar.DAY_OF_MONTH, DateUtils.maxDaysOfMonth(calendar1.getTime()));
        Date minDate = calendar1.getTime();
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTime(currentDate);
        calendar.add(Calendar.MONTH, 12);
        calendar.set(Calendar.DAY_OF_MONTH, DateUtils.maxDaysOfMonth(calendar.getTime()));
        Date maxDate = calendar.getTime();
        picker.setRangeDate(minDate, maxDate);
        long startTimeInMillis = currentDate.getTime() - 3 * android.text.format.DateUtils.DAY_IN_MILLIS;
        long endTimeInMillis = currentDate.getTime() + 3 * android.text.format.DateUtils.DAY_IN_MILLIS;
        picker.setSelectedDate(startTimeInMillis, endTimeInMillis);
        picker.setOnRangeDatePickListener(new OnRangeDatePickListener() {
            @Override
            public void onRangeDatePicked(@NonNull Date startDate, @NonNull Date endDate) {
                Toast.makeText(getApplicationContext(), startDate + "\n" + endDate, Toast.LENGTH_SHORT).show();
            }
        });
        picker.show();
    }

    public void onCalendarDateSingle(View view) {
        CalendarPicker picker = new CalendarPicker(this, R.style.SheetDialog);
        picker.setRangeDateOnFuture(3);
        picker.setSelectedDate(System.currentTimeMillis());
        picker.setOnSingleDatePickListener(new OnSingleDatePickListener() {
            @Override
            public void onSingleDatePicked(@NonNull Date date) {
                Toast.makeText(getApplicationContext(), date.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        picker.show();
    }

}

