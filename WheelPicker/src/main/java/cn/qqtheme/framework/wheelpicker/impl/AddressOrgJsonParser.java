package cn.qqtheme.framework.wheelpicker.impl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.qqtheme.framework.logger.CqrLog;
import cn.qqtheme.framework.wheelpicker.entity.CityEntity;
import cn.qqtheme.framework.wheelpicker.entity.CountyEntity;
import cn.qqtheme.framework.wheelpicker.entity.ProvinceEntity;
import cn.qqtheme.framework.wheelpicker.contract.AddressJsonParser;

/**
 * 使用SDK自带的“org.json”包解析地址数据
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/6/17 17:15
 */
public class AddressOrgJsonParser implements AddressJsonParser {

    @Override
    public List<ProvinceEntity> parseJson(String json) {
        try {
            JSONArray provinceArray = new JSONArray(json);
            return parseProvince(provinceArray);
        } catch (JSONException e) {
            CqrLog.e(e);
        }
        return new ArrayList<>();
    }

    private List<ProvinceEntity> parseProvince(JSONArray provinceArray) {
        List<ProvinceEntity> data = new ArrayList<>();
        for (int i = 0, x = provinceArray.length(); i < x; i++) {
            ProvinceEntity provinceEntity = new ProvinceEntity();
            JSONObject provinceObject = provinceArray.optJSONObject(i);
            provinceEntity.setCode(provinceObject.optString("code"));
            provinceEntity.setName(provinceObject.optString("name"));
            provinceEntity.setCityList(new ArrayList<CityEntity>());
            JSONArray cityArray = provinceObject.optJSONArray("cityList");
            parseCity(provinceEntity, cityArray);
            data.add(provinceEntity);
        }
        return data;
    }

    private void parseCity(ProvinceEntity provinceEntity, JSONArray cityArray) {
        //台湾的第二级可能没数据
        if (cityArray != null) {
            for (int j = 0, y = cityArray.length(); j < y; j++) {
                CityEntity cityEntity = new CityEntity();
                JSONObject cityObject = cityArray.optJSONObject(j);
                cityEntity.setCode(cityObject.optString("code"));
                cityEntity.setName(cityObject.optString("name"));
                cityEntity.setAreaList(new ArrayList<CountyEntity>());
                provinceEntity.getCityList().add(cityEntity);
                JSONArray countyArray = cityObject.optJSONArray("areaList");
                parseCounty(cityEntity, countyArray);
            }
        } else {
            CityEntity cityEntity = new CityEntity();
            cityEntity.setCode("");
            cityEntity.setName("-");
            provinceEntity.getCityList().add(cityEntity);
        }
    }

    private void parseCounty(CityEntity cityEntity, JSONArray countyArray) {
        //港澳台的第三级可能没数据
        if (countyArray != null) {
            for (int k = 0, z = countyArray.length(); k < z; k++) {
                CountyEntity countyEntity = new CountyEntity();
                JSONObject countyObject = countyArray.optJSONObject(k);
                countyEntity.setCode(countyObject.optString("code"));
                countyEntity.setName(countyObject.optString("name"));
                cityEntity.getAreaList().add(countyEntity);
            }
        } else {
            CountyEntity countyEntity = new CountyEntity();
            countyEntity.setCode("");
            countyEntity.setName("-");
            cityEntity.getAreaList().add(countyEntity);
        }
    }

}
