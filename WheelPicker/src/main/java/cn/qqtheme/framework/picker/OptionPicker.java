package cn.qqtheme.framework.picker;

import android.app.Activity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import cn.qqtheme.framework.widget.WheelView;

/**
 * 单项选择器
 *
 * @author 李玉江[QQ:1032694760]
 * @version 2015/9/29
 */
public class OptionPicker extends WheelPicker {
    protected ArrayList<String> options = new ArrayList<String>();
    private OnOptionPickListener onOptionPickListener;
    private String selectedOption = "";
    private String label = "";

    public OptionPicker(Activity activity, String[] options) {
        super(activity);
        this.options.addAll(Arrays.asList(options));
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setSelectedIndex(int index) {
        for (int i = 0; i < options.size(); i++) {
            if (index == i) {
                selectedOption = options.get(index);
                break;
            }
        }
    }

    public void setSelectedItem(String option) {
        selectedOption = option;
    }

    public void setOnOptionPickListener(OnOptionPickListener listener) {
        this.onOptionPickListener = listener;
    }

    @Override
    protected View initContentView() {
        if (options.size() == 0) {
            throw new IllegalArgumentException("please initial options at first, can't be empty");
        }
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);
        WheelView optionView = new WheelView(activity);
        optionView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        optionView.setTextSize(textSize);
        optionView.setTextColor(textColorNormal, textColorFocus);
        optionView.setLineVisible(lineVisible);
        optionView.setLineColor(lineColor);
        optionView.setOffset(offset);
        layout.addView(optionView);
        TextView labelView = new TextView(activity);
        labelView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        labelView.setTextColor(textColorFocus);
        labelView.setTextSize(textSize);
        layout.addView(labelView);
        if (!TextUtils.isEmpty(label)) {
            labelView.setText(label);
        }
        if (TextUtils.isEmpty(selectedOption)) {
            optionView.setItems(options);
        } else {
            optionView.setItems(options, selectedOption);
        }
        optionView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(boolean isUserScroll, int selectedIndex, String item) {
                selectedOption = item;
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
                if (onOptionPickListener != null) {
                    onOptionPickListener.onOptionPicked(selectedOption);
                }
            }
        });
    }

    public interface OnOptionPickListener {

        void onOptionPicked(String option);

    }

}
