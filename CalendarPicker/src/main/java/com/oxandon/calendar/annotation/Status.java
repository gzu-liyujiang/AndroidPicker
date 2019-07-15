package com.oxandon.calendar.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.oxandon.calendar.annotation.Status.BOUND_L;
import static com.oxandon.calendar.annotation.Status.BOUND_M;
import static com.oxandon.calendar.annotation.Status.BOUND_R;
import static com.oxandon.calendar.annotation.Status.INVALID;
import static com.oxandon.calendar.annotation.Status.NORMAL;
import static com.oxandon.calendar.annotation.Status.RANGE;
import static com.oxandon.calendar.annotation.Status.STRESS;

/**
 * Created by peng on 2017/8/2.
 */

@IntDef(value = {NORMAL, INVALID, RANGE, BOUND_L, BOUND_M, BOUND_R, STRESS})
@Retention(RetentionPolicy.RUNTIME)
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