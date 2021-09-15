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

package com.github.gzuliyujiang.calendarpicker.calendar.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.gzuliyujiang.calendarpicker.calendar.protocol.Interval;
import com.github.gzuliyujiang.calendarpicker.calendar.protocol.MonthEntity;
import com.github.gzuliyujiang.calendarpicker.calendar.protocol.OnCalendarSelectListener;
import com.github.gzuliyujiang.calendarpicker.calendar.protocol.OnMonthClickListener;
import com.github.gzuliyujiang.calendarpicker.calendar.utils.DateUtils;
import com.github.gzuliyujiang.calendarpicker.calendar.view.MonthView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 日历适配器
 * Created by peng on 2017/8/3.
 */
public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> implements OnMonthClickListener {
    private final String TAG = CalendarAdapter.class.getSimpleName();
    private final List<Date> dates = new ArrayList<>();
    private final Interval<Date> valid = new Interval<>();
    private final Interval<Date> select = new Interval<>();
    private final Interval<String> selectNote = new Interval<>();
    private boolean singleFlag = false;

    /**
     * 设置选择日期范围
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     */
    public void setRange(Date startDate, Date endDate, boolean clean, boolean notify) {
        List<Date> dates = DateUtils.fillMonths(startDate, endDate);
        setRange(dates, clean, notify);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setRange(List<Date> list, boolean clean, boolean notify) {
        if (clean) {
            dates.clear();
        }
        if (null != list && list.size() > 0) {
            dates.addAll(list);
        }
        if (notify) {
            notifyDataSetChanged();
        }
    }

    public void single(boolean value) {
        singleFlag = value;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void valid(Date from, Date to) {
        valid.left(from);
        valid.right(to);
        notifyDataSetChanged();
    }

    /**
     * 选择区间提示语
     *
     * @param noteFrom 开始日期提示语
     * @param noteTo   结束日期提示语
     */
    public void intervalNotes(String noteFrom, String noteTo) {
        selectNote.left(noteFrom);
        selectNote.right(noteTo);
    }

    /**
     * 设置选择范围
     *
     * @param fromInMillis 开始日期
     * @param toInMillis   结束日期
     */
    public void select(long fromInMillis, long toInMillis) {
        select(new Date(fromInMillis), new Date(toInMillis));
    }

    /**
     * 设置选择范围
     *
     * @param from 开始日期
     * @param to   结束日期
     */
    @SuppressLint("NotifyDataSetChanged")
    public void select(Date from, Date to) {
        select.left(from);
        select.right(to);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MonthView view = new MonthView(parent.getContext());
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width, height);
        view.setLayoutParams(params);
        view.setOnDayInMonthClickListener(CalendarAdapter.this);
        return new CalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        MonthEntity entity = MonthEntity.obtain(valid, select)
                .date(dates.get(position))
                .singleFlag(singleFlag)
                .selectNote(selectNote);
        holder.view().value(entity);
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    public final int getDatePosition(Date date) {
        int position = -1;
        if (dates.size() > 1) {
            if (date.getTime() <= dates.get(0).getTime()) {
                position = 0;
            } else if (date.getTime() >= dates.get(dates.size() - 1).getTime()) {
                position = dates.size() - 1;
            } else {
                for (int i = 0; i < dates.size() - 1; i++) {
                    if (date.getTime() >= dates.get(i).getTime() && date.getTime() <= dates.get(i + 1).getTime()) {
                        position = i;
                        break;
                    }
                }
            }
        }
        return position;
    }

    public Date value(int position) {
        if (position >= 0 && position < dates.size()) {
            return dates.get(position);
        }
        return new Date(0);
    }


    private Date lastClickDate = null;
    private OnCalendarSelectListener calendarSelectListener;

    public void setOnCalendarSelectListener(OnCalendarSelectListener listener) {
        calendarSelectListener = listener;
    }

    @Override
    public void onMonthClick(Date date) {
        if (null == calendarSelectListener) {
            return;
        }
        if (null == date) {
            Log.d(TAG, "onDayInMonthClick error,receive null date");
            return;
        }
        if (null == lastClickDate || singleFlag) {
            lastClickDate = date;
            select(date, date);
            calendarSelectListener.onSingleSelect(date);
            return;
        }
        if (lastClickDate.getTime() >= date.getTime()) {
            lastClickDate = date;
            select(date, date);
            calendarSelectListener.onSingleSelect(date);
        } else {
            select(lastClickDate, date);
            calendarSelectListener.onDoubleSelect(lastClickDate, date);
            Log.d(TAG, "onDayInMonthClick:" + lastClickDate.getTime() + "," + date.getTime());
            lastClickDate = null;
        }
    }
}