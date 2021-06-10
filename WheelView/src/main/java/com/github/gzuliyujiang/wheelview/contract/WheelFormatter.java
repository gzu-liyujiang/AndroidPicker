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

package com.github.gzuliyujiang.wheelview.contract;

import androidx.annotation.NonNull;

/**
 * 滚轮条目显示文本格式化接口
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2019/5/14 20:02
 */
public interface WheelFormatter {

    /**
     * 格式化滚轮条目显示文本
     *
     * @param item 滚轮条目的内容
     * @return 格式化后最终显示的文本
     */
    String formatItem(@NonNull Object item);

}
