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
import java.util.List;

/**
 * Created by peng on 2017/8/4.
 */
public class DayEntity implements Serializable {
    private static final List<DayEntity> pools = new ArrayList<>();
    @DayStatus
    private int status;
    private int value;
    @DayStatus
    private int valueStatus;
    private String desc;
    @DayStatus
    private int descStatus;
    private String note;

    private DayEntity() {
        super();
    }

    @DayStatus
    public int status() {
        return status;
    }

    public DayEntity status(@DayStatus int status) {
        this.status = status;
        return this;
    }

    public DayEntity value(int value) {
        this.value = value;
        return this;
    }

    public String value() {
        return value < 0 || value > MonthEntity.MAX_DAYS_OF_MONTH ? "" : String.valueOf(value + 1);
    }

    public int intValue() {
        return value;
    }

    @DayStatus
    public int valueStatus() {
        return valueStatus;
    }

    public DayEntity valueStatus(@DayStatus int valueStatus) {
        this.valueStatus = valueStatus;
        return this;
    }

    public String desc() {
        return null == desc ? "" : desc;
    }


    public DayEntity desc(String desc) {
        this.desc = desc;
        return this;
    }

    @DayStatus
    public int descStatus() {
        return descStatus;
    }

    public DayEntity descStatus(@DayStatus int descStatus) {
        this.descStatus = descStatus;
        return this;
    }

    public String note() {
        return null == note ? "" : note;
    }

    public DayEntity note(String note) {
        this.note = note;
        return this;
    }

    public void recycle() {
        if (!pools.contains(this)) {
            this.status = DayStatus.NORMAL;
            this.value = -1;
            this.valueStatus = DayStatus.NORMAL;
            this.descStatus = DayStatus.NORMAL;
            this.desc = "";
            pools.add(this);
        }
    }

    public static DayEntity obtain(@DayStatus int status, int value, String desc) {
        DayEntity entity = 0 == pools.size() ? new DayEntity() : pools.remove(0);
        entity.status = status;
        entity.value = value;
        entity.valueStatus = status;
        entity.descStatus = status;
        entity.desc = desc;
        return entity;
    }
}