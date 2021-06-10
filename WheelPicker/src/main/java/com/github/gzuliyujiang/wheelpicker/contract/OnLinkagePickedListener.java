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
 * 联动选择接口
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2019/6/17 18:23
 */
public interface OnLinkagePickedListener {

    /**
     * 联动选择回调
     *
     * @param first  选中项的第一级条目内容
     * @param second 选中项的第二级条目内容
     * @param third  选中项的第三级条目内容
     */
    void onLinkagePicked(Object first, Object second, Object third);

}
