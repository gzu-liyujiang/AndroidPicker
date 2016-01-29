package cn.qqtheme.framework.picker;

import android.app.Activity;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Calendar;

import cn.qqtheme.framework.util.DateUtils;
import cn.qqtheme.framework.widget.WheelView;

/**
 * 时间选择器
 *
 * @author 李玉江[QQ :1032694760]
 * @version 2015 /12/14
 */
public class TimePicker extends WheelPicker {
    /**
     * 24小时
     */
    public static final int HOUR_OF_DAY = 0;
    /**
     * 12小时
     */
    public static final int HOUR = 1;
    private OnTimePickListener onTimePickListener;
    private int mode;
    private String hourLabel = "时", minuteLabel = "分";
    private String selectedHour = "", selectedMinute = "";

    /**
     * 安卓开发应避免使用枚举类（enum），因为相比于静态常量enum会花费两倍以上的内存。
     *
     * @link http ://developer.android.com/training/articles/memory.html#Overhead
     */
    @IntDef(flag = false, value = {HOUR_OF_DAY, HOUR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Mode {
    }

    /**
     * Instantiates a new Time picker.
     *
     * @param activity the activity
     */
    public TimePicker(Activity activity) {
        this(activity, HOUR_OF_DAY);
    }

    /**
     * Instantiates a new Time picker.
     *
     * @param activity the activity
     * @param mode     the mode
     * @see #HOUR_OF_DAY #HOUR_OF_DAY#HOUR_OF_DAY
     * @see #HOUR #HOUR#HOUR
     */
    public TimePicker(Activity activity, @Mode int mode) {
        super(activity);
        this.mode = mode;
        selectedHour = DateUtils.fillZero(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
        selectedMinute = DateUtils.fillZero(Calendar.getInstance().get(Calendar.MINUTE));
    }

    /**
     * Sets label.
     *
     * @param hourLabel   the hour label
     * @param minuteLabel the minute label
     */
    public void setLabel(String hourLabel, String minuteLabel) {
        this.hourLabel = hourLabel;
        this.minuteLabel = minuteLabel;
    }

    /**
     * Sets selected item.
     *
     * @param hour   the hour
     * @param minute the minute
     */
    public void setSelectedItem(int hour, int minute) {
        selectedHour = String.valueOf(hour);
        selectedMinute = String.valueOf(minute);
    }

    /**
     * Sets on time pick listener.
     *
     * @param listener the listener
     */
    public void setOnTimePickListener(OnTimePickListener listener) {
        this.onTimePickListener = listener;
    }

    @Override
    @NonNull
    protected View makeCenterView() {
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);
        WheelView hourView = new WheelView(activity);
        hourView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        hourView.setTextSize(textSize);
        hourView.setTextColor(textColorNormal, textColorFocus);
        hourView.setLineVisible(lineVisible);
        hourView.setLineColor(lineColor);
        layout.addView(hourView);
        TextView hourTextView = new TextView(activity);
        hourTextView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        hourTextView.setTextSize(textSize);
        hourTextView.setTextColor(textColorFocus);
        if (!TextUtils.isEmpty(hourLabel)) {
            hourTextView.setText(hourLabel);
        }
        layout.addView(hourTextView);
        WheelView minuteView = new WheelView(activity);
        minuteView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        minuteView.setTextSize(textSize);
        minuteView.setTextColor(textColorNormal, textColorFocus);
        minuteView.setLineVisible(lineVisible);
        minuteView.setLineColor(lineColor);
        minuteView.setOffset(offset);
        layout.addView(minuteView);
        TextView minuteTextView = new TextView(activity);
        minuteTextView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        minuteTextView.setTextSize(textSize);
        minuteTextView.setTextColor(textColorFocus);
        if (!TextUtils.isEmpty(minuteLabel)) {
            minuteTextView.setText(minuteLabel);
        }
        layout.addView(minuteTextView);
        ArrayList<String> hours = new ArrayList<String>();
        if (mode == HOUR) {
            for (int i = 1; i <= 12; i++) {
                hours.add(DateUtils.fillZero(i));
            }
        } else {
            for (int i = 0; i < 24; i++) {
                hours.add(DateUtils.fillZero(i));
            }
        }
        hourView.setItems(hours, selectedHour);
        ArrayList<String> minutes = new ArrayList<String>();
        for (int i = 0; i < 60; i++) {
            minutes.add(DateUtils.fillZero(i));
        }
        minuteView.setItems(minutes, selectedMinute);
        hourView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(boolean isUserScroll, int selectedIndex, String item) {
                selectedHour = item;
            }
        });
        minuteView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(boolean isUserScroll, int selectedIndex, String item) {
                selectedMinute = item;
            }
        });
        return layout;
    }

    @Override
    public void onSubmit() {
        if (onTimePickListener != null) {
            onTimePickListener.onTimePicked(selectedHour, selectedMinute);
        }
    }

    /**
     * Gets selected hour.
     *
     * @return the selected hour
     */
    public String getSelectedHour() {
        return selectedHour;
    }

    /**
     * Gets selected minute.
     *
     * @return the selected minute
     */
    public String getSelectedMinute() {
        return selectedMinute;
    }

    /**
     * The interface On time pick listener.
     */
    public interface OnTimePickListener {

        /**
         * On time picked.
         *
         * @param hour   the hour
         * @param minute the minute
         */
        void onTimePicked(String hour, String minute);

    }

}
