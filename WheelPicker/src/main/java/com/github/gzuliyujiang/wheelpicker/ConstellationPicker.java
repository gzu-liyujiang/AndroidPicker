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

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * 星座选择器
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2019/5/17 13:10
 */
@SuppressWarnings("WeakerAccess")
public class ConstellationPicker extends OptionPicker {
    public static final List<String> DATA_CN = Arrays.asList(
            "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座",
            "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座"
    );
    public static final List<String> DATA_EN = Arrays.asList(
            "Aquarius", "Pisces", "Aries", "Taurus", "Gemini", "Cancer",
            "Leo", "Virgo", "Libra", "Scorpio", "Sagittarius", "Capricorn"
    );
    private final boolean includeUnlimited;

    public ConstellationPicker(Activity activity) {
        this(activity, false);
    }

    public ConstellationPicker(@NonNull Activity activity, @StyleRes int themeResId) {
        super(activity, themeResId);
        this.includeUnlimited = false;
    }

    public ConstellationPicker(@NonNull Activity activity, @StyleRes int themeResId, boolean includeUnlimited) {
        super(activity, themeResId);
        this.includeUnlimited = includeUnlimited;
    }

    public ConstellationPicker(Activity activity, boolean includeUnlimited) {
        super(activity);
        this.includeUnlimited = includeUnlimited;
    }

    @Override
    protected void initData() {
        super.initData();
        titleView.setText("星座选择");
    }

    @Override
    protected List<?> provideData() {
        boolean isChinese = Locale.getDefault().getDisplayLanguage().contains("中文");
        LinkedList<String> data = new LinkedList<>();
        if (isChinese) {
            data.addAll(DATA_CN);
            if (includeUnlimited) {
                data.addFirst("不限");
            }
        } else {
            data.addAll(DATA_EN);
            if (includeUnlimited) {
                data.addFirst("Unlimited");
            }
        }
        return data;
    }

}
