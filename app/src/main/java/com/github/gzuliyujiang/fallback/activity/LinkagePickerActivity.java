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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.github.gzuliyujiang.fallback.R;
import com.github.gzuliyujiang.fallback.custom.AntFortuneLikePicker;
import com.github.gzuliyujiang.wheelpicker.CarNumberPicker;
import com.github.gzuliyujiang.wheelpicker.contract.OnCarNumberPickedListener;
import com.github.gzuliyujiang.wheelpicker.contract.OnLinkagePickedListener;

/**
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2021/6/4 12:40
 */
public class LinkagePickerActivity extends FragmentActivity implements OnCarNumberPickedListener, OnLinkagePickedListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker_linkage);
    }

    @Override
    public void onCarNumberPicked(String province, String letter) {
        Toast.makeText(getApplication(), province + " " + letter, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLinkagePicked(Object first, Object second, Object third) {
        Toast.makeText(getApplication(), first + " " + second + " " + third, Toast.LENGTH_SHORT).show();
    }

    public void onCarNumber(View view) {
        CarNumberPicker picker = new CarNumberPicker(this, R.style.SheetDialog);
        picker.setBodyWidth(90);
        picker.setOnCarNumberPickedListener(this);
        picker.show();
    }

    public void onLikeAntFortune(View view) {
        AntFortuneLikePicker picker = new AntFortuneLikePicker(this);
        picker.setOnLinkagePickedListener(this);
        picker.show();
    }

}
