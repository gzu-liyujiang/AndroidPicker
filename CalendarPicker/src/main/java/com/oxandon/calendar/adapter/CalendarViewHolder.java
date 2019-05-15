package com.oxandon.calendar.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.oxandon.calendar.view.MonthView;

/**
 * Created by peng on 2017/8/3.
 */

public class CalendarViewHolder extends RecyclerView.ViewHolder {
    private final MonthView view;

    CalendarViewHolder(View itemView) {
        super(itemView);
        view = (MonthView) itemView;
    }

    public MonthView view() {
        return view;
    }
}
