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

package com.github.gzuliyujiang.calendarpicker.core;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.GridView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.github.gzuliyujiang.calendarpicker.R;

/**
 * 日历控件
 * Created by peng on 2017/8/3.
 */
@SuppressWarnings("unused")
public class CalendarView extends LinearLayout {
    private final CalendarAdapter calendarAdapter = new CalendarAdapter();
    private final WeekAdapter weekAdapter = new WeekAdapter();
    private final GridView weekView;
    private final RecyclerView bodyView;

    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        inflate(context, R.layout.calendar_body, this);
        weekView = findViewById(R.id.calendar_body_week);
        weekView.setNumColumns(weekAdapter.getCount());
        weekView.setAdapter(weekAdapter);
        weekView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        bodyView = findViewById(R.id.calendar_body_content);
        bodyView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        bodyView.setAdapter(calendarAdapter);
    }

    public void enablePagerSnap() {
        bodyView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        new PagerSnapHelper().attachToRecyclerView(bodyView);
    }

    public void setColorScheme(ColorScheme colorScheme) {
        weekAdapter.setColorScheme(colorScheme);
        calendarAdapter.colorScheme(colorScheme);
    }

    public final GridView getWeekView() {
        return weekView;
    }

    public final RecyclerView getBodyView() {
        return bodyView;
    }

    public final LinearLayoutManager getLayoutManager() {
        RecyclerView.LayoutManager layoutManager = bodyView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            return (LinearLayoutManager) layoutManager;
        }
        throw new IllegalArgumentException("Layout manager must instance of LinearLayoutManager");
    }

    public final CalendarAdapter getAdapter() {
        return calendarAdapter;
    }

}