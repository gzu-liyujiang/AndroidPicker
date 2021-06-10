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

package com.github.gzuliyujiang.wheelpicker.entity;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 市级数据实体
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2019/6/17 11:47
 */
public class CityEntity extends AddressEntity {
    private List<CountyEntity> countyList;

    @NonNull
    public List<CountyEntity> getCountyList() {
        if (countyList == null) {
            countyList = new ArrayList<>();
        }
        return countyList;
    }

    public void setCountyList(List<CountyEntity> countyList) {
        this.countyList = countyList;
    }

}
