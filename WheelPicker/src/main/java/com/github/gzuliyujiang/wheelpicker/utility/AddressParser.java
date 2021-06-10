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

package com.github.gzuliyujiang.wheelpicker.utility;

import com.github.gzuliyujiang.basepicker.PickerLog;
import com.github.gzuliyujiang.wheelpicker.entity.CityEntity;
import com.github.gzuliyujiang.wheelpicker.entity.CountyEntity;
import com.github.gzuliyujiang.wheelpicker.entity.ProvinceEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用系统自带的“org.json”包转换地址数据
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2019/6/17 17:15
 */
public class AddressParser {

    public static List<ProvinceEntity> parseData(String json) {
        try {
            JSONArray provinceArray = new JSONArray(json);
            return parseProvince(provinceArray);
        } catch (JSONException e) {
            PickerLog.print(e);
        }
        return new ArrayList<>();
    }

    private static List<ProvinceEntity> parseProvince(JSONArray provinceArray) {
        List<ProvinceEntity> data = new ArrayList<>();
        for (int i = 0, x = provinceArray.length(); i < x; i++) {
            ProvinceEntity provinceEntity = new ProvinceEntity();
            JSONObject provinceObject = provinceArray.optJSONObject(i);
            provinceEntity.setCode(provinceObject.optString("code"));
            provinceEntity.setName(provinceObject.optString("name"));
            provinceEntity.setCityList(new ArrayList<>());
            JSONArray cityArray = provinceObject.optJSONArray("cityList");
            parseCity(provinceEntity, cityArray);
            data.add(provinceEntity);
        }
        return data;
    }

    private static void parseCity(ProvinceEntity provinceEntity, JSONArray cityArray) {
        //台湾的第二级可能没数据
        if (cityArray == null || cityArray.length() == 0) {
            return;
        }
        for (int j = 0, y = cityArray.length(); j < y; j++) {
            CityEntity cityEntity = new CityEntity();
            JSONObject cityObject = cityArray.optJSONObject(j);
            cityEntity.setCode(cityObject.optString("code"));
            cityEntity.setName(cityObject.optString("name"));
            cityEntity.setCountyList(new ArrayList<>());
            provinceEntity.getCityList().add(cityEntity);
            JSONArray countyArray = cityObject.optJSONArray("areaList");
            parseCounty(cityEntity, countyArray);
        }
    }

    private static void parseCounty(CityEntity cityEntity, JSONArray countyArray) {
        //港澳台的第三级可能没数据
        if (countyArray == null || countyArray.length() == 0) {
            return;
        }
        for (int k = 0, z = countyArray.length(); k < z; k++) {
            CountyEntity countyEntity = new CountyEntity();
            JSONObject countyObject = countyArray.optJSONObject(k);
            countyEntity.setCode(countyObject.optString("code"));
            countyEntity.setName(countyObject.optString("name"));
            cityEntity.getCountyList().add(countyEntity);
        }
    }

}
