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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by peng on 2017/8/4.
 */
public class MonthEntity implements Serializable {
    public static int WEEK_DAYS = 7;
    public static int MAX_HORIZONTAL_LINES = 6;
    public static int MAX_DAYS_OF_MONTH = 31;
    public static String STR_TODAY = "今天";
    private final static List<MonthEntity> pools = new ArrayList<>();
    private Date date;
    private Interval<Date> valid;
    private Interval<Date> select;
    private Interval<String> note;
    private boolean singleMode = false;
    private FestivalProvider festivalProvider;

    static {
        if (!Locale.getDefault().getDisplayLanguage().contains("中文")) {
            STR_TODAY = "Today";
        }
    }

    public static MonthEntity obtain(Interval<Date> valid, Interval<Date> select) {
        MonthEntity entity = pools.size() == 0 ? new MonthEntity() : pools.remove(0);
        entity.valid = valid;
        entity.select = select;
        return entity;
    }

    private MonthEntity() {
        super();
    }

    public Date date() {
        return date;
    }

    public MonthEntity date(Date date) {
        this.date = date;
        return this;
    }

    public Interval<Date> valid() {
        return valid;
    }

    public MonthEntity valid(Interval<Date> valid) {
        this.valid = valid;
        return this;
    }

    public Interval<Date> select() {
        return select;
    }

    public MonthEntity select(Interval<Date> select) {
        this.select = select;
        return this;
    }

    public MonthEntity singleMode(boolean singleMode) {
        this.singleMode = singleMode;
        return this;
    }

    public boolean singleMode() {
        return this.singleMode;
    }

    public MonthEntity festivalProvider(FestivalProvider festivalProvider) {
        this.festivalProvider = festivalProvider;
        return this;
    }

    public FestivalProvider festivalProvider() {
        return this.festivalProvider;
    }

    public Interval<String> note() {
        return note;
    }

    public MonthEntity note(Interval<String> note) {
        this.note = note;
        return this;
    }

    public void recycle() {
        if (!pools.contains(this)) {
            date = null;
            valid = null;
            select = null;
            note = null;
            pools.add(this);
        }
    }
}