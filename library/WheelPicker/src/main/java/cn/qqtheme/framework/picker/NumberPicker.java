package cn.qqtheme.framework.picker;

import android.app.Activity;

/**
 * 数字选择器
 *
 * @author 李玉江[QQ:1032694760]
 * @since 2015/10/24
 */
public class NumberPicker extends SinglePicker<Number> {

    public NumberPicker(Activity activity) {
        super(activity, new Number[]{});
    }

    /**
     * 设置数字范围，递增量为1
     */
    public void setRange(int startNumber, int endNumber) {
        setRange(startNumber, endNumber, 1);
    }

    /**
     * 设置数字范围及递增量
     */
    public void setRange(int startNumber, int endNumber, int step) {
        for (int i = startNumber; i <= endNumber; i = i + step) {
            addItem(i);
        }
    }

    /**
     * 设置数字范围及递增量
     */
    public void setRange(double startNumber, double endNumber, double step) {
        for (double i = startNumber; i <= endNumber; i = i + step) {
            addItem(i);
        }
    }

    /**
     * 设置默认选中的数字
     */
    public void setSelectedItem(int number) {
        super.setSelectedItem(number);
    }

    /**
     * 设置默认选中的数字
     */
    public void setSelectedItem(double number) {
        super.setSelectedItem(number);
    }

    public void setOnNumberPickListener(OnNumberPickListener listener) {
        super.setOnItemPickListener(listener);
    }

    /**
     * @deprecated use {@link #setOnNumberPickListener(OnNumberPickListener)} instead
     */
    @Deprecated
    public void setOnOptionPickListener(final OptionPicker.OnOptionPickListener listener) {
        setOnNumberPickListener(new OnNumberPickListener() {
            @Override
            public void onNumberPicked(int index, Number item) {
                listener.onOptionPicked(index, item.toString());
            }
        });
    }

    public static abstract class OnNumberPickListener implements OnItemPickListener<Number> {

        public abstract void onNumberPicked(int index, Number item);

        @Override
        public final void onItemPicked(int index, Number item) {
            onNumberPicked(index, item);
        }

    }

}

