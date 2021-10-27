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

package com.github.gzuliyujiang.wheelpicker.impl;

import androidx.annotation.NonNull;

import com.github.gzuliyujiang.wheelpicker.contract.LinkageProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 数据参见 http://www.360doc.com/content/12/0602/07/3899427_215339300.shtml
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2021/6/9 11:31
 */
public class CarPlateProvider implements LinkageProvider {
    private static final String[] ABBREVIATIONS = {
            "京", "津", "冀", "晋", "蒙", "辽", "吉", "黑", "沪",
            "苏", "浙", "皖", "闽", "赣", "鲁", "豫", "鄂", "湘",
            "粤", "桂", "琼", "渝", "川", "贵", "云", "藏", "陕",
            "甘", "青", "宁", "新"};

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
        List<String> provinces = new ArrayList<>();
        Collections.addAll(provinces, ABBREVIATIONS);
        return provinces;
    }

    @NonNull
    @Override
    public List<String> linkageSecondData(int firstIndex) {
        List<String> letters = new ArrayList<>();
        if (firstIndex == INDEX_NO_FOUND) {
            firstIndex = 0;
        }
        String province = provideFirstData().get(firstIndex);
        switch (province) {
            case "京":
                for (char i = 'A'; i <= 'M'; i++) {
                    letters.add(String.valueOf(i));
                }
                letters.remove("I");
                letters.add("Y");
                break;
            case "津":
            case "青":
                for (char i = 'A'; i <= 'H'; i++) {
                    letters.add(String.valueOf(i));
                }
                break;
            case "冀":
                for (char i = 'A'; i <= 'H'; i++) {
                    letters.add(String.valueOf(i));
                }
                letters.add("J");
                letters.add("R");
                letters.add("S");
                letters.add("T");
                break;
            case "晋":
                for (char i = 'A'; i <= 'M'; i++) {
                    letters.add(String.valueOf(i));
                }
                letters.remove("G");
                letters.remove("I");
                break;
            case "蒙":
            case "赣":
                for (char i = 'A'; i <= 'M'; i++) {
                    letters.add(String.valueOf(i));
                }
                letters.remove("I");
                break;
            case "辽":
            case "甘":
                for (char i = 'A'; i <= 'P'; i++) {
                    letters.add(String.valueOf(i));
                }
                letters.remove("I");
                letters.remove("O");
                break;
            case "吉":
            case "闽":
                for (char i = 'A'; i <= 'K'; i++) {
                    letters.add(String.valueOf(i));
                }
                letters.remove("I");
                break;
            case "黑":
            case "新":
                for (char i = 'A'; i <= 'R'; i++) {
                    letters.add(String.valueOf(i));
                }
                letters.remove("I");
                letters.remove("O");
                break;
            case "沪":
                for (char i = 'A'; i <= 'D'; i++) {
                    letters.add(String.valueOf(i));
                }
                letters.add("R");
                break;
            case "苏":
                for (char i = 'A'; i <= 'N'; i++) {
                    letters.add(String.valueOf(i));
                }
                letters.remove("I");
                break;
            case "浙":
                for (char i = 'A'; i <= 'L'; i++) {
                    letters.add(String.valueOf(i));
                }
                letters.remove("I");
                break;
            case "皖":
            case "鄂":
                for (char i = 'A'; i <= 'S'; i++) {
                    letters.add(String.valueOf(i));
                }
                letters.remove("I");
                letters.remove("O");
                break;
            case "鲁":
                for (char i = 'A'; i <= 'V'; i++) {
                    letters.add(String.valueOf(i));
                }
                letters.remove("I");
                letters.remove("O");
                letters.add("Y");
                break;
            case "豫":
                for (char i = 'A'; i <= 'U'; i++) {
                    letters.add(String.valueOf(i));
                }
                letters.remove("I");
                letters.remove("O");
                break;
            case "湘":
                for (char i = 'A'; i <= 'N'; i++) {
                    letters.add(String.valueOf(i));
                }
                letters.remove("I");
                letters.remove("O");
                letters.add("U");
                break;
            case "粤":
                for (char i = 'A'; i <= 'Z'; i++) {
                    letters.add(String.valueOf(i));
                }
                letters.remove("I");
                letters.remove("O");
                break;
            case "桂":
                for (char i = 'A'; i <= 'P'; i++) {
                    letters.add(String.valueOf(i));
                }
                letters.remove("I");
                letters.remove("O");
                letters.add("R");
                break;
            case "琼":
            case "宁":
                for (char i = 'A'; i <= 'E'; i++) {
                    letters.add(String.valueOf(i));
                }
                break;
            case "渝":
                for (char i = 'A'; i <= 'D'; i++) {
                    letters.add(String.valueOf(i));
                }
                letters.remove("D");
                letters.remove("E");
                break;
            case "川":
                for (char i = 'A'; i <= 'Z'; i++) {
                    letters.add(String.valueOf(i));
                }
                letters.remove("G");
                letters.remove("I");
                letters.remove("O");
                break;
            case "贵":
            case "藏":
                for (char i = 'A'; i <= 'J'; i++) {
                    letters.add(String.valueOf(i));
                }
                letters.remove("I");
                break;
            case "云":
                // “A-V”为昆明市东川区（原东川市）
                letters.add("A-V");
                for (char i = 'A'; i <= 'S'; i++) {
                    letters.add(String.valueOf(i));
                }
                letters.remove("B");
                letters.remove("I");
                letters.remove("O");
                break;
            case "陕":
                for (char i = 'A'; i <= 'K'; i++) {
                    letters.add(String.valueOf(i));
                }
                letters.remove("I");
                letters.add("V");
                break;
        }
        return letters;
    }

    @NonNull
    @Override
    public List<?> linkageThirdData(int firstIndex, int secondIndex) {
        return new ArrayList<>();
    }

    @Override
    public int findFirstIndex(Object firstValue) {
        if (firstValue == null) {
            return INDEX_NO_FOUND;
        }
        for (int i = 0, n = ABBREVIATIONS.length; i < n; i++) {
            String abbreviation = ABBREVIATIONS[i];
            if (abbreviation.equals(firstValue.toString())) {
                return i;
            }
        }
        return INDEX_NO_FOUND;
    }

    @Override
    public int findSecondIndex(int firstIndex, Object secondValue) {
        if (secondValue == null) {
            return INDEX_NO_FOUND;
        }
        List<String> letters = linkageSecondData(firstIndex);
        for (int i = 0, n = letters.size(); i < n; i++) {
            String letter = letters.get(i);
            if (letter.equals(secondValue.toString())) {
                return i;
            }
        }
        return INDEX_NO_FOUND;
    }

    @Override
    public int findThirdIndex(int firstIndex, int secondIndex, Object thirdValue) {
        return 0;
    }

}
