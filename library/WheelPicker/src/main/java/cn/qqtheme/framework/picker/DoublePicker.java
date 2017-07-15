package cn.qqtheme.framework.picker;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.qqtheme.framework.widget.WheelView;

/**
 * 双项选择器，选择两项，数据不联动。
 * <p/>
 * Author:李玉江[QQ:1032694760]
 * DateTime:2017/5/1 8:34
 * Builder:Android Studio
 */
public class DoublePicker extends WheelPicker {
    private List<String> firstData = new ArrayList<>();
    private List<String> secondData = new ArrayList<>();
    private int selectedFirstIndex = 0;
    private int selectedSecondIndex = 0;
    private OnWheelListener onWheelListener;
    private OnPickListener onPickListener;
    private CharSequence firstPrefixLabel, firstSuffixLabel;
    private CharSequence secondPrefixLabel, secondSuffixLabel;

    public DoublePicker(Activity activity, List<String> firstData, List<String> secondData) {
        super(activity);
        this.firstData = firstData;
        this.secondData = secondData;
    }

    public void setSelectedIndex(int firstIndex, int secondIndex) {
        if (firstIndex >= 0 && firstIndex < firstData.size()) {
            selectedFirstIndex = firstIndex;
        }
        if (secondIndex >= 0 && secondIndex < secondData.size()) {
            selectedSecondIndex = secondIndex;
        }
    }

    public void setFirstLabel(CharSequence firstPrefixLabel, CharSequence firstSuffixLabel) {
        this.firstPrefixLabel = firstPrefixLabel;
        this.firstSuffixLabel = firstSuffixLabel;
    }

    public void setSecondLabel(CharSequence secondPrefixLabel, CharSequence secondSuffixLabel) {
        this.secondPrefixLabel = secondPrefixLabel;
        this.secondSuffixLabel = secondSuffixLabel;
    }

    public String getSelectedFirstItem() {
        if (firstData.size() > selectedFirstIndex) {
            return firstData.get(selectedFirstIndex);
        }
        return "";
    }

    public String getSelectedSecondItem() {
        if (secondData.size() > selectedSecondIndex) {
            return secondData.get(selectedSecondIndex);
        }
        return "";
    }

    @NonNull
    @Override
    protected View makeCenterView() {
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);
        if (!TextUtils.isEmpty(firstPrefixLabel)) {
            TextView firstPrefixLabelView = createLabelView();
            firstPrefixLabelView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
            firstPrefixLabelView.setText(firstPrefixLabel);
            layout.addView(firstPrefixLabelView);
        }
        final WheelView firstView = createWheelView();
        firstView.setLayoutParams(new LinearLayout.LayoutParams(0, WRAP_CONTENT, 1.0f));
        layout.addView(firstView);
        if (!TextUtils.isEmpty(firstSuffixLabel)) {
            TextView firstSuffixLabelView = createLabelView();
            firstSuffixLabelView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
            firstSuffixLabelView.setText(firstSuffixLabel);
            layout.addView(firstSuffixLabelView);
        }
        if (!TextUtils.isEmpty(secondPrefixLabel)) {
            TextView secondPrefixLabelView = createLabelView();
            secondPrefixLabelView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
            secondPrefixLabelView.setText(secondPrefixLabel);
            layout.addView(secondPrefixLabelView);
        }
        final WheelView secondView = createWheelView();
        secondView.setLayoutParams(new LinearLayout.LayoutParams(0, WRAP_CONTENT, 1.0f));
        layout.addView(secondView);
        if (!TextUtils.isEmpty(secondSuffixLabel)) {
            TextView secondSuffixLabelView = createLabelView();
            secondSuffixLabelView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
            secondSuffixLabelView.setText(secondSuffixLabel);
            layout.addView(secondSuffixLabelView);
        }
        firstView.setItems(firstData, selectedFirstIndex);
        firstView.setOnItemSelectListener(new WheelView.OnItemSelectListener() {
            @Override
            public void onSelected(int index) {
                selectedFirstIndex = index;
                if (onWheelListener != null) {
                    onWheelListener.onFirstWheeled(selectedFirstIndex, firstData.get(selectedFirstIndex));
                }
            }
        });
        secondView.setItems(secondData, selectedSecondIndex);
        secondView.setOnItemSelectListener(new WheelView.OnItemSelectListener() {
            @Override
            public void onSelected(int index) {
                selectedSecondIndex = index;
                if (onWheelListener != null) {
                    onWheelListener.onSecondWheeled(selectedSecondIndex, secondData.get(selectedSecondIndex));
                }
            }
        });
        return layout;
    }

    @Override
    public void onSubmit() {
        if (onPickListener != null) {
            onPickListener.onPicked(selectedFirstIndex, selectedSecondIndex);
        }
    }

    public void setOnWheelListener(OnWheelListener onWheelListener) {
        this.onWheelListener = onWheelListener;
    }

    public void setOnPickListener(OnPickListener onPickListener) {
        this.onPickListener = onPickListener;
    }

    /**
     * 数据条目滑动监听器
     */
    public interface OnWheelListener {

        void onFirstWheeled(int index, String item);

        void onSecondWheeled(int index, String item);

    }

    /**
     * 数据选择完成监听器
     */
    public interface OnPickListener {

        void onPicked(int selectedFirstIndex, int selectedSecondIndex);

    }

}
