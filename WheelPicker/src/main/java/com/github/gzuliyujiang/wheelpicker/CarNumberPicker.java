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

import com.github.gzuliyujiang.wheelpicker.contract.LinkageProvider;
import com.github.gzuliyujiang.wheelpicker.contract.OnCarNumberPickedListener;
import com.github.gzuliyujiang.wheelpicker.contract.OnLinkagePickedListener;
import com.github.gzuliyujiang.wheelpicker.widget.CarNumberWheelLayout;

/**
 * 中国大陆车牌号滚轮选择
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2016/12/18 10:47
 */
@SuppressWarnings({"unused"})
public class CarNumberPicker extends LinkagePicker {
    private OnCarNumberPickedListener onCarNumberPickedListener;

    public CarNumberPicker(@NonNull Activity activity) {
        super(activity);
    }

    public CarNumberPicker(@NonNull Activity activity, @StyleRes int themeResId) {
        super(activity, themeResId);
    }

    @Deprecated
    @Override
    public void setData(@NonNull LinkageProvider data) {
        throw new UnsupportedOperationException("Data already preset");
    }

    @Deprecated
    @Override
    public void setOnLinkagePickedListener(OnLinkagePickedListener onLinkagePickedListener) {
        throw new UnsupportedOperationException("Use setOnCarNumberPickedListener instead");
    }

    @NonNull
    @Override
    protected View createBodyView() {
        wheelLayout = new CarNumberWheelLayout(activity);
        return wheelLayout;
    }

    @Override
    protected void initView() {
        super.initView();
        titleView.setText("车牌选择");
    }

    @Override
    protected void onOk() {
        if (onCarNumberPickedListener != null) {
            String province = wheelLayout.getFirstWheelView().getCurrentItem().toString();
            String letter = wheelLayout.getSecondWheelView().getCurrentItem().toString();
            onCarNumberPickedListener.onCarNumberPicked(province, letter);
        }
    }


    public void setOnCarNumberPickedListener(OnCarNumberPickedListener onCarNumberPickedListener) {
        this.onCarNumberPickedListener = onCarNumberPickedListener;
    }

}
