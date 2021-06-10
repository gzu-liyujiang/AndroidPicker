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

package com.github.gzuliyujiang.wheelpicker.contract;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * 提供二级或三级联动数据
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2019/6/17 11:27
 */
public interface LinkageProvider {
    int INDEX_NO_FOUND = -1;

    /**
     * 是否展示第一级
     *
     * @return 返回true表示展示第一级
     */
    boolean firstLevelVisible();

    /**
     * 是否展示第三级
     *
     * @return 返回true表示展示第三级
     */
    boolean thirdLevelVisible();

    /**
     * 提供第一级数据
     *
     * @return 第一级数据
     */
    @NonNull
    List<?> provideFirstData();

    /**
     * 根据第一级数据联动第二级数据
     *
     * @param firstIndex 第一级数据索引
     * @return 第二级数据
     */
    @NonNull
    List<?> linkageSecondData(int firstIndex);

    /**
     * 根据第一二级数据联动第三级数据
     *
     * @param firstIndex  第一级数据索引
     * @param secondIndex 第二级数据索引
     * @return 第三级数据
     */
    @NonNull
    List<?> linkageThirdData(int firstIndex, int secondIndex);

    /**
     * 根据第一数据值查找其索引
     *
     * @param firstValue 第一级数据值
     * @return 第一级数据索引
     */
    int findFirstIndex(Object firstValue);

    /**
     * 根据第二数据值查找其索引
     *
     * @param firstIndex  第一级数据索引
     * @param secondValue 第二级数据值
     * @return 第二级数据索引
     */
    int findSecondIndex(int firstIndex, Object secondValue);

    /**
     * 根据第三数据值查找其索引
     *
     * @param firstIndex  第一级数据索引
     * @param secondIndex 第二级数据索引
     * @param thirdValue  第三级数据值
     * @return 第三级数据索引
     */
    int findThirdIndex(int firstIndex, int secondIndex, Object thirdValue);

}