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

import androidx.annotation.NonNull;

import com.github.gzuliyujiang.wheelpicker.contract.LinkageProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2021/6/7 12:22
 */
public class AntFortuneLikeProvider implements LinkageProvider {

    @Override
    public boolean firstLevelVisible() {
        return true;
    }

    @Override
    public boolean thirdLevelVisible() {
        return false;
    }

    @NonNull
    @Override
    public List<String> provideFirstData() {
        return Arrays.asList("每周", "每两周", "每月", "每日");
    }

    @NonNull
    @Override
    public List<String> linkageSecondData(int firstIndex) {
        switch (firstIndex) {
            case 0:
            case 1:
                return Arrays.asList("周一", "周二", "周三", "周四", "周五");
            case 2:
                List<String> data = new ArrayList<>();
                for (int i = 1; i <= 28; i++) {
                    data.add(i + "日");
                }
                return data;
        }
        return new ArrayList<>();
    }

    @NonNull
    @Override
    public List<String> linkageThirdData(int firstIndex, int secondIndex) {
        return new ArrayList<>();
    }

    @Override
    public int findFirstIndex(Object firstValue) {
        return 0;
    }

    @Override
    public int findSecondIndex(int firstIndex, Object secondValue) {
        return 0;
    }

    @Override
    public int findThirdIndex(int firstIndex, int secondIndex, Object thirdValue) {
        return 0;
    }

}
