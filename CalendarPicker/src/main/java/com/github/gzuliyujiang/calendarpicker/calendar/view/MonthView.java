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

package com.github.gzuliyujiang.calendarpicker.calendar.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.github.gzuliyujiang.calendarpicker.R;
import com.github.gzuliyujiang.calendarpicker.calendar.annotation.Status;
import com.github.gzuliyujiang.calendarpicker.calendar.protocol.DayEntity;
import com.github.gzuliyujiang.calendarpicker.calendar.protocol.MonthEntity;
import com.github.gzuliyujiang.calendarpicker.calendar.protocol.NInterval;
import com.github.gzuliyujiang.calendarpicker.calendar.protocol.OnMonthClickListener;
import com.github.gzuliyujiang.calendarpicker.calendar.utils.DateUtils;

import java.util.Date;

/**
 * 月份控件
 * Created by peng on 2017/8/2.
 */
public class MonthView extends ViewGroup {
    private final DayView[] dayViews = new DayView[MonthEntity.MAX_DAYS_OF_MONTH];
    private final View[] lines = new View[MonthEntity.MAX_HORIZONTAL_LINES];
    private final int LINE_HEIGHT;
    private final SplitLinesLayoutControl lineControl;

    private MonthEntity monthEntity;
    private int isTodayOfMonth = -1;
    //location
    private int position = 0;
    private int offset = 0;
    //child width and height
    private int childWidth = 0;
    private int childHeight = 0;

    public MonthView(Context context) {
        this(context, null);
    }

    public MonthView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MonthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundColor(ContextCompat.getColor(context, R.color.calendar_month_background_color));
        //DayView
        for (int i = 0; i < dayViews.length; i++) {
            dayViews[i] = new DayView(context);
            addView(dayViews[i]);
        }
        //horizontal line
        LINE_HEIGHT = (int) getResources().getDimension(R.dimen.calendar_month_divide_line_height);
        for (int j = 0; j < lines.length; j++) {
            View view = new View(getContext());
            view.setBackgroundResource(R.color.calendar_month_divide_line_color);
            addView(view);
            lines[j] = view;
        }
        lineControl = new SplitLinesLayoutControl(lines);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (null == value()) {
            return;
        }
        int totalWidth = MeasureSpec.getSize(widthMeasureSpec);
        dayViews[0].measure(widthMeasureSpec, heightMeasureSpec);
        int childrenHeight = 0;
        //calc need rows
        int amount = position + offset;
        int dayRows = (amount / MonthEntity.WEEK_DAYS) + (((amount % MonthEntity.WEEK_DAYS) != 0) ? 1 : 0);
        //measure container
        childrenHeight += dayViews[0].getMeasuredHeight() * dayRows;
        childrenHeight += (dayRows) * LINE_HEIGHT;
        setMeasuredDimension(totalWidth, childrenHeight);
        //measure DayViews
        childWidth = totalWidth / MonthEntity.WEEK_DAYS;
        childHeight = dayViews[0].getMeasuredHeight();
        int childWidthSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
        int childHeightSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
        for (DayView dayView : dayViews) {
            dayView.measure(childWidthSpec, childHeightSpec);
        }
        //measure horizontal lines
        for (View line : lines) {
            line.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(LINE_HEIGHT, MeasureSpec.EXACTLY));
        }
    }

    //分割线布局控制器
    private static class SplitLinesLayoutControl {
        private final int width;
        private final int height;
        private final View[] view;
        private int count = 0;

        SplitLinesLayoutControl(@NonNull View[] views) {
            this.view = views;
            width = views[0].getMeasuredWidth();
            height = views[0].getMeasuredHeight();
        }

        public int layout(int offsetY) {
            if (count >= view.length) {
                return offsetY;
            }
            int bottom = offsetY + height;
            view[count].layout(0, offsetY, width, bottom);
            count += 1;
            return bottom;
        }
    }

    @Override
    protected void onLayout(boolean b, int left, int top, int right, int bottom) {
        if (null == value()) {
            return;
        }
        int offsetX = 0, offsetY = 0;
        for (int i = 0; i < position; i++) {
            offsetX += childWidth;
        }
        int childBottom = offsetY + childHeight;
        boolean lastIsRightBound = false;//上一个是否是右边界

        NInterval validRange = DateUtils.daysInterval(value().date(), monthEntity.valid());
        NInterval selectRange = null;
        if (monthEntity.select().bothNoNull()) {
            selectRange = DateUtils.daysInterval(value().date(), monthEntity.select());
        }
        for (int index = 0, move = position + 1; index < dayViews.length; index++, move++) {
            boolean rightBound = move % MonthEntity.WEEK_DAYS == 0;
            DayEntity dayEntity;
            if (index < offset) {
                //set state
                boolean isToday = index == isTodayOfMonth;
                dayEntity = DayEntity.obtain(Status.NORMAL, index, isToday ? MonthEntity.STR_TODAY : "")
                        .valueStatus((lastIsRightBound || rightBound) ? Status.STRESS : Status.NORMAL)
                        .descStatus(isToday ? Status.STRESS : Status.NORMAL);
                //valid
                if (validRange.contain(index)) {
                    if (null != selectRange && selectRange.contain(index)) {
                        if (index == selectRange.lBound()) {
                            if (monthEntity.singleFlag()) {
                                dayEntity.status(Status.BOUND_M).note(monthEntity.selectNote().left());
                            } else {
                                dayEntity.status(Status.BOUND_L).note(monthEntity.selectNote().left());
                            }
                        } else if (index == selectRange.rBound()) {
                            dayEntity.status(Status.BOUND_R).note(monthEntity.selectNote().right());
                        } else {
                            dayEntity.status(Status.RANGE);
                            dayEntity.valueStatus(Status.RANGE);
                            dayEntity.descStatus(Status.RANGE);
                        }
                    }
                } else {
                    //不响应选择事件
                    dayEntity.status(Status.INVALID).valueStatus(Status.INVALID).descStatus(Status.INVALID);
                }
                dayViews[index].setOnClickListener(clickDayListener);
            } else {
                dayEntity = DayEntity.obtain(Status.INVALID, -1, "");
                dayViews[index].setOnClickListener(null);
            }
            dayViews[index].value(dayEntity);
            dayViews[index].layout(offsetX, offsetY, offsetX + childWidth, childBottom);
            if (rightBound) {
                offsetX = 0;
                offsetY += childHeight;
                //draw horizontal line
                offsetY = lineControl.layout(offsetY);
                childBottom = offsetY + childHeight;
            } else {
                offsetX += childWidth;
            }
            lastIsRightBound = rightBound;
        }
        lineControl.layout(offsetY + childHeight);
    }

    public void value(@NonNull MonthEntity entity) {
        if (null != value()) {
            value().recycle();
        }
        this.monthEntity = entity;
        position = DateUtils.firstDayOfMonthIndex(entity.date());
        offset = DateUtils.maxDaysOfMonth(entity.date());
        isTodayOfMonth = DateUtils.isTodayOfMonth(entity.date());
        requestLayout();
    }

    public MonthEntity value() {
        return monthEntity;
    }

    private OnMonthClickListener onDayInMonthClickListener;

    public void setOnDayInMonthClickListener(OnMonthClickListener listener) {
        onDayInMonthClickListener = listener;
    }

    private final OnClickListener clickDayListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!(v instanceof DayView)) {
                return;
            }
            if (null == onDayInMonthClickListener) {
                return;
            }
            try {
                DayView dayView = (DayView) v;
                DayEntity entity = dayView.value();
                Date month = new Date(monthEntity.date().getTime());
                Date dayDate = DateUtils.specialDayInMonth(month, entity.intValue());
                onDayInMonthClickListener.onMonthClick(dayDate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}