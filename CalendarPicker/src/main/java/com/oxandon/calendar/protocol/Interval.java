package com.oxandon.calendar.protocol;

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
