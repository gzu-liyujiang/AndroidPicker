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

import com.github.gzuliyujiang.wheelpicker.contract.DateFormatter;

/**
 * 带单位的日期格式化
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2021/6/4 11:33
 */
public class UnitDateFormatter implements DateFormatter {

    @Override
    public String formatYear(int year) {
        return year + "年";
    }

    @Override
    public String formatMonth(int month) {
        return month + "月";
    }

    @Override
    public String formatDay(int day) {
        return day + "日";
    }

}
