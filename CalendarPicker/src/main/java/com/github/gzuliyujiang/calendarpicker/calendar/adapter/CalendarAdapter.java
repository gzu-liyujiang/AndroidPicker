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
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.gzuliyujiang.calendarpicker.calendar.protocol.Interval;
import com.github.gzuliyujiang.calendarpicker.calendar.protocol.MonthEntity;
import com.github.gzuliyujiang.calendarpicker.calendar.protocol.OnCalendarSelectListener;
import com.github.gzuliyujiang.calendarpicker.calendar.protocol.OnCalendarSelectedListener;
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
@SuppressWarnings("UnusedReturnValue")
public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> implements OnMonthClickListener {
    private boolean notify = true;
    private final List<Date> dates = new ArrayList<>();
    private final Interval<Date> valid = new Interval<>();
    private final Interval<Date> select = new Interval<>();
    private final Interval<String> selectNote = new Interval<>();
    private boolean singleMode = false;
    private Date lastClickDate = null;
    @SuppressWarnings("deprecation")
    private OnCalendarSelectListener calendarSelectListener;
    private OnCalendarSelectedListener onCalendarSelectedListener;

    public CalendarAdapter notify(boolean notify) {
        this.notify = notify;
        return this;
    }

    public CalendarAdapter single(boolean value) {
        singleMode = value;
        if (notify) {
            refresh();
        }
        return this;
    }

    public CalendarAdapter valid(Date from, Date to) {
        valid.left(from);
        valid.right(to);
        if (notify) {
            refresh();
        }
        return this;
    }

    /**
     * 选择区间提示语
     *
     * @param noteFrom 开始日期提示语
     * @param noteTo   结束日期提示语
     */
    public CalendarAdapter intervalNotes(String noteFrom, String noteTo) {
        selectNote.left(noteFrom);
        selectNote.right(noteTo);
        if (notify) {
            refresh();
        }
        return this;
    }

    /**
     * 设置选择范围
     *
     * @param fromInMillis 开始日期
     * @param toInMillis   结束日期
     */
    public CalendarAdapter select(long fromInMillis, long toInMillis) {
        return select(new Date(fromInMillis), new Date(toInMillis));
    }

    /**
     * 设置选择范围
     *
     * @param from 开始日期
     * @param to   结束日期
     */
    public CalendarAdapter select(Date from, Date to) {
        select.left(from);
        select.right(to);
        if (notify) {
            refresh();
        }
        return this;
    }

    /**
     * 设置选择日期范围
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     */
    public CalendarAdapter range(Date startDate, Date endDate) {
        return range(startDate, endDate, true);
    }

    public CalendarAdapter range(Date startDate, Date endDate, boolean clear) {
        List<Date> dates = DateUtils.fillMonths(startDate, endDate);
        return range(dates, clear);
    }

    public CalendarAdapter range(List<Date> list, boolean clear) {
        if (clear) {
            dates.clear();
        }
        if (null != list && list.size() > 0) {
            dates.addAll(list);
        }
        if (notify) {
            refresh();
        }
        return this;
    }

    /**
     * @deprecated 使用 {@link #notify(boolean)} 及 {@link #range(Date, Date, boolean)} 代替
     */
    @Deprecated
    public CalendarAdapter setRange(Date startDate, Date endDate, boolean clear, boolean notify) {
        List<Date> dates = DateUtils.fillMonths(startDate, endDate);
        this.notify = notify;
        return range(dates, clear);
    }

    /**
     * @deprecated 使用 {@link #notify(boolean)} 及 {@link #range(List, boolean)} 代替
     */
    @Deprecated
    public CalendarAdapter setRange(List<Date> list, boolean clear, boolean notify) {
        this.notify = notify;
        return range(list, clear);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refresh() {
        notifyDataSetChanged();
    }

    public void setOnCalendarSelectedListener(OnCalendarSelectedListener onCalendarSelectedListener) {
        this.onCalendarSelectedListener = onCalendarSelectedListener;
    }

    /**
     * @deprecated 使用 {@link #setOnCalendarSelectedListener(OnCalendarSelectedListener)} 代替
     */
    @Deprecated
    @SuppressWarnings("deprecation")
    public void setOnCalendarSelectListener(OnCalendarSelectListener listener) {
        calendarSelectListener = listener;
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
                .singleMode(singleMode)
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

    @Override
    public void onMonthClick(Date date) {
        if (null == date) {
            return;
        }
        if (null == lastClickDate || singleMode) {
            lastClickDate = date;
            select(date, date).refresh();
            if (null != calendarSelectListener) {
                calendarSelectListener.onSingleSelect(date);
            }
            if (null != onCalendarSelectedListener) {
                onCalendarSelectedListener.onSingleSelected(date);
            }
            return;
        }
        if (lastClickDate.getTime() >= date.getTime()) {
            lastClickDate = date;
            select(date, date).refresh();
            if (null != calendarSelectListener) {
                calendarSelectListener.onSingleSelect(date);
            }
            if (null != onCalendarSelectedListener) {
                onCalendarSelectedListener.onSingleSelected(date);
            }
        } else {
            select(lastClickDate, date).refresh();
            if (null != calendarSelectListener) {
                calendarSelectListener.onDoubleSelect(lastClickDate, date);
            }
            if (null != onCalendarSelectedListener) {
                onCalendarSelectedListener.onRangeSelected(lastClickDate, date);
            }
            lastClickDate = null;
        }
    }
}