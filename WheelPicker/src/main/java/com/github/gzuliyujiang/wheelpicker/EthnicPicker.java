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

import com.github.gzuliyujiang.dialog.DialogLog;
import com.github.gzuliyujiang.wheelpicker.annotation.EthnicSpec;
import com.github.gzuliyujiang.wheelpicker.entity.EthnicEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 民族选择器
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2021/6/12 13:50
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class EthnicPicker extends OptionPicker {
    public static String JSON = "[{\"code\":\"01\",\"name\":\"汉族\",\"spelling\":\"Han\"}," +
            "{\"code\":\"02\",\"name\":\"蒙古族\",\"spelling\":\"Mongol\"}," +
            "{\"code\":\"03\",\"name\":\"回族\",\"spelling\":\"Hui\"}," +
            "{\"code\":\"04\",\"name\":\"藏族\",\"spelling\":\"Zang\"}," +
            "{\"code\":\"05\",\"name\":\"维吾尔族\",\"spelling\":\"Uygur\"}," +
            "{\"code\":\"06\",\"name\":\"苗族\",\"spelling\":\"Miao\"}," +
            "{\"code\":\"07\",\"name\":\"彝族\",\"spelling\":\"Yi\"}," +
            "{\"code\":\"08\",\"name\":\"壮族\",\"spelling\":\"Zhuang\"}," +
            "{\"code\":\"09\",\"name\":\"布依族\",\"spelling\":\"Buyei\"}," +
            "{\"code\":\"10\",\"name\":\"朝鲜族\",\"spelling\":\"Chosen\"}," +
            "{\"code\":\"11\",\"name\":\"满族\",\"spelling\":\"Man\"}," +
            "{\"code\":\"12\",\"name\":\"侗族\",\"spelling\":\"Dong\"}," +
            "{\"code\":\"13\",\"name\":\"瑶族\",\"spelling\":\"Yao\"}," +
            "{\"code\":\"14\",\"name\":\"白族\",\"spelling\":\"Bai\"}," +
            "{\"code\":\"15\",\"name\":\"土家族\",\"spelling\":\"Tujia\"}," +
            "{\"code\":\"16\",\"name\":\"哈尼族\",\"spelling\":\"Hani\"}," +
            "{\"code\":\"17\",\"name\":\"哈萨克族\",\"spelling\":\"Kazak\"}," +
            "{\"code\":\"18\",\"name\":\"傣族\",\"spelling\":\"Dai\"}," +
            "{\"code\":\"19\",\"name\":\"黎族\",\"spelling\":\"Li\"}," +
            "{\"code\":\"20\",\"name\":\"傈僳族\",\"spelling\":\"Lisu\"}," +
            "{\"code\":\"21\",\"name\":\"佤族\",\"spelling\":\"Va\"}," +
            "{\"code\":\"22\",\"name\":\"畲族\",\"spelling\":\"She\"}," +
            "{\"code\":\"23\",\"name\":\"高山族\",\"spelling\":\"Gaoshan\"}," +
            "{\"code\":\"24\",\"name\":\"拉祜族\",\"spelling\":\"Lahu\"}," +
            "{\"code\":\"25\",\"name\":\"水族\",\"spelling\":\"Sui\"}," +
            "{\"code\":\"26\",\"name\":\"东乡族\",\"spelling\":\"Dongxiang\"}," +
            "{\"code\":\"27\",\"name\":\"纳西族\",\"spelling\":\"Naxi\"}," +
            "{\"code\":\"28\",\"name\":\"景颇族\",\"spelling\":\"Jingpo\"}," +
            "{\"code\":\"29\",\"name\":\"柯尔克孜族\",\"spelling\":\"Kirgiz\"}," +
            "{\"code\":\"30\",\"name\":\"土族\",\"spelling\":\"Tu\"}," +
            "{\"code\":\"31\",\"name\":\"达斡尔族\",\"spelling\":\"Daur\"}," +
            "{\"code\":\"32\",\"name\":\"仫佬族\",\"spelling\":\"Mulao\"}," +
            "{\"code\":\"33\",\"name\":\"羌族\",\"spelling\":\"Qiang\"}," +
            "{\"code\":\"34\",\"name\":\"布朗族\",\"spelling\":\"Blang\"}," +
            "{\"code\":\"35\",\"name\":\"撒拉族\",\"spelling\":\"Salar\"}," +
            "{\"code\":\"36\",\"name\":\"毛难族\",\"spelling\":\"Maonan\"}," +
            "{\"code\":\"37\",\"name\":\"仡佬族\",\"spelling\":\"Gelao\"}," +
            "{\"code\":\"38\",\"name\":\"锡伯族\",\"spelling\":\"Xibe\"}," +
            "{\"code\":\"39\",\"name\":\"阿昌族\",\"spelling\":\"Achang\"}," +
            "{\"code\":\"40\",\"name\":\"普米族\",\"spelling\":\"Pumi\"}," +
            "{\"code\":\"41\",\"name\":\"塔吉克族\",\"spelling\":\"Tajik\"}," +
            "{\"code\":\"42\",\"name\":\"怒族\",\"spelling\":\"Nu\"}," +
            "{\"code\":\"43\",\"name\":\"乌孜别克族\",\"spelling\":\"Uzbek\"}," +
            "{\"code\":\"44\",\"name\":\"俄罗斯族\",\"spelling\":\"Russ\"}," +
            "{\"code\":\"45\",\"name\":\"鄂温克族\",\"spelling\":\"Ewenki\"}," +
            "{\"code\":\"46\",\"name\":\"德昂族\",\"spelling\":\"Deang\"}," +
            "{\"code\":\"47\",\"name\":\"保安族\",\"spelling\":\"Bonan\"}," +
            "{\"code\":\"48\",\"name\":\"裕固族\",\"spelling\":\"Yugur\"}," +
            "{\"code\":\"49\",\"name\":\"京族\",\"spelling\":\"Gin\"}," +
            "{\"code\":\"50\",\"name\":\"塔塔尔族\",\"spelling\":\"Tatar\"}," +
            "{\"code\":\"51\",\"name\":\"独龙族\",\"spelling\":\"Derung\"}," +
            "{\"code\":\"52\",\"name\":\"鄂伦春族\",\"spelling\":\"Oroqen\"}," +
            "{\"code\":\"53\",\"name\":\"赫哲族\",\"spelling\":\"Hezhen\"}," +
            "{\"code\":\"54\",\"name\":\"门巴族\",\"spelling\":\"Monba\"}," +
            "{\"code\":\"55\",\"name\":\"珞巴族\",\"spelling\":\"Lhoba\"}," +
            "{\"code\":\"56\",\"name\":\"基诺族\",\"spelling\":\"Jino\"}]";
    private int ethnicSpec = EthnicSpec.DEFAULT;

    public EthnicPicker(@NonNull Activity activity) {
        super(activity);
    }

    public EthnicPicker(@NonNull Activity activity, int themeResId) {
        super(activity, themeResId);
    }

    public void setEthnicSpec(@EthnicSpec int ethnicSpec) {
        this.ethnicSpec = ethnicSpec;
        setData(provideData());
    }

    @Override
    public void setDefaultValue(Object item) {
        if (item instanceof String) {
            setDefaultValueByName(item.toString());
        } else {
            super.setDefaultValue(item);
        }
    }

    public void setDefaultValueByCode(String code) {
        EthnicEntity entity = new EthnicEntity();
        entity.setCode(code);
        super.setDefaultValue(entity);
    }

    public void setDefaultValueByName(String name) {
        EthnicEntity entity = new EthnicEntity();
        entity.setName(name);
        super.setDefaultValue(entity);
    }

    public void setDefaultValueBySpelling(String spelling) {
        EthnicEntity entity = new EthnicEntity();
        entity.setSpelling(spelling);
        super.setDefaultValue(entity);
    }

    @Override
    protected List<EthnicEntity> provideData() {
        ArrayList<EthnicEntity> data = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(JSON);
            for (int i = 0, n = jsonArray.length(); i < n; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                EthnicEntity entity = new EthnicEntity();
                entity.setCode(jsonObject.getString("code"));
                entity.setName(jsonObject.getString("name"));
                entity.setSpelling(jsonObject.getString("spelling"));
                data.add(entity);
            }
        } catch (JSONException e) {
            DialogLog.print(e);
        }
        switch (ethnicSpec) {
            case EthnicSpec.DEFAULT:
                EthnicEntity other = new EthnicEntity();
                other.setCode("97");
                other.setName("其他");
                other.setSpelling("Other");
                data.add(other);
                EthnicEntity foreign = new EthnicEntity();
                foreign.setCode("98");
                foreign.setName("外国血统");
                foreign.setSpelling("Foreign");
                data.add(foreign);
                break;
            case EthnicSpec.SEVENTH_NATIONAL_CENSUS:
                EthnicEntity unrecognized = new EthnicEntity();
                unrecognized.setCode("97");
                unrecognized.setName("未定族称人口");
                unrecognized.setSpelling("Unrecognized");
                data.add(unrecognized);
                EthnicEntity naturalization = new EthnicEntity();
                naturalization.setCode("98");
                naturalization.setName("入籍");
                naturalization.setSpelling("Naturalization");
                data.add(naturalization);
                break;
            default:
                break;
        }
        return data;
    }

}
