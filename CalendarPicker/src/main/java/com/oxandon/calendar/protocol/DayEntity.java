package com.oxandon.calendar.protocol;

import com.oxandon.calendar.annotation.Status;

import java.util.ArrayList;
import java.util.List;


/**
 * 日期单元实体信息
 * Created by peng on 2017/8/4.
 */

public class DayEntity {
    @Status
    private int status;
    private int value;
    @Status
    private int valueStatus;
    private String desc;
    @Status
    private int descStatus;
    private String note;

    private DayEntity() {
    }

    @Status
    public int status() {
        return status;
    }

    public DayEntity status(@Status int status) {
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

    @Status
    public int valueStatus() {
        return valueStatus;
    }

    public DayEntity valueStatus(@Status int valueStatus) {
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

    @Status
    public int descStatus() {
        return descStatus;
    }

    public DayEntity descStatus(@Status int descStatus) {
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
            this.status = Status.NORMAL;
            this.value = -1;
            this.valueStatus = Status.NORMAL;
            this.descStatus = Status.NORMAL;
            this.desc = "";
            pools.add(this);
        }
    }

    private static final List<DayEntity> pools = new ArrayList<>();

    public static DayEntity obtain(@Status int status, int value, String desc) {
        DayEntity entity = 0 == pools.size() ? new DayEntity() : pools.remove(0);
        entity.status = status;
        entity.value = value;
        entity.valueStatus = status;
        entity.descStatus = status;
        entity.desc = desc;
        return entity;
    }
}