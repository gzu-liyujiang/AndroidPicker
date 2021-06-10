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
 * 日期模式
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2019/5/14 17:10
 */
@Retention(RetentionPolicy.SOURCE)
public @interface DateMode {
    /**
     * 不显示
     */
    int NONE = -1;
    /**
     * 年月日
     */
    int YEAR_MONTH_DAY = 0;
    /**
     * 年月
     */
    int YEAR_MONTH = 1;
    /**
     * 月日
     */
    int MONTH_DAY = 2;
}
