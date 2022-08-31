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

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by peng on 2017/8/3.
 */
@SuppressWarnings("UnusedReturnValue")
public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.VH> implements OnDateClickListener {
    public static String DATE_FORMAT = "yyyy年MM月";
    private boolean notify = true;
    private ColorScheme colorScheme = new ColorScheme();
    private final List<Date> dates = new ArrayList<>();
    private final Interval<Date> valid = new Interval<>();
    private final Interval<Date> select = new Interval<>();
    private final Interval<String> selectNote = new Interval<>();
    private boolean singleMode = false;
    private FestivalProvider festivalProvider;
    private ItemViewProvider itemViewProvider;
    private Date lastClickDate = null;
    private OnDateSelectedListener onDateSelectedListener;

    static {
        if (!Locale.getDefault().getDisplayLanguage().contains("中文")) {
            DATE_FORMAT = "MMM, yyyy";
        }
    }

    public CalendarAdapter notify(boolean notify) {
        this.notify = notify;
        return this;
    }

    public CalendarAdapter colorScheme(ColorScheme colorScheme) {
        if (colorScheme == null) {
            colorScheme = new ColorScheme();
        }
        this.colorScheme = colorScheme;
        return this;
    }

    public CalendarAdapter single(boolean value) {
        singleMode = value;
        if (notify) {
            refresh();
        }
        return this;
    }

    public CalendarAdapter festivalProvider(FestivalProvider value) {
        festivalProvider = value;
        if (notify) {
            refresh();
        }
        return this;
    }

    public CalendarAdapter itemViewProvider(ItemViewProvider value) {
        itemViewProvider = value;
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
        List<Date> dates = DateUtils.fillDates(startDate, endDate);
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
        List<Date> dates = DateUtils.fillDates(startDate, endDate);
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

    public void setOnCalendarSelectedListener(OnDateSelectedListener onDateSelectedListener) {
        this.onDateSelectedListener = onDateSelectedListener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        TextView titleView = itemViewProvider == null ? null : itemViewProvider.provideTitleView(context);
        if (titleView == null) {
            titleView = new TextView(context);
            titleView.setGravity(Gravity.CENTER);
            titleView.setTextSize(14);
            titleView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            int padding = (int) (parent.getResources().getDisplayMetrics().density * 10);
            titleView.setPadding(padding, padding, padding, padding);
        }
        linearLayout.addView(titleView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        MonthView monthView = itemViewProvider == null ? null : itemViewProvider.provideMonthView(context);
        if (monthView == null) {
            monthView = new MonthView(context);
        }
        monthView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.addView(monthView);
        return new VH(linearLayout, titleView, monthView);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.titleView.setBackgroundColor(colorScheme.monthTitleBackgroundColor());
        holder.titleView.setTextColor(colorScheme.monthTitleTextColor());
        holder.titleView.setText(TimeUtils.dateText(getDateValue(position).getTime(), DATE_FORMAT));
        holder.monthView.setOnDayInMonthClickListener(this);
        holder.monthView.setValue(MonthEntity.obtain(valid, select)
                .date(dates.get(position))
                .singleMode(singleMode)
                .festivalProvider(festivalProvider)
                .note(selectNote), colorScheme);
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    public final int getDatePosition(Date date) {
        int size = dates.size();
        if (size <= 1) {
            return 0;
        }
        if (date == null) {
            return 0;
        }
        long time = date.getTime();
        if (time <= dates.get(0).getTime()) {
            return 0;
        }
        int lastPosition = size - 1;
        if (time >= dates.get(lastPosition).getTime()) {
            return lastPosition;
        }
        for (int i = 0; i <= lastPosition; i++) {
            Calendar minDate = DateUtils.calendar(dates.get(i).getTime());
            minDate.set(Calendar.DAY_OF_MONTH, 1);
            minDate.set(Calendar.HOUR_OF_DAY, 0);
            minDate.set(Calendar.MINUTE, 0);
            minDate.set(Calendar.SECOND, 0);
            minDate.set(Calendar.MILLISECOND, 0);
            Calendar maxDate = DateUtils.calendar(dates.get(i).getTime());
            maxDate.set(Calendar.DAY_OF_MONTH, DateUtils.maxDaysOfMonth(maxDate.getTime()));
            maxDate.set(Calendar.HOUR_OF_DAY, 23);
            maxDate.set(Calendar.MINUTE, 59);
            maxDate.set(Calendar.SECOND, 59);
            maxDate.set(Calendar.MILLISECOND, 1000);
            if (time >= minDate.getTime().getTime() && time <= maxDate.getTime().getTime()) {
                return i;
            }
        }
        return -1;
    }

    public Date getDateValue(int position) {
        if (position >= 0 && position < dates.size()) {
            return dates.get(position);
        }
        return new Date(0);
    }

    @Override
    public void onCalendarDayClick(Date date) {
        if (null == date) {
            return;
        }
        if (singleMode || null == lastClickDate || lastClickDate.getTime() >= date.getTime()) {
            lastClickDate = date;
            select(date, date).refresh();
            if (null != onDateSelectedListener) {
                onDateSelectedListener.onSingleSelected(date);
            }
            if (!singleMode && null != onDateSelectedListener) {
                onDateSelectedListener.onRangeSelected(date, date);
            }
            return;
        }
        select(lastClickDate, date).refresh();
        if (null != onDateSelectedListener) {
            onDateSelectedListener.onRangeSelected(lastClickDate, date);
        }
        lastClickDate = null;
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView titleView;
        MonthView monthView;

        VH(View itemView, TextView titleView, MonthView monthView) {
            super(itemView);
            this.titleView = titleView;
            this.monthView = monthView;
        }

    }

}