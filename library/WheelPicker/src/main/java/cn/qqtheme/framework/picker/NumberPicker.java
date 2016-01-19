package cn.qqtheme.framework.picker;

import android.app.Activity;

/**
 * 数字选择器
 *
 * @author 李玉江[QQ :1032694760]
 * @version 2015 /10/24
 */
public class NumberPicker extends OptionPicker {

    /**
     * Instantiates a new Number picker.
     *
     * @param activity the activity
     */
    public NumberPicker(Activity activity) {
        super(activity, new String[]{});
    }

    /**
     * Sets range.
     *
     * @param startNumber the start number
     * @param endNumber   the end number
     */
    public void setRange(int startNumber, int endNumber) {
        setRange(startNumber, endNumber, 1);
    }

    /**
     * Sets range.
     *
     * @param startNumber the start number
     * @param endNumber   the end number
     * @param step        the step
     */
    public void setRange(int startNumber, int endNumber, int step) {
        for (int i = startNumber; i <= endNumber; i = i + step) {
            options.add(String.valueOf(i));
        }
    }

    /**
     * Sets selected item.
     *
     * @param number the number
     */
    public void setSelectedItem(int number) {
        setSelectedItem(String.valueOf(number));
    }

}

