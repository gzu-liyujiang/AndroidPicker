package cn.qqtheme.framework.picker;

import android.app.Activity;

import cn.qqtheme.framework.util.DateUtils;

/**
 * 小时选择器
 *
 * @author 李玉江[QQ:1032694760]
 * @since 2015/12/15
 * Created By Android Studio
 */
public class HourPicker extends OptionPicker {
    public enum Mode {
        //24小时
        HOUR_OF_DAY,
        //12小时
        HOUR
    }

    public HourPicker(Activity activity) {
        this(activity, Mode.HOUR_OF_DAY);
    }

    public HourPicker(Activity activity, Mode mode) {
        super(activity, new String[]{});
        if (mode.equals(Mode.HOUR)) {
            for (int i = 1; i <= 12; i++) {
                options.add(DateUtils.fillZore(i));
            }
        } else {
            for (int i = 0; i < 24; i++) {
                options.add(DateUtils.fillZore(i));
            }
        }
    }

}