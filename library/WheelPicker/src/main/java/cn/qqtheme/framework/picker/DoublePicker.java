package cn.qqtheme.framework.picker;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

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
        final WheelView firstView = createWheelView();
        firstView.setLayoutParams(new LinearLayout.LayoutParams(0, WRAP_CONTENT, 1.0f));
        layout.addView(firstView);
        final WheelView secondView = createWheelView();
        secondView.setLayoutParams(new LinearLayout.LayoutParams(0, WRAP_CONTENT, 1.0f));
        layout.addView(secondView);
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
