package cn.qqtheme.framework.calendarpicker;

import android.support.annotation.NonNull;

import java.util.Date;

public interface OnRangeDatePickListener {

    void onDatePicked(@NonNull Date startDate, @NonNull Date endDate);

}
