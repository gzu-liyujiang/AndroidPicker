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
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.github.gzuliyujiang.calendarpicker.R;
import com.github.gzuliyujiang.calendarpicker.calendar.annotation.Status;
import com.github.gzuliyujiang.calendarpicker.calendar.protocol.DayEntity;

/**
 * 日期控件
 * Created by peng on 2017/8/2.
 */

public final class DayView extends LinearLayout {
    private TextView tvDesc;
    private TextView tvDay;
    private DayEntity entity;

    public DayView(Context context) {
        super(context);
        initialize(context);
    }

    public DayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public DayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    /**
     * 初始化
     *
     * @param context 上下文
     */
    private void initialize(Context context) {
        setOrientation(VERTICAL);
        inflate(context, R.layout.calendar_day_item, this);
        setBackgroundColor(Color.WHITE);
        tvDesc = findViewById(R.id.calendar_day_item_desc);
        tvDay = findViewById(R.id.calendar_day_item_day);
    }

    public void value(DayEntity entity) {
        if (null != value()) {
            value().recycle();
        }
        this.entity = entity;
        //内容
        tvDay.setText(entity.value());
        setTextStatusColor(tvDay, entity.valueStatus());
        //描述
        tvDesc.setText(entity.desc());
        setTextStatusColor(tvDesc, entity.descStatus());
        //背景
        setBackgroundStatus(entity);
    }

    public DayEntity value() {
        return entity;
    }

    /**
     * 设置文本状态颜色
     *
     * @param tv     文本控件
     * @param status 状态
     */
    private void setTextStatusColor(TextView tv, @Status int status) {
        switch (status) {
            //正常
            case Status.NORMAL:
                tv.setTextColor(ContextCompat.getColor(getContext(), R.color.calendar_day_text_normal_color));
                break;
            //不可用
            case Status.INVALID:
                tv.setTextColor(ContextCompat.getColor(getContext(), R.color.calendar_day_text_invalid_color));
                break;
            //范围内
            case Status.RANGE:
                tv.setTextColor(ContextCompat.getColor(getContext(), R.color.calendar_day_text_range_color));
                break;
            //左边界
            case Status.BOUND_L:
            case Status.BOUND_M:
            case Status.BOUND_R:
                tv.setTextColor(ContextCompat.getColor(getContext(), R.color.calendar_day_text_select_color));
                break;
            //强调
            case Status.STRESS:
                tv.setTextColor(ContextCompat.getColor(getContext(), R.color.calendar_day_text_stress_color));
                break;
        }
    }

    /**
     * 设置背景状态
     *
     * @param entity 实体
     */
    private void setBackgroundStatus(DayEntity entity) {
        switch (entity.status()) {
            //正常
            case Status.NORMAL:
                setBackgroundColor(ContextCompat.getColor(getContext(), R.color.calendar_day_background_normal_color));
                setEnabled(true);
                break;
            //不可用
            case Status.INVALID:
                setBackgroundColor(ContextCompat.getColor(getContext(), R.color.calendar_day_background_invalid_color));
                setTextStatusColor(tvDay, entity.status());
                setEnabled(false);
                break;
            //范围内
            case Status.RANGE:
                setBackgroundColor(ContextCompat.getColor(getContext(), R.color.calendar_day_background_range_color));
                setEnabled(true);
                break;
            //左边界
            case Status.BOUND_L:
                tvDay.setTextColor(ContextCompat.getColor(getContext(), R.color.calendar_day_text_select_color));
                tvDesc.setTextColor(ContextCompat.getColor(getContext(), R.color.calendar_day_text_select_color));
                tvDesc.setText(entity.note());
                setBackgroundResource(R.drawable.calendar_shape_day_range_left);
                break;
            //单选
            case Status.BOUND_M:
                tvDay.setTextColor(ContextCompat.getColor(getContext(), R.color.calendar_day_text_select_color));
                tvDesc.setTextColor(ContextCompat.getColor(getContext(), R.color.calendar_day_text_select_color));
                tvDesc.setText(entity.note());
                setBackgroundResource(R.drawable.calendar_shape_day_range_middle);
                break;
            //右边界
            case Status.BOUND_R:
                tvDay.setTextColor(ContextCompat.getColor(getContext(), R.color.calendar_day_text_select_color));
                tvDesc.setTextColor(ContextCompat.getColor(getContext(), R.color.calendar_day_text_select_color));
                tvDesc.setText(entity.note());
                setBackgroundResource(R.drawable.calendar_shape_day_range_right);
                break;
            //强调
            case Status.STRESS:
                setBackgroundColor(ContextCompat.getColor(getContext(), R.color.calendar_day_background_range_color));
                setEnabled(true);
                break;
        }
    }
}