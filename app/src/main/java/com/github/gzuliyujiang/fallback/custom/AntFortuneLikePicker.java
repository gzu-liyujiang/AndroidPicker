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

package com.github.gzuliyujiang.fallback.custom;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.github.gzuliyujiang.wheelpicker.LinkagePicker;

/**
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2021/6/7 12:20
 */
public class AntFortuneLikePicker extends LinkagePicker {

    public AntFortuneLikePicker(@NonNull Activity activity) {
        super(activity);
    }

    @Override
    protected void initData() {
        super.initData();
        enableRoundCorner();
        cancelView.setText("取消");
        cancelView.setTextColor(0xFF3355E5);
        okView.setTextColor(0xFF3355E5);
        okView.setText("确定");
        titleView.setTextColor(0xFF333333);
        titleView.setText("定投周期");
        wheelLayout.setData(new AntFortuneLikeProvider());
        wheelLayout.setAtmosphericEnabled(true);
        wheelLayout.setVisibleItemCount(7);
        wheelLayout.setCyclicEnabled(false);
        wheelLayout.setIndicatorEnabled(true);
        wheelLayout.setIndicatorColor(0xFFDCDCDC);
        wheelLayout.setIndicatorSize((int) (contentView.getResources().getDisplayMetrics().density * 0.6f));
        wheelLayout.setTextColor(0xFF999999);
        wheelLayout.setSelectedTextColor(0xFF333333);
        wheelLayout.setCurtainEnabled(false);
        wheelLayout.setCurvedEnabled(false);
    }

}
