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

package com.github.gzuliyujiang.fallback.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.github.gzuliyujiang.colorpicker.ColorGradientView;
import com.github.gzuliyujiang.colorpicker.BrightnessGradientView;
import com.github.gzuliyujiang.colorpicker.ColorPicker;
import com.github.gzuliyujiang.colorpicker.OnColorChangedListener;
import com.github.gzuliyujiang.colorpicker.OnColorPickedListener;
import com.github.gzuliyujiang.fallback.R;

/**
 * 颜色选择器
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2021/6/10 11:49
 */
public class ColorPickerActivity extends FragmentActivity implements View.OnClickListener, OnColorChangedListener {
    private BrightnessGradientView brightnessGradientView;
    private Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker_color);
        button = findViewById(R.id.color_picker_button);
        button.setOnClickListener(this);
        ColorGradientView colorGradientView = findViewById(R.id.color_picker_panel);
        colorGradientView.setPointerDrawable(android.R.drawable.ic_menu_compass);
        colorGradientView.setColor(0xFF7FF7FF);
        colorGradientView.setOnColorChangedListener(this);
        brightnessGradientView = findViewById(R.id.color_picker_bright);
        brightnessGradientView.setColor(0xFF377377);
        brightnessGradientView.setOnColorChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.color_picker_button) {
            ColorPicker picker = new ColorPicker(this);
            picker.setInitColor(0xFF7FF7FF);
            picker.setOnColorPickListener(new OnColorPickedListener() {
                @Override
                public void onColorPicked(int pickedColor) {
                    button.setTextColor(pickedColor);
                }
            });
            picker.show();
        }
    }

    @Override
    public void onColorChanged(ColorGradientView gradientView, int color) {
        if (gradientView.getId() == R.id.color_picker_panel) {
            brightnessGradientView.setColor(color);
        }
        button.setTextColor(color);
    }

}
