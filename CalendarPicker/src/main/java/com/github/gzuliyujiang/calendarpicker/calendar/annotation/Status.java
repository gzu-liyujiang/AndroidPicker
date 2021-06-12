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

package com.github.gzuliyujiang.calendarpicker.calendar.annotation;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.github.gzuliyujiang.calendarpicker.calendar.annotation.Status.BOUND_L;
import static com.github.gzuliyujiang.calendarpicker.calendar.annotation.Status.BOUND_M;
import static com.github.gzuliyujiang.calendarpicker.calendar.annotation.Status.BOUND_R;
import static com.github.gzuliyujiang.calendarpicker.calendar.annotation.Status.INVALID;
import static com.github.gzuliyujiang.calendarpicker.calendar.annotation.Status.NORMAL;
import static com.github.gzuliyujiang.calendarpicker.calendar.annotation.Status.RANGE;
import static com.github.gzuliyujiang.calendarpicker.calendar.annotation.Status.STRESS;

/**
 * Created by peng on 2017/8/2.
 */

@IntDef(value = {NORMAL, INVALID, RANGE, BOUND_L, BOUND_M, BOUND_R, STRESS})
@Retention(RetentionPolicy.SOURCE)
public @interface Status {
    //正常
    int NORMAL = 0;
    //不可用
    int INVALID = 1;
    //范围内
    int RANGE = 2;
    //左边界
    int BOUND_L = 3;
    //单选
    int BOUND_M = 4;
    //右边界
    int BOUND_R = 5;
    //强调
    int STRESS = 6;
}