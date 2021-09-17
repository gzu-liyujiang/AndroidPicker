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

package com.github.gzuliyujiang.calendarpicker.core;

import android.graphics.Color;

import androidx.annotation.ColorInt;

import java.io.Serializable;

/**
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2021/9/17 12:28
 */
public class ColorScheme implements Serializable {
    private int weekTextColor = 0xFF343434;
    private int weekBackgroundColor = Color.TRANSPARENT;
    private int monthTitleTextColor = 0xFF343434;
    private int monthTitleBackgroundColor = Color.TRANSPARENT;
    private int monthBackgroundColor = Color.TRANSPARENT;
    private int monthDividerColor = Color.TRANSPARENT;
    private int dayNormalTextColor = 0xFF343434;
    private int dayInvalidTextColor = 0xFFCCCCCC;
    private int dayStressTextColor = 0xFFFF6600;
    private int daySelectTextColor = 0xFFFFFFFF;
    private int dayNormalBackgroundColor = Color.TRANSPARENT;
    private int dayInvalidBackgroundColor = Color.TRANSPARENT;
    private int daySelectBackgroundColor = 0xFFE75051;

    public ColorScheme weekTextColor(@ColorInt int color) {
        this.weekTextColor = color;
        return this;
    }

    @ColorInt
    public int weekTextColor() {
        return weekTextColor;
    }

    public ColorScheme weekBackgroundColor(@ColorInt int color) {
        this.weekBackgroundColor = color;
        return this;
    }

    @ColorInt
    public int weekBackgroundColor() {
        return weekBackgroundColor;
    }

    public ColorScheme monthTitleTextColor(@ColorInt int color) {
        this.monthTitleTextColor = color;
        return this;
    }

    @ColorInt
    public int monthTitleTextColor() {
        return monthTitleTextColor;
    }

    public ColorScheme monthTitleBackgroundColor(@ColorInt int color) {
        this.monthTitleBackgroundColor = color;
        return this;
    }

    @ColorInt
    public int monthTitleBackgroundColor() {
        return monthTitleBackgroundColor;
    }

    public ColorScheme monthBackgroundColor(@ColorInt int color) {
        this.monthBackgroundColor = color;
        return this;
    }

    @ColorInt
    public int monthBackgroundColor() {
        return monthBackgroundColor;
    }

    public ColorScheme monthDividerColor(@ColorInt int color) {
        this.monthDividerColor = color;
        return this;
    }

    @ColorInt
    public int monthDividerColor() {
        return monthDividerColor;
    }

    public ColorScheme dayNormalTextColor(@ColorInt int color) {
        this.dayNormalTextColor = color;
        return this;
    }

    @ColorInt
    public int dayNormalTextColor() {
        return dayNormalTextColor;
    }

    public ColorScheme dayInvalidTextColor(@ColorInt int color) {
        this.dayInvalidTextColor = color;
        return this;
    }

    @ColorInt
    public int dayInvalidTextColor() {
        return dayInvalidTextColor;
    }

    public ColorScheme dayStressTextColor(@ColorInt int color) {
        this.dayStressTextColor = color;
        return this;
    }

    @ColorInt
    public int dayStressTextColor() {
        return dayStressTextColor;
    }

    public ColorScheme daySelectTextColor(@ColorInt int color) {
        this.daySelectTextColor = color;
        return this;
    }

    @ColorInt
    public int daySelectTextColor() {
        return daySelectTextColor;
    }

    public ColorScheme dayNormalBackgroundColor(@ColorInt int color) {
        this.dayNormalBackgroundColor = color;
        return this;
    }

    @ColorInt
    public int dayNormalBackgroundColor() {
        return dayNormalBackgroundColor;
    }

    public ColorScheme dayInvalidBackgroundColor(@ColorInt int color) {
        this.dayInvalidBackgroundColor = color;
        return this;
    }

    @ColorInt
    public int dayInvalidBackgroundColor() {
        return dayInvalidBackgroundColor;
    }

    public ColorScheme daySelectBackgroundColor(@ColorInt int color) {
        this.daySelectBackgroundColor = color;
        return this;
    }

    @ColorInt
    public int daySelectBackgroundColor() {
        return daySelectBackgroundColor;
    }

}
