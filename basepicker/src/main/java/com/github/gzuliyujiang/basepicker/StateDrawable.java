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

package com.github.gzuliyujiang.basepicker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

/**
 * 按下状态与普通状态下显示不同的颜色
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2017/01/01 05:30
 */
@SuppressWarnings("unused")
public class StateDrawable extends StateListDrawable {

    public StateDrawable(@ColorInt int pressedColor) {
        this(Color.TRANSPARENT, pressedColor);
    }

    public StateDrawable(@ColorInt int normalColor, @ColorInt int pressedColor) {
        addState(new ColorDrawable(normalColor), new ColorDrawable(pressedColor));
    }

    public StateDrawable(@NonNull Context context, @DrawableRes int normalRes, @DrawableRes int pressedRes) {
        addState(context.getDrawable(normalRes), context.getDrawable(pressedRes));
    }

    public StateDrawable(Drawable normal, Drawable pressed) {
        addState(normal, pressed);
    }

    protected void addState(Drawable normal, Drawable pressed) {
        addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);
        addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_focused}, pressed);
        addState(new int[]{android.R.attr.state_enabled}, normal);
        addState(new int[]{android.R.attr.state_focused}, pressed);
        addState(new int[]{android.R.attr.state_window_focused}, normal);
        addState(new int[]{}, normal);
    }

}
