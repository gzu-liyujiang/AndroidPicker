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

package com.github.gzuliyujiang.wheelpicker;

import android.app.Activity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;

import com.github.gzuliyujiang.basepicker.ConfirmPicker;
import com.github.gzuliyujiang.wheelpicker.annotation.TimeMode;
import com.github.gzuliyujiang.wheelpicker.contract.OnTimePickedListener;
import com.github.gzuliyujiang.wheelpicker.widget.TimeWheelLayout;

/**
 * 时间选择器
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2021/6/5 18:19
 */
@SuppressWarnings("unused")
public class TimePicker extends ConfirmPicker {
    protected TimeWheelLayout wheelLayout;
    private OnTimePickedListener onTimePickedListener;

    public TimePicker(@NonNull Activity activity) {
        super(activity);
    }

    public TimePicker(@NonNull Activity activity, @StyleRes int themeResId) {
        super(activity, themeResId);
    }

    @NonNull
    @Override
    protected View createBodyView(@NonNull Activity activity) {
        wheelLayout = new TimeWheelLayout(activity);
        return wheelLayout;
    }

    @Override
    protected void initView(@NonNull View contentView) {
        super.initView(contentView);
    }

    @Override
    protected void initData() {
        super.initData();
        wheelLayout.setTimeMode(provideTimeMode());
    }

    protected int provideTimeMode() {
        return TimeMode.HOUR_24_NO_SECOND;
    }

    @Override
    protected void onCancel() {

    }

    @Override
    protected void onOk() {
        if (onTimePickedListener != null) {
            int hour = wheelLayout.getSelectedHour();
            int minute = wheelLayout.getSelectedMinute();
            int second = wheelLayout.getSelectedSecond();
            onTimePickedListener.onTimePicked(hour, minute, second);
        }
    }

    public void setOnTimePickedListener(OnTimePickedListener onTimePickedListener) {
        this.onTimePickedListener = onTimePickedListener;
    }

    public final TimeWheelLayout getWheelLayout() {
        return wheelLayout;
    }

}
