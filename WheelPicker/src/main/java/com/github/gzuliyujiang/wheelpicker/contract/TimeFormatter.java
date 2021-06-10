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
 * 时间显示文本格式化接口
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2019/5/14 19:55
 */
public interface TimeFormatter {

    /**
     * 格式化小时数
     *
     * @param hour 小时数
     * @return 格式化后最终显示的小时数字符串
     */
    String formatHour(int hour);

    /**
     * 格式化分钟数
     *
     * @param minute 分钟数
     * @return 格式化后最终显示的分钟数字符串
     */
    String formatMinute(int minute);

    /**
     * 格式化秒数
     *
     * @param second 秒数
     * @return 格式化后最终显示的秒数字符串
     */
    String formatSecond(int second);

}
