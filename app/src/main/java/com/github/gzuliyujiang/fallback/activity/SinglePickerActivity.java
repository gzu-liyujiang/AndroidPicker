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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.github.gzuliyujiang.fallback.R;
import com.github.gzuliyujiang.fallback.bean.GoodsCategoryBean;
import com.github.gzuliyujiang.wheelpicker.ConstellationPicker;
import com.github.gzuliyujiang.wheelpicker.EthnicPicker;
import com.github.gzuliyujiang.wheelpicker.NumberPicker;
import com.github.gzuliyujiang.wheelpicker.OptionPicker;
import com.github.gzuliyujiang.wheelpicker.PhoneCodePicker;
import com.github.gzuliyujiang.wheelpicker.SexPicker;
import com.github.gzuliyujiang.wheelpicker.annotation.EthnicSpec;
import com.github.gzuliyujiang.wheelpicker.contract.OnNumberPickedListener;
import com.github.gzuliyujiang.wheelpicker.contract.OnOptionPickedListener;
import com.github.gzuliyujiang.wheelview.contract.WheelFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 单项滚轮选择器
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2019/6/23
 */
public class SinglePickerActivity extends FragmentActivity implements OnNumberPickedListener, OnOptionPickedListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker_single);
    }

    @Override
    public void onNumberPicked(int position, Number item) {
        Toast.makeText(this, position + "-" + item, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onOptionPicked(int position, Object item) {
        Toast.makeText(this, position + "-" + item, Toast.LENGTH_SHORT).show();
    }

    public void onInteger(View view) {
        NumberPicker picker = new NumberPicker(this);
        picker.setOnNumberPickedListener(this);
        picker.setFormatter(new WheelFormatter() {
            @Override
            public String formatItem(@NonNull Object item) {
                return item.toString() + " cm";
            }
        });
        picker.setRange(140, 200, 1);
        picker.setDefaultValue(172);
        picker.getTitleView().setText("身高选择");
        picker.show();
    }

    public void onFloat(View view) {
        NumberPicker picker = new NumberPicker(this);
        picker.setBodyWidth(100);
        picker.setOnNumberPickedListener(this);
        picker.setFormatter(new WheelFormatter() {
            @Override
            public String formatItem(@NonNull Object item) {
                DecimalFormat df = new DecimalFormat("0.00");
                return df.format(item);
            }
        });
        picker.setRange(-10f, 10f, 0.1f);
        picker.setDefaultValue(5f);
        picker.getTitleView().setText("温度选择");
        picker.getLabelView().setText("℃");
        picker.show();
    }

    public void onOptionText(View view) {
        OptionPicker picker = new OptionPicker(this);
        picker.setBackgroundColor(true, 0xFFFFFFFF);
        picker.setData("测试", "很长很长很长很长很长很长很长很长很长很长很长很长很长很长");
        picker.setOnOptionPickedListener(this);
        picker.getTitleView().setText("这是标题");
        picker.getWheelView().setCyclicEnabled(true);
        picker.getWheelView().setCurvedEnabled(true);
        picker.getWheelView().setCurvedMaxAngle(60);
        picker.show();
    }

    public void onOptionBean(View view) {
        List<GoodsCategoryBean> data = new ArrayList<>();
        data.add(new GoodsCategoryBean(1, "食品生鲜"));
        data.add(new GoodsCategoryBean(2, "家用电器"));
        data.add(new GoodsCategoryBean(3, "家居生活"));
        data.add(new GoodsCategoryBean(4, "医疗保健"));
        data.add(new GoodsCategoryBean(5, "酒水饮料"));
        data.add(new GoodsCategoryBean(6, "图书音像"));
        OptionPicker picker = new OptionPicker(this);
        picker.setBodyWidth(140);
        picker.setOnOptionPickedListener(this);
        picker.setData(data);
        picker.setDefaultPosition(2);
        picker.show();
    }

    public void onSex(View view) {
        SexPicker picker = new SexPicker(this);
        picker.setDefaultValue("女");
        picker.setOnOptionPickedListener(this);
        picker.show();
    }

    public void onEthnic(View view) {
        EthnicPicker picker = new EthnicPicker(this);
        picker.setEthnicSpec(EthnicSpec.SEVENTH_NATIONAL_CENSUS);
        picker.setOnOptionPickedListener(this);
        picker.show();
    }

    public void onConstellation(View view) {
        ConstellationPicker picker = new ConstellationPicker(this, true);
        picker.setDefaultValue("射手座");
        picker.setOnOptionPickedListener(this);
        picker.show();
    }

    public void onPhoneCode(View view) {
        PhoneCodePicker picker = new PhoneCodePicker(this);
        picker.setDefaultPosition(2);
        picker.setOnOptionPickedListener(this);
        picker.show();
    }

}
