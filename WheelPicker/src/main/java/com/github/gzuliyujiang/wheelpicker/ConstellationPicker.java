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

import com.github.gzuliyujiang.dialog.DialogLog;
import com.github.gzuliyujiang.wheelpicker.entity.ConstellationEntity;
import com.github.gzuliyujiang.wheelpicker.entity.DateEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 星座选择器
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2019/5/17 13:10
 */
@SuppressWarnings("WeakerAccess")
public class ConstellationPicker extends OptionPicker {
    public static String JSON = "[{\"id\":0,\"name\":\"不限\",\"startDate\":\"\",\"endDate\":\"\",\"english\":\"Unlimited\"},\n" +
            "{\"id\":1,\"name\":\"白羊座\",\"startDate\":\"3-21\",\"endDate\":\"4-19\",\"english\":\"Aries\"},\n" +
            "{\"id\":2,\"name\":\"金牛座\",\"startDate\":\"4-20\",\"endDate\":\"5-20\",\"english\":\"Taurus\"},\n" +
            "{\"id\":3,\"name\":\"双子座\",\"startDate\":\"5-21\",\"endDate\":\"6-21\",\"english\":\"Gemini\"},\n" +
            "{\"id\":4,\"name\":\"巨蟹座\",\"startDate\":\"6-22\",\"endDate\":\"7-22\",\"english\":\"Cancer\"},\n" +
            "{\"id\":5,\"name\":\"狮子座\",\"startDate\":\"7-23\",\"endDate\":\"8-22\",\"english\":\"Leo\"},\n" +
            "{\"id\":6,\"name\":\"处女座\",\"startDate\":\"8-23\",\"endDate\":\"9-22\",\"english\":\"Virgo\"},\n" +
            "{\"id\":7,\"name\":\"天秤座\",\"startDate\":\"9-23\",\"endDate\":\"10-23\",\"english\":\"Libra\"},\n" +
            "{\"id\":8,\"name\":\"天蝎座\",\"startDate\":\"10-24\",\"endDate\":\"11-22\",\"english\":\"Scorpio\"},\n" +
            "{\"id\":9,\"name\":\"射手座\",\"startDate\":\"11-23\",\"endDate\":\"12-21\",\"english\":\"Sagittarius\"},\n" +
            "{\"id\":10,\"name\":\"摩羯座\",\"startDate\":\"12-22\",\"endDate\":\"1-19\",\"english\":\"Capricorn\"},\n" +
            "{\"id\":11,\"name\":\"水瓶座\",\"startDate\":\"1-20\",\"endDate\":\"2-18\",\"english\":\"Aquarius\"},\n" +
            "{\"id\":12,\"name\":\"双鱼座\",\"startDate\":\"2-19\",\"endDate\":\"3-20\",\"english\":\"Pisces\"}]";
    private boolean includeUnlimited = false;

    public ConstellationPicker(Activity activity) {
        super(activity);
    }

    public ConstellationPicker(@NonNull Activity activity, @StyleRes int themeResId) {
        super(activity, themeResId);
    }

    public void setIncludeUnlimited(boolean includeUnlimited) {
        this.includeUnlimited = includeUnlimited;
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

    public void setDefaultValueById(String id) {
        ConstellationEntity entity = new ConstellationEntity();
        entity.setId(id);
        super.setDefaultValue(entity);
    }

    public void setDefaultValueByName(String name) {
        ConstellationEntity entity = new ConstellationEntity();
        entity.setName(name);
        super.setDefaultValue(entity);
    }

    public void setDefaultValueByDate(DateEntity date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.toTimeInMillis());
        setDefaultValueByDate(calendar.getTime());
    }

    public void setDefaultValueByDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String name;
        switch (month) {
            case 1:
                name = day < 21 ? "摩羯座" : "水瓶座";
                break;
            case 2:
                name = day < 20 ? "水瓶座" : "双鱼座";
                break;
            case 3:
                name = day < 21 ? "双鱼座" : "白羊座";
                break;
            case 4:
                name = day < 21 ? "白羊座" : "金牛座";
                break;
            case 5:
                name = day < 22 ? "金牛座" : "双子座";
                break;
            case 6:
                name = day < 22 ? "双子座" : "巨蟹座";
                break;
            case 7:
                name = day < 23 ? "巨蟹座" : "狮子座";
                break;
            case 8:
                name = day < 24 ? "狮子座" : "处女座";
                break;
            case 9:
                name = day < 24 ? "处女座" : "天秤座";
                break;
            case 10:
                name = day < 24 ? "天秤座" : "天蝎座";
                break;
            case 11:
                name = day < 23 ? "天蝎座" : "射手座";
                break;
            case 12:
                name = day < 22 ? "射手座" : "摩羯座";
                break;
            default:
                name = "不限";
                break;
        }
        setDefaultValueByName(name);
    }

    public void setDefaultValueByEnglish(String english) {
        ConstellationEntity entity = new ConstellationEntity();
        entity.setEnglish(english);
        super.setDefaultValue(entity);
    }

    @Override
    protected List<?> provideData() {
        ArrayList<ConstellationEntity> data = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(JSON);
            for (int i = 0, n = jsonArray.length(); i < n; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ConstellationEntity entity = new ConstellationEntity();
                entity.setId(jsonObject.getString("id"));
                entity.setStartDate(jsonObject.getString("startDate"));
                entity.setEndDate(jsonObject.getString("endDate"));
                entity.setName(jsonObject.getString("name"));
                entity.setEnglish(jsonObject.getString("english"));
                if (!includeUnlimited && "0".equals(entity.getId())) {
                    continue;
                }
                data.add(entity);
            }
        } catch (JSONException e) {
            DialogLog.print(e);
        }
        return data;
    }

}
