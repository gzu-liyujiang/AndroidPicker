package cn.qqtheme.framework.picker;

import android.app.Activity;

/**
 * 性别
 *
 * @author 李玉江[QQ:1032694760]
 * @since 2015/12/15
 * Created By Android Studio
 */
public class SexPicker extends OptionPicker {

    public SexPicker(Activity activity) {
        super(activity, new String[]{
                "男",
                "女",
                "保密"
        });
    }

    /**
     * 仅仅提供男和女来选择
     */
    public void onlyMaleAndFemale() {
        options.remove(options.size() - 1);
        setSelected(options.size() - 1);
    }

    public void defaultSelected() {
        setSelected(options.size() - 1);
    }

    public void maleSelected() {
        setSelected(0);
    }

    public void femaleSelected() {
        setSelected(1);
    }

}
