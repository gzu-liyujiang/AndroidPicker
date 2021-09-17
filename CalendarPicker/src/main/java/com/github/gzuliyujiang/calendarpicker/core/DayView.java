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
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.graphics.ColorUtils;

/**
 * 日期控件
 * Created by peng on 2017/8/2.
 */
public final class DayView extends LinearLayout {
    private TextView tvDesc;
    private TextView tvDay;
    private DayEntity entity;

    public DayView(@NonNull Context context) {
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DayView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(context);
    }

    private void initialize(Context context) {
        setOrientation(VERTICAL);
        setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        int padding = (int) (getResources().getDisplayMetrics().density * 3);
        setPadding(0, padding, 0, padding);
        tvDay = new TextView(context);
        tvDay.setGravity(Gravity.CENTER);
        tvDay.setTextSize(15);
        addView(tvDay, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        tvDesc = new TextView(context);
        tvDesc.setGravity(Gravity.CENTER);
        tvDesc.setTextSize(12);
        addView(tvDesc, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void setValue(DayEntity entity, ColorScheme scheme) {
        if (null != getValue()) {
            getValue().recycle();
        }
        this.entity = entity;
        //内容
        tvDay.setText(entity.value());
        setTextStatusColor(tvDay, entity.valueStatus(), scheme);
        //描述
        tvDesc.setText(entity.desc());
        setTextStatusColor(tvDesc, entity.descStatus(), scheme);
        //背景
        setBackgroundStatus(entity, scheme);
    }

    public DayEntity getValue() {
        return entity;
    }

    private void setTextStatusColor(TextView tv, @DayStatus int status, ColorScheme scheme) {
        switch (status) {
            //正常
            case DayStatus.NORMAL:
                tv.setTextColor(scheme.dayNormalTextColor());
                break;
            //不可用
            case DayStatus.INVALID:
                tv.setTextColor(scheme.dayInvalidTextColor());
                break;
            //强调
            case DayStatus.STRESS:
                tv.setTextColor(scheme.dayStressTextColor());
                break;
            //范围内、左边界
            case DayStatus.RANGE:
            case DayStatus.BOUND_L:
            case DayStatus.BOUND_M:
            case DayStatus.BOUND_R:
                tv.setTextColor(scheme.daySelectTextColor());
                break;
            default:
                break;
        }
    }

    private void setBackgroundStatus(DayEntity entity, ColorScheme scheme) {
        switch (entity.status()) {
            //正常、强调
            case DayStatus.NORMAL:
            case DayStatus.STRESS:
                setBackgroundColor(scheme.dayNormalBackgroundColor());
                setEnabled(true);
                break;
            //不可用
            case DayStatus.INVALID:
                tvDay.setTextColor(scheme.dayInvalidTextColor());
                setBackgroundColor(scheme.dayInvalidBackgroundColor());
                setEnabled(false);
                break;
            //范围内
            case DayStatus.RANGE:
                setBackgroundColor(ColorUtils.setAlphaComponent(scheme.daySelectBackgroundColor(), 200));
                setEnabled(true);
                break;
            //左边界、单选、右边界
            case DayStatus.BOUND_L:
            case DayStatus.BOUND_M:
            case DayStatus.BOUND_R:
                tvDay.setTextColor(scheme.daySelectTextColor());
                tvDesc.setTextColor(scheme.daySelectTextColor());
                tvDesc.setText(entity.note());
                setBackgroundColor(scheme.daySelectBackgroundColor());
                break;
            default:
                break;
        }
    }

}