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

import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;

import com.github.gzuliyujiang.wheelpicker.contract.AddressLoader;
import com.github.gzuliyujiang.wheelpicker.contract.AddressReceiver;
import com.github.gzuliyujiang.wheelpicker.contract.LinkageProvider;
import com.github.gzuliyujiang.wheelpicker.contract.OnAddressPickedListener;
import com.github.gzuliyujiang.wheelpicker.contract.OnLinkagePickedListener;
import com.github.gzuliyujiang.wheelpicker.entity.CityEntity;
import com.github.gzuliyujiang.wheelpicker.entity.CountyEntity;
import com.github.gzuliyujiang.wheelpicker.entity.ProvinceEntity;
import com.github.gzuliyujiang.wheelpicker.impl.AddressProvider;
import com.github.gzuliyujiang.wheelpicker.impl.AssetsAddressLoader;

import java.util.List;

/**
 * 省市区县滚轮选择
 * <p>
 * 数据来源：
 * 1、国家民政局：http://www.mca.gov.cn/article/sj/xzqh
 * 2、国家统计局：http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm
 * 3、台湾维基数据：https://zh.wikipedia.org/wiki/中华人民共和国行政区划代码_(7区)
 * 4、港澳维基数据：https://zh.wikipedia.org/wiki/中华人民共和国行政区划代码_(8区)
 * 5、数据抓取转化：https://github.com/small-dream/China_Province_City
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2019/6/15 12:17
 */
@SuppressWarnings({"unused"})
public class AddressPicker extends LinkagePicker implements AddressReceiver {
    private AddressLoader addressLoader;
    private int addressMode;
    private OnAddressPickedListener onAddressPickedListener;

    public AddressPicker(@NonNull Activity activity) {
        super(activity);
    }

    public AddressPicker(@NonNull Activity activity, @StyleRes int themeResId) {
        super(activity, themeResId);
    }

    @Override
    protected void initData() {
        super.initData();
        if (addressLoader == null) {
            return;
        }
        addressLoader.loadJson(this);
    }

    @Override
    public void onAddressReceived(@NonNull List<ProvinceEntity> data) {
        wheelLayout.setData(new AddressProvider(data, addressMode));
    }

    @Deprecated
    @Override
    public void setData(@NonNull LinkageProvider data) {
        throw new UnsupportedOperationException("Use setAddressMode or setAddressLoader instead");
    }

    @Deprecated
    @Override
    public void setOnLinkagePickedListener(OnLinkagePickedListener onLinkagePickedListener) {
        throw new UnsupportedOperationException("Use setOnAddressPickedListener instead");
    }

    @Override
    protected void onOk() {
        if (onAddressPickedListener != null) {
            ProvinceEntity province = (ProvinceEntity) wheelLayout.getFirstWheelView().getCurrentItem();
            CityEntity city = (CityEntity) wheelLayout.getSecondWheelView().getCurrentItem();
            CountyEntity county = (CountyEntity) wheelLayout.getThirdWheelView().getCurrentItem();
            onAddressPickedListener.onAddressPicked(province, city, county);
        }
    }


    public void setOnAddressPickedListener(OnAddressPickedListener onAddressPickedListener) {
        this.onAddressPickedListener = onAddressPickedListener;
    }

    public void setAddressLoader(@NonNull AddressLoader loader) {
        this.addressLoader = loader;
    }

    public void setAddressMode(String jsonPath, int mode) {
        this.addressMode = mode;
        setAddressLoader(new AssetsAddressLoader(getContext(), jsonPath));
    }

}
