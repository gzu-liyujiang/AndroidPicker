package com.oxandon.calendar.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oxandon.calendar.R;
import com.oxandon.calendar.annotation.Status;
import com.oxandon.calendar.protocol.DayEntity;

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
        tvDesc = findViewById(R.id.tvDesc);
        tvDay = findViewById(R.id.tvDay);
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
                tv.setTextColor(ContextCompat.getColor(getContext(), R.color.day_text_normal_color));
                break;
            //不可用
            case Status.INVALID:
                tv.setTextColor(ContextCompat.getColor(getContext(), R.color.day_text_invalid_color));
                break;
            //范围内
            case Status.RANGE:
                tv.setTextColor(ContextCompat.getColor(getContext(), R.color.day_text_range_color));
                break;
            //左边界
            case Status.BOUND_L:
            case Status.BOUND_M:
            case Status.BOUND_R:
                tv.setTextColor(ContextCompat.getColor(getContext(), R.color.day_text_select_color));
                break;
            //强调
            case Status.STRESS:
                tv.setTextColor(ContextCompat.getColor(getContext(), R.color.day_text_stress_color));
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
                setBackgroundColor(ContextCompat.getColor(getContext(), R.color.day_background_normal_color));
                setEnabled(true);
                break;
            //不可用
            case Status.INVALID:
                setBackgroundColor(ContextCompat.getColor(getContext(), R.color.day_background_invalid_color));
                setTextStatusColor(tvDay, entity.status());
                setEnabled(false);
                break;
            //范围内
            case Status.RANGE:
                setBackgroundColor(ContextCompat.getColor(getContext(), R.color.day_background_range_color));
                setEnabled(true);
                break;
            //左边界
            case Status.BOUND_L:
                tvDay.setTextColor(ContextCompat.getColor(getContext(), R.color.day_text_select_color));
                tvDesc.setTextColor(ContextCompat.getColor(getContext(), R.color.day_text_select_color));
                tvDesc.setText(entity.note());
                setBackgroundResource(R.drawable.day_shape_range_lbg);
                break;
            //单选
            case Status.BOUND_M:
                tvDay.setTextColor(ContextCompat.getColor(getContext(), R.color.day_text_select_color));
                tvDesc.setTextColor(ContextCompat.getColor(getContext(), R.color.day_text_select_color));
                tvDesc.setText(entity.note());
                setBackgroundResource(R.drawable.day_shape_range_mbg);
                break;
            //右边界
            case Status.BOUND_R:
                tvDay.setTextColor(ContextCompat.getColor(getContext(), R.color.day_text_select_color));
                tvDesc.setTextColor(ContextCompat.getColor(getContext(), R.color.day_text_select_color));
                tvDesc.setText(entity.note());
                setBackgroundResource(R.drawable.day_shape_range_rbg);
                break;
            //强调
            case Status.STRESS:
                setBackgroundColor(ContextCompat.getColor(getContext(), R.color.day_background_range_color));
                setEnabled(true);
                break;
        }
    }
}