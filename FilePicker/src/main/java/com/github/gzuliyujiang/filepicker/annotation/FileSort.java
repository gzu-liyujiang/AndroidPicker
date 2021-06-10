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

package com.github.gzuliyujiang.filepicker.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2021/6/10 14:35
 */
@Retention(RetentionPolicy.SOURCE)
public @interface FileSort {
    int BY_NAME_ASC = 0;
    int BY_NAME_DESC = 1;
    int BY_TIME_ASC = 2;
    int BY_TIME_DESC = 3;
    int BY_SIZE_ASC = 4;
    int BY_SIZE_DESC = 5;
    int BY_EXTENSION_ASC = 6;
    int BY_EXTENSION_DESC = 7;
}
