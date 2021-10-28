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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;

import com.github.gzuliyujiang.dialog.ModalDialog;
import com.github.gzuliyujiang.wheelpicker.contract.OnNumberPickedListener;
import com.github.gzuliyujiang.wheelpicker.widget.NumberWheelLayout;
import com.github.gzuliyujiang.wheelview.contract.WheelFormatter;
import com.github.gzuliyujiang.wheelview.widget.WheelView;

/**
 * 数字选择器
 *
 * @author 李玉江[QQ:1032694760]
 * @since 2015/10/24
 */
@SuppressWarnings("unused")
public class NumberPicker extends ModalDialog {
    protected NumberWheelLayout wheelLayout;
    private OnNumberPickedListener onNumberPickedListener;

    public NumberPicker(@NonNull Activity activity) {
        super(activity);
    }

    public NumberPicker(@NonNull Activity activity, @StyleRes int themeResId) {
        super(activity, themeResId);
    }

    @NonNull
    @Override
    protected View createBodyView() {
        wheelLayout = new NumberWheelLayout(activity);
        return wheelLayout;
    }

    @Override
    protected void onCancel() {

    }

    @Override
    protected void onOk() {
        if (onNumberPickedListener != null) {
            int position = wheelLayout.getWheelView().getCurrentPosition();
            Number item = wheelLayout.getWheelView().getCurrentItem();
            onNumberPickedListener.onNumberPicked(position, item);
        }
    }

    public void setFormatter(WheelFormatter formatter) {
        wheelLayout.getWheelView().setFormatter(formatter);
    }

    public void setRange(int min, int max, int step) {
        wheelLayout.setRange(min, max, step);
    }

    public void setRange(float min, float max, float step) {
        wheelLayout.setRange(min, max, step);
    }

    public void setDefaultValue(Object item) {
        wheelLayout.setDefaultValue(item);
    }

    public void setDefaultPosition(int position) {
        wheelLayout.setDefaultPosition(position);
    }

    public final void setOnNumberPickedListener(OnNumberPickedListener onNumberPickedListener) {
        this.onNumberPickedListener = onNumberPickedListener;
    }

    public final NumberWheelLayout getWheelLayout() {
        return wheelLayout;
    }

    public final WheelView getWheelView() {
        return wheelLayout.getWheelView();
    }

    public final TextView getLabelView() {
        return wheelLayout.getLabelView();
    }

}

