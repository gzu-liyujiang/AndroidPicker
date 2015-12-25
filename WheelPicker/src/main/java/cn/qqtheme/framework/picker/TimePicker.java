package cn.qqtheme.framework.picker;

import android.app.Activity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import cn.qqtheme.framework.util.DateUtils;
import cn.qqtheme.framework.widget.WheelView;

/**
 * 时间选择器
 *
 * @author 李玉江[QQ:1032694760]
 * @since 2015/12/14
 * Created By Android Studio
 */
public class TimePicker extends WheelPicker {
    private OnTimePickListener onTimePickListener;
    private Mode mode;
    private String hourLabel = "时", minuteLabel = "分";
    private String selectedHour = "", selectedMinute = "";

    public enum Mode {
        //24小时
        HOUR_OF_DAY,
        //12小时
        HOUR
    }

    public TimePicker(Activity activity) {
        this(activity, Mode.HOUR_OF_DAY);
    }

    public TimePicker(Activity activity, Mode mode) {
        super(activity);
        this.mode = mode;
        selectedHour = DateUtils.fillZore(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
        selectedMinute = DateUtils.fillZore(Calendar.getInstance().get(Calendar.MINUTE));
    }

    public void setLabel(String hourLabel, String minuteLabel) {
        this.hourLabel = hourLabel;
        this.minuteLabel = minuteLabel;
    }

    public void setSelectedItem(int hour, int minute) {
        selectedHour = String.valueOf(hour);
        selectedMinute = String.valueOf(minute);
    }

    public void setOnTimePickListener(OnTimePickListener listener) {
        this.onTimePickListener = listener;
    }

    @Override
    protected View initContentView() {
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
        if (mode.equals(Mode.HOUR)) {
            for (int i = 1; i <= 12; i++) {
                hours.add(DateUtils.fillZore(i));
            }
        } else {
            for (int i = 0; i < 24; i++) {
                hours.add(DateUtils.fillZore(i));
            }
        }
        hourView.setItems(hours, selectedHour);
        ArrayList<String> minutes = new ArrayList<String>();
        for (int i = 0; i < 60; i++) {
            minutes.add(DateUtils.fillZore(i));
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
    protected void setContentViewAfter(View contentView) {
        super.setContentViewAfter(contentView);
        super.setOnConfirmListener(new OnConfirmListener() {
            @Override
            public void onConfirm() {
                if (onTimePickListener != null) {
                    onTimePickListener.onTimePicked(selectedHour, selectedMinute);
                }
            }
        });
    }

    public interface OnTimePickListener {

        void onTimePicked(String hour, String minute);

    }

}
