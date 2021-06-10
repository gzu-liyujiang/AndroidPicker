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

package com.github.gzuliyujiang.wheelpicker.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 地址模式
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2019/6/15 12:19
 */
@Retention(RetentionPolicy.SOURCE)
public @interface AddressMode {
    /**
     * 省级、市级、县级
     */
    int PROVINCE_CITY_COUNTY = 0;
    /**
     * 省级、市级
     */
    int PROVINCE_CITY = 1;
    /**
     * 市级、县级
     */
    int CITY_COUNTY = 2;
}
