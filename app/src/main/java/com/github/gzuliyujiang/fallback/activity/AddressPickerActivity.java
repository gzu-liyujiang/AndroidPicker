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
import com.github.gzuliyujiang.fallback.custom.CustomAddressPicker;
import com.github.gzuliyujiang.fallback.custom.TextAddressLoader;
import com.github.gzuliyujiang.fallback.custom.TextAddressParser;
import com.github.gzuliyujiang.wheelpicker.AddressPicker;
import com.github.gzuliyujiang.wheelpicker.annotation.AddressMode;
import com.github.gzuliyujiang.wheelpicker.contract.OnAddressPickedListener;
import com.github.gzuliyujiang.wheelpicker.entity.CityEntity;
import com.github.gzuliyujiang.wheelpicker.entity.CountyEntity;
import com.github.gzuliyujiang.wheelpicker.entity.ProvinceEntity;
import com.github.gzuliyujiang.wheelpicker.utility.AddressJsonParser;

/**
 * 地址滚轮选择器
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2019/6/23
 */
public class AddressPickerActivity extends FragmentActivity implements OnAddressPickedListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker_address);
    }

    @Override
    public void onAddressPicked(ProvinceEntity province, CityEntity city, CountyEntity county) {
        Toast.makeText(this, province + " " + city + " " + county, Toast.LENGTH_SHORT).show();
    }

    public void onProvinceCityCounty(View view) {
        AddressPicker picker = new AddressPicker(this);
        picker.setAddressMode(AddressMode.PROVINCE_CITY_COUNTY);
        picker.setDefaultValue("贵州省", "贵阳市", "观山湖区");
        picker.setOnAddressPickedListener(this);
        picker.show();
    }

    public void onProvinceCity(View view) {
        AddressPicker picker = new AddressPicker(this);
        picker.setAddressMode(AddressMode.PROVINCE_CITY);
        picker.setDefaultValue("520000", "520100", "520115");
        picker.setOnAddressPickedListener(this);
        picker.show();
    }

    public void onCityCounty(View view) {
        AddressPicker picker = new AddressPicker(this);
        picker.setAddressMode(AddressMode.CITY_COUNTY);
        picker.setDefaultValue("贵州省", "毕节市", "纳雍县");
        picker.setOnAddressPickedListener(this);
        picker.show();
    }

    public void onCustomUi(View view) {
        CustomAddressPicker picker = new CustomAddressPicker(this);
        picker.setDefaultValue("贵州省", "毕节市", "纳雍县");
        picker.setOnAddressPickedListener(this);
        picker.show();
    }

    public void onCustomDataByJson(View view) {
        AddressPicker picker = new AddressPicker(this);
        picker.setAddressMode("city.json", AddressMode.PROVINCE_CITY_COUNTY,
                new AddressJsonParser.Builder()
                        .provinceCodeField("code")
                        .provinceNameField("name")
                        .provinceChildField("city")
                        .cityCodeField("code")
                        .cityNameField("name")
                        .cityChildField("area")
                        .countyCodeField("code")
                        .countyNameField("name")
                        .build());
        picker.setDefaultValue("贵州省", "毕节地区", "纳雍县");
        picker.setOnAddressPickedListener(this);
        picker.show();
    }

    public void onCustomDataByText(View view) {
        AddressPicker picker = new AddressPicker(this);
        picker.setAddressLoader(new TextAddressLoader(this), new TextAddressParser());
        picker.setDefaultValue("贵州省", "毕节地区", "纳雍县");
        picker.setOnAddressPickedListener(this);
        picker.show();
    }

}
