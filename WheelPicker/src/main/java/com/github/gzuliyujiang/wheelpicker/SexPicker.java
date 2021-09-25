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
 * 性别选择器
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2019/6/23 11:48
 */
@SuppressWarnings("WeakerAccess")
public class SexPicker extends OptionPicker {
    public static final List<String> DATA_CN = Arrays.asList(
            "男", "女"
    );
    public static final List<String> DATA_EN = Arrays.asList(
            "Male", "Female"
    );
    private final boolean includeSecrecy;

    public SexPicker(Activity activity) {
        this(activity, false);
    }

    public SexPicker(@NonNull Activity activity, @StyleRes int themeResId) {
        super(activity, themeResId);
        this.includeSecrecy = false;
    }

    public SexPicker(Activity activity, boolean includeSecrecy) {
        super(activity);
        this.includeSecrecy = includeSecrecy;
    }

    public SexPicker(@NonNull Activity activity, @StyleRes int themeResId, boolean includeSecrecy) {
        super(activity, themeResId);
        this.includeSecrecy = includeSecrecy;
    }

    @Override
    protected void initView() {
        super.initView();
        titleView.setText("性别选择");
    }

    @Override
    protected List<?> provideData() {
        boolean isChinese = Locale.getDefault().getDisplayLanguage().contains("中文");
        LinkedList<String> data = new LinkedList<>();
        if (isChinese) {
            data.addAll(DATA_CN);
            if (includeSecrecy) {
                data.addFirst("保密");
            }
        } else {
            data.addAll(DATA_EN);
            if (includeSecrecy) {
                data.addFirst("Unlimited");
            }
        }
        return data;
    }

}
