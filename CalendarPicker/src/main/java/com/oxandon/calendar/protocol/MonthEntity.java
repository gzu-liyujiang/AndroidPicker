package com.oxandon.calendar.protocol;

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

    private Date date;
    private Interval<Date> valid;
    private Interval<Date> select;
    private Interval<String> selectNote;
    private boolean singleFlag = false;

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

    public MonthEntity singleFlag(boolean single) {
        this.singleFlag = single;
        return this;
    }

    public boolean singleFlag() {
        return this.singleFlag;
    }

    public Interval<String> selectNote() {
        return selectNote;
    }

    public MonthEntity selectNote(Interval<String> selectNote) {
        this.selectNote = selectNote;
        return this;
    }

    private final static List<MonthEntity> pools = new ArrayList<>();

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