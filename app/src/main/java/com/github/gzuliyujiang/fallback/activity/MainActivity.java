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

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.github.gzuliyujiang.dialog.DialogConfig;
import com.github.gzuliyujiang.dialog.DialogStyle;
import com.github.gzuliyujiang.fallback.R;
import com.github.gzuliyujiang.fallback.custom.AntFortuneLikeProvider;
import com.github.gzuliyujiang.wheelpicker.OptionPicker;
import com.github.gzuliyujiang.wheelpicker.contract.OnOptionPickedListener;
import com.github.gzuliyujiang.wheelpicker.widget.LinkageWheelLayout;
import com.github.gzuliyujiang.wheelpicker.widget.OptionWheelLayout;
import com.github.gzuliyujiang.wheelview.widget.WheelView;

import java.util.Arrays;

/**
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2021/6/3 18:18
 */
public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker_main);
        WheelView wheelView = findViewById(R.id.wheel_view);
        wheelView.setData(Arrays.asList("第1项", "第2项", "第3项", "第4项", "第5项", "第6项", "第7项", "第8项", "第9项",
                "aaa", "bbb", "ccc", "ddd", "eee", "fff", "第N项"));
        OptionWheelLayout optionWheelLayout = findViewById(R.id.wheel_option);
        optionWheelLayout.setData(Arrays.asList("aaa", "bbb", "ccc", "123", "xxx", "yyy", "zzz"));
        LinkageWheelLayout linkageWheelLayout = findViewById(R.id.wheel_linkage);
        linkageWheelLayout.setData(new AntFortuneLikeProvider());
    }

    private void startActivity(Class<?> cls) {
        startActivity(new Intent(this, cls));
    }

    public void onDialogStyle(View view) {
        OptionPicker picker = new OptionPicker(this);
        picker.setData("默认-屏幕底部弹窗", "样式1-屏幕底部弹窗", "样式2-屏幕底部弹窗", "样式3-屏幕中间弹窗");
        picker.setOnOptionPickedListener(new OnOptionPickedListener() {
            @Override
            public void onOptionPicked(int position, Object item) {
                switch (position) {
                    case 1:
                        DialogConfig.setDialogStyle(DialogStyle.One);
                        break;
                    case 2:
                        DialogConfig.setDialogStyle(DialogStyle.Two);
                        break;
                    case 3:
                        DialogConfig.setDialogStyle(DialogStyle.Three);
                        break;
                    case 0:
                    default:
                        DialogConfig.setDialogStyle(DialogStyle.Default);
                        break;
                }
            }
        });
        picker.show();
    }

    public void onDialogFragment(View view) {
        new OptionPickerFragment().show(getSupportFragmentManager(), OptionPickerFragment.class.getName());
    }

    public void onDateTimePicker(View view) {
        startActivity(DateTimePickerActivity.class);
    }

    public void onSinglePicker(View view) {
        startActivity(SinglePickerActivity.class);
    }

    public void onLinkagePicker(View view) {
        startActivity(LinkagePickerActivity.class);
    }

    public void onAddressPicker(View view) {
        startActivity(AddressPickerActivity.class);
    }

    public void onColorPicker(View view) {
        startActivity(ColorPickerActivity.class);
    }

    public void onFilePicker(View view) {
        startActivity(FilePickerActivity.class);
    }

    public void onCalendarPicker(View view) {
        startActivity(CalendarPickerActivity.class);
    }

    public void onImagePicker(View view) {
        startActivity(ImagePickerActivity.class);
    }

}
