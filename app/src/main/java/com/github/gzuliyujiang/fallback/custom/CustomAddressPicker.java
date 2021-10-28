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

package com.github.gzuliyujiang.fallback.custom;

import android.app.Activity;
import android.view.View;

import androidx.annotation.NonNull;

import com.github.gzuliyujiang.dialog.BottomDialog;
import com.github.gzuliyujiang.fallback.R;
import com.github.gzuliyujiang.wheelpicker.annotation.AddressMode;
import com.github.gzuliyujiang.wheelpicker.contract.AddressLoader;
import com.github.gzuliyujiang.wheelpicker.contract.AddressReceiver;
import com.github.gzuliyujiang.wheelpicker.contract.OnAddressPickedListener;
import com.github.gzuliyujiang.wheelpicker.entity.CityEntity;
import com.github.gzuliyujiang.wheelpicker.entity.CountyEntity;
import com.github.gzuliyujiang.wheelpicker.entity.ProvinceEntity;
import com.github.gzuliyujiang.wheelpicker.impl.AddressProvider;
import com.github.gzuliyujiang.wheelpicker.impl.AssetAddressLoader;
import com.github.gzuliyujiang.wheelpicker.utility.AddressJsonParser;
import com.github.gzuliyujiang.wheelpicker.widget.LinkageWheelLayout;

import java.util.List;

/**
 * 自定义地址选择器
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2021/6/7 16:03
 */
public class CustomAddressPicker extends BottomDialog implements AddressReceiver, View.OnClickListener {
    protected LinkageWheelLayout wheelLayout;
    private OnAddressPickedListener onAddressPickedListener;

    public CustomAddressPicker(@NonNull Activity activity) {
        super(activity);
    }

    public CustomAddressPicker(@NonNull Activity activity, int themeResId) {
        super(activity, themeResId);
    }

    @NonNull
    @Override
    protected View createContentView() {
        return View.inflate(activity, R.layout.wheel_picker_custom_ui_address, null);
    }

    @Override
    protected void initView() {
        super.initView();
        contentView.findViewById(R.id.wheel_picker_address_cancel).setOnClickListener(this);
        contentView.findViewById(R.id.wheel_picker_address_confirm).setOnClickListener(this);
        wheelLayout = contentView.findViewById(R.id.wheel_picker_address_wheel);
    }

    @Override
    protected void initData() {
        super.initData();
        wheelLayout.showLoading();
        AddressLoader addressLoader = new AssetAddressLoader(getContext(), "pca-code.json");
        addressLoader.loadJson(this,
                new AddressJsonParser.Builder()
                        .provinceCodeField("code")
                        .provinceNameField("name")
                        .provinceChildField("children")
                        .cityCodeField("code")
                        .cityNameField("name")
                        .cityChildField("children")
                        .countyCodeField("code")
                        .countyNameField("name")
                        .build());
    }

    @Override
    public void onAddressReceived(@NonNull List<ProvinceEntity> data) {
        wheelLayout.hideLoading();
        wheelLayout.setData(new AddressProvider(data, AddressMode.PROVINCE_CITY_COUNTY));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.wheel_picker_address_cancel) {
            dismiss();
            return;
        }
        if (id == R.id.wheel_picker_address_confirm) {
            if (onAddressPickedListener != null) {
                ProvinceEntity province = wheelLayout.getFirstWheelView().getCurrentItem();
                CityEntity city = wheelLayout.getSecondWheelView().getCurrentItem();
                CountyEntity county = wheelLayout.getThirdWheelView().getCurrentItem();
                onAddressPickedListener.onAddressPicked(province, city, county);
            }
            dismiss();
        }
    }

    public void setDefaultValue(String province, String city, String county) {
        wheelLayout.setDefaultValue(province, city, county);
    }

    public void setOnAddressPickedListener(OnAddressPickedListener onAddressPickedListener) {
        this.onAddressPickedListener = onAddressPickedListener;
    }

}
