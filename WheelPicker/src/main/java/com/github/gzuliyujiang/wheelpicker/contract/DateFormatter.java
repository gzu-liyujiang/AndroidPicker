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

/**
 * 日期显示文本格式化接口
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2019/5/14 19:55
 */
public interface DateFormatter {

    /**
     * 格式化年份
     *
     * @param year 年份数字
     * @return 格式化后最终显示的年份字符串
     */
    String formatYear(int year);

    /**
     * 格式化月份
     *
     * @param month 月份数字
     * @return 格式化后最终显示的月份字符串
     */
    String formatMonth(int month);

    /**
     * 格式化日子
     *
     * @param day 日子数字
     * @return 格式化后最终显示的日子字符串
     */
    String formatDay(int day);

}

