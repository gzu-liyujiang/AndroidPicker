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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by peng on 2017/8/4.
 */

public class MonthEntity {
    public static final int WEEK_DAYS = 7;
    public static final int MAX_HORIZONTAL_LINES = 6;
    public static final int MAX_DAYS_OF_MONTH = 31;
    public static final String STR_TODAY = "今天";
    private final static List<MonthEntity> pools = new ArrayList<>();
    private Date date;
    private Interval<Date> valid;
    private Interval<Date> select;
    private Interval<String> selectNote;
    private boolean singleMode = false;

    private MonthEntity() {
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

    public MonthEntity singleMode(boolean single) {
        this.singleMode = single;
        return this;
    }

    public boolean singleMode() {
        return this.singleMode;
    }

    public Interval<String> selectNote() {
        return selectNote;
    }

    public MonthEntity selectNote(Interval<String> selectNote) {
        this.selectNote = selectNote;
        return this;
    }

    public static MonthEntity obtain(Interval<Date> valid, Interval<Date> select) {
        MonthEntity entity = pools.size() == 0 ? new MonthEntity() : pools.remove(0);
        entity.valid = valid;
        entity.select = select;
        return entity;
    }

    public void recycle() {
        if (!pools.contains(this)) {
            date = null;
            valid = null;
            select = null;
            selectNote = null;
            pools.add(this);
        }
    }
}