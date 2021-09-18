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

package com.github.gzuliyujiang.wheelpicker.impl;

import androidx.annotation.NonNull;

import com.github.gzuliyujiang.wheelpicker.annotation.AddressMode;
import com.github.gzuliyujiang.wheelpicker.contract.LinkageProvider;
import com.github.gzuliyujiang.wheelpicker.entity.CityEntity;
import com.github.gzuliyujiang.wheelpicker.entity.CountyEntity;
import com.github.gzuliyujiang.wheelpicker.entity.ProvinceEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2021/6/7 11:55
 */
public class AddressProvider implements LinkageProvider {
    private final List<ProvinceEntity> data;
    private final int mode;

    public AddressProvider(@NonNull List<ProvinceEntity> data, int mode) {
        this.data = data;
        this.mode = mode;
    }

    @Override
    public boolean firstLevelVisible() {
        return mode == AddressMode.PROVINCE_CITY_COUNTY || mode == AddressMode.PROVINCE_CITY;
    }

    @Override
    public boolean thirdLevelVisible() {
        return mode == AddressMode.PROVINCE_CITY_COUNTY || mode == AddressMode.CITY_COUNTY;
    }

    @NonNull
    @Override
    public List<ProvinceEntity> provideFirstData() {
        return data;
    }

    @NonNull
    @Override
    public List<CityEntity> linkageSecondData(int firstIndex) {
        if (data.size() == 0) {
            return new ArrayList<>();
        }
        if (firstIndex == INDEX_NO_FOUND) {
            firstIndex = 0;
        }
        return data.get(firstIndex).getCityList();
    }

    @NonNull
    @Override
    public List<CountyEntity> linkageThirdData(int firstIndex, int secondIndex) {
        List<CityEntity> data = linkageSecondData(firstIndex);
        if (data.size() == 0) {
            return new ArrayList<>();
        }
        if (secondIndex == INDEX_NO_FOUND) {
            secondIndex = 0;
        }
        return data.get(secondIndex).getCountyList();
    }

    @Override
    public int findFirstIndex(Object firstValue) {
        if (firstValue == null) {
            return INDEX_NO_FOUND;
        }
        if (firstValue instanceof ProvinceEntity) {
            return data.indexOf(firstValue);
        }
        for (int i = 0, n = data.size(); i < n; i++) {
            ProvinceEntity entity = data.get(i);
            if (entity.getCode().equals(firstValue.toString()) ||
                    entity.getName().contains(firstValue.toString())) {
                return i;
            }
        }
        return INDEX_NO_FOUND;
    }

    @Override
    public int findSecondIndex(int firstIndex, Object secondValue) {
        if (secondValue == null) {
            return INDEX_NO_FOUND;
        }
        List<CityEntity> cityList = linkageSecondData(firstIndex);
        if (secondValue instanceof CityEntity) {
            return cityList.indexOf(secondValue);
        }
        for (int i = 0, n = cityList.size(); i < n; i++) {
            CityEntity entity = cityList.get(i);
            if (entity.getCode().equals(secondValue.toString()) ||
                    entity.getName().contains(secondValue.toString())) {
                return i;
            }
        }
        return INDEX_NO_FOUND;
    }

    @Override
    public int findThirdIndex(int firstIndex, int secondIndex, Object thirdValue) {
        if (thirdValue == null) {
            return INDEX_NO_FOUND;
        }
        List<CountyEntity> countyList = linkageThirdData(firstIndex, secondIndex);
        if (thirdValue instanceof CountyEntity) {
            return countyList.indexOf(thirdValue);
        }
        for (int i = 0, n = countyList.size(); i < n; i++) {
            CountyEntity entity = countyList.get(i);
            if (entity.getCode().equals(thirdValue.toString()) ||
                    entity.getName().contains(thirdValue.toString())) {
                return i;
            }
        }
        return INDEX_NO_FOUND;
    }

}
