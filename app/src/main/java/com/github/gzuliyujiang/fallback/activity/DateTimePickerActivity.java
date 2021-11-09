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

import androidx.annotation.Nullable;

import com.github.gzuliyujiang.fallback.R;
import com.github.gzuliyujiang.wheelpicker.BirthdayPicker;
import com.github.gzuliyujiang.wheelpicker.DatePicker;
import com.github.gzuliyujiang.wheelpicker.DatimePicker;
import com.github.gzuliyujiang.wheelpicker.TimePicker;
import com.github.gzuliyujiang.wheelpicker.annotation.DateMode;
import com.github.gzuliyujiang.wheelpicker.annotation.TimeMode;
import com.github.gzuliyujiang.wheelpicker.contract.OnDatePickedListener;
import com.github.gzuliyujiang.wheelpicker.contract.OnDatimePickedListener;
import com.github.gzuliyujiang.wheelpicker.contract.OnTimeMeridiemPickedListener;
import com.github.gzuliyujiang.wheelpicker.contract.OnTimePickedListener;
import com.github.gzuliyujiang.wheelpicker.entity.DateEntity;
import com.github.gzuliyujiang.wheelpicker.entity.DatimeEntity;
import com.github.gzuliyujiang.wheelpicker.entity.TimeEntity;
import com.github.gzuliyujiang.wheelpicker.impl.UnitDateFormatter;
import com.github.gzuliyujiang.wheelpicker.impl.UnitTimeFormatter;
import com.github.gzuliyujiang.wheelpicker.widget.DateWheelLayout;
import com.github.gzuliyujiang.wheelpicker.widget.DatimeWheelLayout;
import com.github.gzuliyujiang.wheelpicker.widget.TimeWheelLayout;

/**
 * 日期时间滚轮选择器
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2019/6/23
 */
public class DateTimePickerActivity extends BackAbleActivity implements OnDatePickedListener, OnTimePickedListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker_date_time);
    }

    @Override
    public void onDatePicked(int year, int month, int day) {
        Toast.makeText(this, year + "-" + month + "-" + day, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTimePicked(int hour, int minute, int second) {
        Toast.makeText(this, hour + ":" + minute + ":" + second, Toast.LENGTH_SHORT).show();
    }

    public void onYearMonthDayTime(View view) {
        DatimePicker picker = new DatimePicker(this);
        final DatimeWheelLayout wheelLayout = picker.getWheelLayout();
        picker.setOnDatimePickedListener(new OnDatimePickedListener() {
            @Override
            public void onDatimePicked(int year, int month, int day, int hour, int minute, int second) {
                String text = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                text += wheelLayout.getTimeWheelLayout().isAnteMeridiem() ? " 上午" : " 下午";
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }
        });
        wheelLayout.setDateMode(DateMode.YEAR_MONTH_DAY);
        wheelLayout.setTimeMode(TimeMode.HOUR_12_HAS_SECOND);
        wheelLayout.setRange(DatimeEntity.now(), DatimeEntity.yearOnFuture(10));
        wheelLayout.setDateLabel("年", "月", "日");
        wheelLayout.setTimeLabel("时", "分", "秒");
        picker.show();
    }

    public void onYearMonthDay(View view) {
        DatePicker picker = new DatePicker(this);
        //picker.setBodyWidth(240);
        DateWheelLayout wheelLayout = picker.getWheelLayout();
        wheelLayout.setDateMode(DateMode.YEAR_MONTH_DAY);
        //wheelLayout.setDateLabel("年", "月", "日");
        wheelLayout.setDateFormatter(new UnitDateFormatter());
        wheelLayout.setRange(DateEntity.target(2021, 1, 1), DateEntity.target(2050, 12, 31), DateEntity.today());
        wheelLayout.setCurtainEnabled(true);
        wheelLayout.setCurtainColor(0xFFCC0000);
        wheelLayout.setIndicatorEnabled(true);
        wheelLayout.setIndicatorColor(0xFFFF0000);
        wheelLayout.setIndicatorSize(view.getResources().getDisplayMetrics().density * 2);
        wheelLayout.setTextColor(0xCCCC0000);
        wheelLayout.setSelectedTextColor(0xFFFF0000);
        //wheelLayout.getYearLabelView().setTextColor(0xFF999999);
        //wheelLayout.getMonthLabelView().setTextColor(0xFF999999);
        picker.setOnDatePickedListener(this);
        picker.getWheelLayout().setResetWhenLinkage(false);
        picker.show();
    }

    public void onYearMonth(View view) {
        DatePicker picker = new DatePicker(this);
        picker.setBodyWidth(240);
        picker.getWheelLayout().setDateMode(DateMode.YEAR_MONTH);
        picker.getWheelLayout().setDateLabel("年", "月", "");
        picker.setOnDatePickedListener(this);
        picker.show();
    }

    public void onMonthDay(View view) {
        DatePicker picker = new DatePicker(this);
        picker.setBodyWidth(200);
        picker.getWheelLayout().setDateMode(DateMode.MONTH_DAY);
        picker.getWheelLayout().setDateFormatter(new UnitDateFormatter());
        picker.setOnDatePickedListener(this);
        picker.show();
    }

    public void onTime12(View view) {
        TimePicker picker = new TimePicker(this);
        picker.setBodyWidth(140);
        TimeWheelLayout wheelLayout = picker.getWheelLayout();
        wheelLayout.setRange(TimeEntity.target(1, 0, 0), TimeEntity.target(12, 59, 59));
        wheelLayout.setTimeMode(TimeMode.HOUR_12_NO_SECOND);
        wheelLayout.setTimeLabel(":", " ", "");
        wheelLayout.setDefaultValue(TimeEntity.now());
        wheelLayout.setTimeStep(1, 10, 1);
        picker.setOnTimeMeridiemPickedListener(new OnTimeMeridiemPickedListener() {
            @Override
            public void onTimePicked(int hour, int minute, int second, boolean isAnteMeridiem) {
                String text = hour + ":" + minute + ":" + second;
                text += isAnteMeridiem ? " 上午" : " 下午";
                Toast.makeText(getApplication(), text, Toast.LENGTH_SHORT).show();
            }
        });
        picker.show();
    }

    public void onTime24(View view) {
        TimePicker picker = new TimePicker(this);
        TimeWheelLayout wheelLayout = picker.getWheelLayout();
        wheelLayout.setTimeMode(TimeMode.HOUR_24_HAS_SECOND);
        wheelLayout.setTimeFormatter(new UnitTimeFormatter());
        wheelLayout.setDefaultValue(TimeEntity.now());
        wheelLayout.setResetWhenLinkage(false);
        picker.setOnTimePickedListener(this);
        picker.show();
    }

    public void onBirthday(View view) {
        BirthdayPicker picker = new BirthdayPicker(this);
        picker.setDefaultValue(1991, 11, 11);
        picker.setOnDatePickedListener(this);
        picker.getWheelLayout().setResetWhenLinkage(false);
        picker.show();
    }

}
