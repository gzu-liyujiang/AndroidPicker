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
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.Date;

/**
 * 月份控件
 * Created by peng on 2017/8/2.
 */
public class MonthView extends ViewGroup {
    private ColorScheme colorScheme = new ColorScheme();
    private final DayView[] dayViews = new DayView[MonthEntity.MAX_DAYS_OF_MONTH];
    private final View[] dividerViews = new View[MonthEntity.MAX_HORIZONTAL_LINES];
    private int dividerHeight;
    private DividerLayoutControl dividerLayoutControl;
    private MonthEntity monthEntity;
    private int isTodayOfMonth = -1;
    //location
    private int position = 0;
    private int offset = 0;
    //child width and height
    private int childWidth = 0;
    private int childHeight = 0;
    private OnDateClickListener onDayInMonthClickListener;

    public MonthView(Context context) {
        super(context);
        initialize(context);
    }

    public MonthView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public MonthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MonthView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(context);
    }

    private void initialize(Context context) {
        for (int i = 0, m = dayViews.length; i < m; i++) {
            dayViews[i] = new DayView(context);
            addView(dayViews[i]);
        }
        dividerHeight = (int) (getResources().getDisplayMetrics().density * 0.5f);
        for (int j = 0, n = dividerViews.length; j < n; j++) {
            View view = new View(getContext());
            addView(view);
            dividerViews[j] = view;
        }
        dividerLayoutControl = new DividerLayoutControl(dividerViews);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (null == getValue()) {
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
        childrenHeight += (dayRows) * dividerHeight;
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
        for (View line : dividerViews) {
            line.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(dividerHeight, MeasureSpec.EXACTLY));
        }
    }

    @Override
    protected void onLayout(boolean b, int left, int top, int right, int bottom) {
        if (null == getValue()) {
            return;
        }
        int offsetX = 0, offsetY = 0;
        for (int i = 0; i < position; i++) {
            offsetX += childWidth;
        }
        int childBottom = offsetY + childHeight;
        boolean lastIsRightBound = false;//上一个是否是右边界
        NumInterval validRange = DateUtils.daysInterval(monthEntity.date(), monthEntity.valid());
        NumInterval selectRange = null;
        if (monthEntity.select().bothNoNull()) {
            selectRange = DateUtils.daysInterval(monthEntity.date(), monthEntity.select());
        }
        for (int index = 0, move = position + 1; index < dayViews.length; index++, move++) {
            boolean rightBound = move % MonthEntity.WEEK_DAYS == 0;
            DayEntity dayEntity;
            if (index < offset) {
                //set state
                boolean isToday = index == isTodayOfMonth;
                dayEntity = DayEntity.obtain(DayStatus.NORMAL, index, isToday ? MonthEntity.STR_TODAY : toDayDesc(index))
                        .valueStatus((lastIsRightBound || rightBound) ? DayStatus.STRESS : DayStatus.NORMAL)
                        .descStatus(isToday ? DayStatus.STRESS : DayStatus.NORMAL);
                //valid
                if (validRange.contain(index)) {
                    if (null != selectRange && selectRange.contain(index)) {
                        if (index == selectRange.lBound()) {
                            if (monthEntity.singleMode()) {
                                dayEntity.status(DayStatus.BOUND_M).note(monthEntity.note().left());
                            } else {
                                dayEntity.status(DayStatus.BOUND_L).note(monthEntity.note().left());
                            }
                        } else if (index == selectRange.rBound()) {
                            dayEntity.status(DayStatus.BOUND_R).note(monthEntity.note().right());
                        } else {
                            dayEntity.status(DayStatus.RANGE);
                            dayEntity.valueStatus(DayStatus.RANGE);
                            dayEntity.descStatus(DayStatus.RANGE);
                        }
                    }
                } else {
                    //不响应选择事件
                    dayEntity.status(DayStatus.INVALID)
                            .valueStatus(DayStatus.INVALID)
                            .descStatus(DayStatus.INVALID);
                }
                dayViews[index].setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!(v instanceof DayView)) {
                            return;
                        }
                        if (null == onDayInMonthClickListener) {
                            return;
                        }
                        try {
                            DayEntity entity = ((DayView) v).getValue();
                            Date dayDate = DateUtils.specialDayInMonth(monthEntity.date(), entity.intValue());
                            onDayInMonthClickListener.onCalendarDayClick(dayDate);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else {
                dayEntity = DayEntity.obtain(DayStatus.INVALID, -1, "");
                dayViews[index].setOnClickListener(null);
            }
            dayViews[index].setValue(dayEntity, colorScheme);
            dayViews[index].layout(offsetX, offsetY, offsetX + childWidth, childBottom);
            if (rightBound) {
                offsetX = 0;
                offsetY += childHeight;
                //draw horizontal line
                offsetY = dividerLayoutControl.layout(offsetY);
                childBottom = offsetY + childHeight;
            } else {
                offsetX += childWidth;
            }
            lastIsRightBound = rightBound;
        }
        dividerLayoutControl.layout(offsetY + childHeight);
    }

    @NonNull
    protected String toDayDesc(int index) {
        FestivalProvider festivalProvider = monthEntity.festivalProvider();
        if (festivalProvider == null) {
            return "";
        }
        Date date = DateUtils.specialDayInMonth(monthEntity.date(), index);
        String festival = festivalProvider.provideText(date);
        if (festival == null) {
            festival = "";
        }
        return festival;
    }

    public void setValue(@NonNull MonthEntity entity, @NonNull ColorScheme colorScheme) {
        if (null != monthEntity) {
            monthEntity.recycle();
        }
        this.monthEntity = entity;
        position = DateUtils.firstDayOfMonthIndex(entity.date());
        offset = DateUtils.maxDaysOfMonth(entity.date());
        isTodayOfMonth = DateUtils.isTodayOfMonth(entity.date());
        setBackgroundColor(colorScheme.monthBackgroundColor());
        for (View view : dividerViews) {
            view.setBackgroundColor(colorScheme.monthDividerColor());
        }
        this.colorScheme = colorScheme;
        requestLayout();
    }

    public MonthEntity getValue() {
        return monthEntity;
    }

    public void setOnDayInMonthClickListener(OnDateClickListener listener) {
        onDayInMonthClickListener = listener;
    }

    /**
     * 分割线布局控制器
     */
    private static class DividerLayoutControl {
        private final int width;
        private final int height;
        private final View[] view;
        private int count = 0;

        DividerLayoutControl(@NonNull View[] views) {
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

}