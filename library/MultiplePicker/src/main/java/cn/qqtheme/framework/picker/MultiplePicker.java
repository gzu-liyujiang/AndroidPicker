package cn.qqtheme.framework.picker;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.qqtheme.framework.popup.ConfirmPopup;

/**
 * 多项选择器
 *
 * @author 李玉江[QQ:1032694760]
 * @since 2017/06/24
 */
public class MultiplePicker extends ConfirmPopup<ScrollView> {
    private List<String> items = new ArrayList<>();
    private LinearLayout layout;
    private OnItemPickListener onItemPickListener;

    public MultiplePicker(Activity activity, String[] items) {
        this(activity, Arrays.asList(items));
    }

    public MultiplePicker(Activity activity, List<String> items) {
        super(activity);
        this.items = items;
    }

    public void setOnItemPickListener(OnItemPickListener onItemPickListener) {
        this.onItemPickListener = onItemPickListener;
    }

    @NonNull
    @Override
    protected ScrollView makeCenterView() {
        ScrollView scrollView = new ScrollView(activity);
        layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.VERTICAL);
        for (String item : items) {
            LinearLayout line = new LinearLayout(activity);
            line.setOrientation(LinearLayout.HORIZONTAL);
            line.setGravity(Gravity.CENTER);
            TextView textView = new TextView(activity);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, WRAP_CONTENT, 1.0f);
            lp.gravity = Gravity.CENTER;
            textView.setLayoutParams(lp);
            textView.setText(item);
            textView.setGravity(Gravity.CENTER);
            line.addView(textView);
            CheckBox checkBox = new CheckBox(activity);
            checkBox.setLayoutParams(new LinearLayout.LayoutParams(0, WRAP_CONTENT, 0.4f));
            line.addView(checkBox);
            layout.addView(line);
        }
        scrollView.addView(layout);
        return scrollView;
    }

    @Override
    protected void onSubmit() {
        if (onItemPickListener == null) {
            return;
        }
        List<String> checked = new ArrayList<>();
        for (int i = 0, count = layout.getChildCount(); i < count; i++) {
            LinearLayout line = (LinearLayout) layout.getChildAt(i);
            CheckBox checkBox = (CheckBox) line.getChildAt(1);
            if (checkBox.isChecked()) {
                TextView textView = (TextView) line.getChildAt(0);
                checked.add(textView.getText().toString());
            }
        }
        onItemPickListener.onItemPicked(checked.size(), checked);
    }

    public interface OnItemPickListener {

        void onItemPicked(int count, List<String> items);

    }

}
