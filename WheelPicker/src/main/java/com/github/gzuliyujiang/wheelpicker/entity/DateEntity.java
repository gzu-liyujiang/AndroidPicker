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

package com.github.gzuliyujiang.wheelpicker.entity;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;

/**
 * 日期数据实体
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2019/6/17 15:29
 */
@SuppressWarnings({"unused"})
public class DateEntity implements Serializable {
    private int year;
    private int month;
    private int day;

    public static DateEntity target(int year, int month, int day) {
        DateEntity entity = new DateEntity();
        entity.setYear(year);
        entity.setMonth(month);
        entity.setDay(day);
        return entity;
    }

    public static DateEntity today() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return target(year, month, day);
    }

    public static DateEntity dayOnFuture(int day) {
        DateEntity entity = today();
        entity.setDay(entity.getDay() + day);
        return entity;
    }

    public static DateEntity monthOnFuture(int month) {
        DateEntity entity = today();
        entity.setMonth(entity.getMonth() + month);
        return entity;
    }

    public static DateEntity yearOnFuture(int year) {
        DateEntity entity = today();
        entity.setYear(entity.getYear() + year);
        return entity;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public long toTimeInMillis() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DateEntity that = (DateEntity) o;
        return year == that.year &&
                month == that.month &&
                day == that.day;
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, month, day);
    }

    @NonNull
    @Override
    public String toString() {
        return year + "-" + month + "-" + day;
    }

}
