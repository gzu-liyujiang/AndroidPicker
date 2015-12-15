package cn.qqtheme.framework.picker;

import android.app.Activity;

/**
 * 数字选择器
 *
 * @author 李玉江[QQ:1032694760]
 * @since 2015/10/24
 * Created By Android Studio
 */
public class NumberPicker extends OptionPicker {

    public NumberPicker(Activity activity) {
        super(activity, new String[]{});
    }

    public void setRange(int startNumber, int endNumber) {
        setRange(startNumber, endNumber, 1);
    }

    public void setRange(int startNumber, int endNumber, int step) {
        for (int i = startNumber; i <= endNumber; i = i + step) {
            options.add(String.valueOf(i));
        }
    }

}

