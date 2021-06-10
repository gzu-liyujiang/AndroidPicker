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

import com.github.gzuliyujiang.wheelpicker.entity.PhoneCodeEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 手机号前缀选择器
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2019/5/10 16:44
 */
@SuppressWarnings("unused")
public class PhoneCodePicker extends OptionPicker {

    public PhoneCodePicker(@NonNull Activity activity) {
        super(activity);
    }

    public PhoneCodePicker(@NonNull Activity activity, @StyleRes int themeResId) {
        super(activity, themeResId);
    }

    @Override
    protected List<?> provideData() {
        List<PhoneCodeEntity> data = new ArrayList<>();
        data.add(new PhoneCodeEntity("大陆+86", "+86"));
        data.add(new PhoneCodeEntity("香港+852", "+852"));
        data.add(new PhoneCodeEntity("澳门+853", "+853"));
        data.add(new PhoneCodeEntity("台湾+886", "+886"));
        return data;
    }

}
