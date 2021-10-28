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
import com.github.gzuliyujiang.wheelpicker.entity.SexEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 性别选择器
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2019/6/23 11:48
 */
@SuppressWarnings("WeakerAccess")
public class SexPicker extends OptionPicker {
    public static String JSON = "[{\"id\":0,\"name\":\"保密\",\"english\":\"Secrecy\"},\n" +
            "{\"id\":1,\"name\":\"男\",\"english\":\"Male\"},\n" +
            "{\"id\":2,\"name\":\"女\",\"english\":\"Female\"}]";
    private boolean includeSecrecy;

    public SexPicker(Activity activity) {
        super(activity);
    }

    public SexPicker(@NonNull Activity activity, @StyleRes int themeResId) {
        super(activity, themeResId);
    }

    public void setIncludeSecrecy(boolean includeSecrecy) {
        this.includeSecrecy = includeSecrecy;
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

    public void setDefaultValueByName(String name) {
        SexEntity entity = new SexEntity();
        entity.setName(name);
        super.setDefaultValue(entity);
    }

    public void setDefaultValueByEnglish(String english) {
        SexEntity entity = new SexEntity();
        entity.setEnglish(english);
        super.setDefaultValue(entity);
    }

    @Override
    protected List<?> provideData() {
        ArrayList<SexEntity> data = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(JSON);
            for (int i = 0, n = jsonArray.length(); i < n; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                SexEntity entity = new SexEntity();
                entity.setId(jsonObject.getString("id"));
                entity.setName(jsonObject.getString("name"));
                entity.setEnglish(jsonObject.getString("english"));
                if (!includeSecrecy && "0".equals(entity.getId())) {
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
