package cn.qqtheme.framework.picker;

import android.app.Activity;
import android.support.annotation.NonNull;
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
 * @author 李玉江[QQ :1032694760]
 * @version 2015 /9/29
 */
public class OptionPicker extends WheelPicker {
    /**
     * The Options.
     */
    protected ArrayList<String> options = new ArrayList<String>();
    private OnOptionPickListener onOptionPickListener;
    private String selectedOption = "";
    private String label = "";

    /**
     * Instantiates a new Option picker.
     *
     * @param activity the activity
     * @param options  the options
     */
    public OptionPicker(Activity activity, String[] options) {
        super(activity);
        this.options.addAll(Arrays.asList(options));
    }

    /**
     * Sets label.
     *
     * @param label the label
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Sets selected index.
     *
     * @param index the index
     */
    public void setSelectedIndex(int index) {
        for (int i = 0; i < options.size(); i++) {
            if (index == i) {
                selectedOption = options.get(index);
                break;
            }
        }
    }

    /**
     * Sets selected item.
     *
     * @param option the option
     */
    public void setSelectedItem(String option) {
        selectedOption = option;
    }

    /**
     * Sets on option pick listener.
     *
     * @param listener the listener
     */
    public void setOnOptionPickListener(OnOptionPickListener listener) {
        this.onOptionPickListener = listener;
    }

    @Override
    @NonNull
    protected View makeCenterView() {
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
    public void onSubmit() {
        if (onOptionPickListener != null) {
            onOptionPickListener.onOptionPicked(selectedOption);
        }
    }

    /**
     * Gets selected option.
     *
     * @return the selected option
     */
    public String getSelectedOption() {
        return selectedOption;
    }

    /**
     * The interface On option pick listener.
     */
    public interface OnOptionPickListener {

        /**
         * On option picked.
         *
         * @param option the option
         */
        void onOptionPicked(String option);

    }

}
