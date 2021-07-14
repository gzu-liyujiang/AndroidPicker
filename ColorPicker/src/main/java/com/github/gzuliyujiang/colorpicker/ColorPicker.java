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

package com.github.gzuliyujiang.colorpicker;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.CallSuper;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;

import com.github.gzuliyujiang.basepicker.BottomDialog;
import com.github.gzuliyujiang.basepicker.PickerLog;

import java.util.Locale;

/**
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2021/6/10 10:54
 */
@SuppressWarnings("unused")
public class ColorPicker extends BottomDialog implements View.OnClickListener, OnColorChangedListener {
    protected TextView cancelView;
    protected TextView hexView;
    protected TextView okView;
    protected ColorGradientView colorGradientView;
    protected BrightnessGradientView brightnessGradientView;
    private boolean initialized = false;
    private int initColor = Color.YELLOW;
    private OnColorPickedListener onColorPickedListener;

    public ColorPicker(@NonNull Activity activity) {
        super(activity);
    }

    public ColorPicker(@NonNull Activity activity, @StyleRes int themeResId) {
        super(activity, themeResId);
    }

    @NonNull
    @Override
    protected View createContentView(@NonNull Activity activity) {
        return View.inflate(activity, R.layout.color_picker_content, null);
    }

    @CallSuper
    @Override
    protected void initView(@NonNull View contentView) {
        super.initView(contentView);
        cancelView = contentView.findViewById(R.id.color_picker_cancel);
        hexView = contentView.findViewById(R.id.color_picker_hex);
        okView = contentView.findViewById(R.id.color_picker_ok);
        colorGradientView = contentView.findViewById(R.id.color_picker_panel);
        brightnessGradientView = contentView.findViewById(R.id.color_picker_bright);
    }

    @CallSuper
    @Override
    protected void initData() {
        super.initData();
        initialized = true;
        if (cancelView != null) {
            cancelView.setOnClickListener(this);
        }
        if (okView != null) {
            okView.setOnClickListener(this);
        }
        hexView.getPaint().setStrokeWidth(hexView.getResources().getDisplayMetrics().density * 3);
        colorGradientView.setOnColorChangedListener(this);
        brightnessGradientView.setOnColorChangedListener(this);
        colorGradientView.setBrightnessGradientView(brightnessGradientView);
        //将触发onColorChanged，故必须先待其他控件初始化完成后才能调用
        colorGradientView.setColor(initColor);
    }

    @CallSuper
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.color_picker_cancel) {
            PickerLog.print("cancel clicked");
            onCancel();
            dismiss();
        } else if (id == R.id.color_picker_ok) {
            PickerLog.print("ok clicked");
            onOk();
            dismiss();
        }
    }

    @Override
    public void onColorChanged(@ColorInt int color) {
        updateCurrentColor(color);
    }

    protected void onCancel() {

    }

    protected void onOk() {
        if (onColorPickedListener != null) {
            onColorPickedListener.onColorPicked(getCurrentColor());
        }
    }

    private void updateCurrentColor(@ColorInt int color) {
        String hexString = Utils.toHexString(color, false).toUpperCase(Locale.PRC);
        hexView.setText(hexString);
        hexView.setTextColor(Utils.reverseColor(color));
        hexView.setBackgroundColor(color);
    }

    /**
     * 设置初始默认颜色
     *
     * @param initColor 颜色值，如：0xFFFF00FF
     */
    public void setInitColor(@ColorInt int initColor) {
        this.initColor = initColor;
        if (initialized) {
            colorGradientView.setColor(initColor);
        }
    }

    public void setOnColorPickListener(OnColorPickedListener onColorPickedListener) {
        this.onColorPickedListener = onColorPickedListener;
    }

    @ColorInt
    public final int getCurrentColor() {
        try {
            return Color.parseColor("#" + hexView.getText());
        } catch (Exception e) {
            PickerLog.print(e);
            return initColor;
        }
    }

    public final TextView getOkView() {
        return okView;
    }

    public final TextView getHexView() {
        return hexView;
    }

    public final TextView getCancelView() {
        return cancelView;
    }

    public final ColorGradientView getColorGradientView() {
        return colorGradientView;
    }

    public final BrightnessGradientView getBrightnessGradientView() {
        return brightnessGradientView;
    }

}
