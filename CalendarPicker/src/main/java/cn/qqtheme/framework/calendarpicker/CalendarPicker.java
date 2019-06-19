package cn.qqtheme.framework.calendarpicker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.oxandon.calendar.R;
import com.oxandon.calendar.adapter.CalendarAdapter;
import com.oxandon.calendar.protocol.OnCalendarSelectListener;
import com.oxandon.calendar.utils.DateUtils;
import com.oxandon.calendar.view.CalendarView;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.qqtheme.framework.popup.AbstractConfirmPopup;

/**
 * 日历日期选择器
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/4/30 13:36
 */
public class CalendarPicker extends AbstractConfirmPopup<View> implements OnCalendarSelectListener {
    private CalendarView calendarView;
    private boolean isSingle;
    private Date minDate, maxDate;
    private Date selectDate, startDate, endDate;
    private String noteFrom, noteTo;
    private OnSingleDatePickListener onSingleDatePickListener;
    private OnRangeDatePickListener onRangeDatePickListener;

    public CalendarPicker(FragmentActivity activity, boolean isSingle) {
        super(activity);
        this.isSingle = isSingle;
        setHeight((int) (activity.getResources().getDisplayMetrics().heightPixels * 0.7));
    }

    @NonNull
    @Override
    protected View createBodyView(Context context) {
        View view = LayoutInflater.from(activity).inflate(R.layout.calendar_date_picker, null);
        calendarView = view.findViewById(R.id.calendar_date_picker_calendar);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View contentView) {
        super.onViewCreated(contentView);
        CalendarAdapter adapter = calendarView.getAdapter();
        adapter.setOnCalendarSelectListener(this);
        if (!TextUtils.isEmpty(noteFrom) && !TextUtils.isEmpty(noteTo)) {
            adapter.intervalNotes(noteFrom, noteTo);
        }
        adapter.single(isSingle);
        if (isSingle) {
            startDate = selectDate;
        }
        adapter.setRange(minDate, maxDate, true, false);
        adapter.valid(minDate, maxDate);
        adapter.select(startDate, endDate);
    }

    public void setOnSingleDatePickListener(OnSingleDatePickListener onSingleDatePickListener) {
        this.onSingleDatePickListener = onSingleDatePickListener;
    }

    public void setRangeDate(Date minDate, Date maxDate) {
        this.minDate = DateUtils.min(minDate, maxDate);
        this.maxDate = DateUtils.max(minDate, maxDate);
    }

    public void setRangeDateBaseOnToday(int offsetMonth) {
        minDate = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTime(minDate);
        calendar.add(Calendar.MONTH, offsetMonth);
        calendar.set(Calendar.DAY_OF_MONTH, DateUtils.maxDaysOfMonth(calendar.getTime()));
        maxDate = calendar.getTime();
    }

    public void setSelectedDate(long timeInMillis) {
        this.selectDate = new Date(timeInMillis);
    }

    public void setSelectedDate(long timeInMillisStart, long timeInMillisEnd) {
        this.startDate = new Date(Math.min(timeInMillisStart, timeInMillisEnd));
        this.endDate = new Date(Math.max(timeInMillisStart, timeInMillisEnd));
    }

    public void setIntervalNotes(String noteFrom, String noteTo) {
        this.noteFrom = noteFrom;
        this.noteTo = noteTo;
    }

    public void setOnRangeDatePickListener(OnRangeDatePickListener onRangeDatePickListener) {
        this.onRangeDatePickListener = onRangeDatePickListener;
    }

    @Override
    protected void onConfirm() {
        if (isSingle && selectDate == null) {
            return;
        }
        boolean doubleNotSelected = startDate == null || endDate == null;
        if (!isSingle && doubleNotSelected) {
            return;
        }
        dismiss();
        if (onSingleDatePickListener != null) {
            onSingleDatePickListener.onDatePicked(selectDate);
        }
        if (onRangeDatePickListener != null) {
            onRangeDatePickListener.onDatePicked(startDate, endDate);
        }
    }

    @Override
    public void onSingleSelect(@NonNull Date date) {
        selectDate = date;
    }

    @Override
    public void onDoubleSelect(@NonNull Date before, @NonNull Date after) {
        startDate = before;
        endDate = after;
    }

}
