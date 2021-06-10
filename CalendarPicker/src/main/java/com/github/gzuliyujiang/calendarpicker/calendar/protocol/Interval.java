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

package com.github.gzuliyujiang.calendarpicker.calendar.protocol;

/**
 * 泛型区间
 * Created by peng on 2017/8/4.
 */

public class Interval<T> {
    private int lBound;
    private T left;
    private int rBound;
    private T right;

    public int lBound() {
        return lBound;
    }

    public void lBound(int lBound) {
        this.lBound = lBound;
    }

    public T left() {
        return left;
    }

    public Interval left(T left) {
        this.left = left;
        return this;
    }

    public int rBound() {
        return rBound;
    }

    public void rBound(int rBound) {
        this.rBound = rBound;
    }

    public T right() {
        return right;
    }

    public Interval right(T right) {
        this.right = right;
        return this;
    }

    public boolean bothNoNull() {
        return null != left() && null != right();
    }
}
