package com.oxandon.calendar.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import com.oxandon.calendar.R;
import com.oxandon.calendar.adapter.CalendarAdapter;
import com.oxandon.calendar.utils.TimeUtils;
import com.oxandon.calendar.view.decoration.GroupListener;
import com.oxandon.calendar.view.decoration.StickyDecoration;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 日历控件
 * Created by peng on 2017/8/3.
 */

public class CalendarView extends LinearLayout {
    private final CalendarAdapter calendarAdapter = new CalendarAdapter();
    private final RecyclerView bodyView;
    private final LinearLayoutManager mLayoutManager;

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
        //初始化星期标头
        initWeekGridView(context);
        //月份列表
        bodyView = findViewById(R.id.bodyView);
        mLayoutManager = new LinearLayoutManager(context);
        bodyView.setLayoutManager(mLayoutManager);
        bodyView.setAdapter(getAdapter());
        initDecoration(bodyView);
    }

    private void initWeekGridView(Context context) {
        String[] from = new String[]{"week"};
        int[] to = new int[]{R.id.tvWeekDay};
        String[] strings = new String[]{
                "日", "一", "二", "三", "四", "五", "六"
        };
        List<Map<String, String>> weeks = new ArrayList<>();
        for (String string : strings) {
            Map<String, String> map = new HashMap<>();
            map.put(from[0], string);
            weeks.add(map);
        }
        ListAdapter adapter = new SimpleAdapter(context, weeks, R.layout.calendar_week_item, from, to);
        GridView weekView = findViewById(R.id.weekView);
        weekView.setNumColumns(adapter.getCount());
        weekView.setAdapter(adapter);
        weekView.setSelector(new ColorDrawable(Color.TRANSPARENT));
    }

    public LinearLayoutManager getLayoutManager() {
        return mLayoutManager;
    }

    public void initDecoration(RecyclerView bodyView) {
        GroupListener groupListener = new GroupListener() {
            @Override
            public String getGroupName(int position) {
                Date date = getAdapter().value(position);
                return TimeUtils.dateText(date.getTime(), TimeUtils.YY_M_CN);
            }
        };
        StickyDecoration decoration = StickyDecoration.Builder
                .init(groupListener)
                .setGroupBackground(ContextCompat.getColor(getContext(), R.color
                        .calendar_background_decoration_color))     //背景色
                .setGroupHeight((int) getResources().getDimension(R.dimen.calendar_decoration_height))     //高度
                .setDivideColor(ContextCompat.getColor(getContext(), R.color.month_divide_line_color))           //分割线颜色
                .setDivideHeight((int) getResources().getDimension(R.dimen.calendar_decoration_divide_line_height))
                //分割线高度 (默认没有分割线)
                .setGroupTextColor(ContextCompat.getColor(getContext(), R.color.calendar_text_decoration_color))
                //字体颜色
                .setTypeface(Typeface.defaultFromStyle(Typeface.BOLD)) //加粗
                .setGroupTextSize((int) getResources().getDimension(R.dimen.calendar_decoration_text_size))   //字体大小
                .setTextSideMargin(dp2px(10))  //边距   靠左时为左边距  靠右时为右边距
                .setTextAlign(Paint.Align.CENTER)                      //居中显示
                .build();
        bodyView.addItemDecoration(decoration);
    }

    public CalendarAdapter getAdapter() {
        return calendarAdapter;
    }

    public RecyclerView bodyView() {
        return bodyView;
    }

    private int dp2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}