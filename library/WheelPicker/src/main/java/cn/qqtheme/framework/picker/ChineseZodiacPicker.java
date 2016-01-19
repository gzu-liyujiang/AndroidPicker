package cn.qqtheme.framework.picker;

import android.app.Activity;

/**
 * 生肖选择器
 *
 * @author 李玉江[QQ :1032694760]
 * @version 2015 /12/15
 */
public class ChineseZodiacPicker extends OptionPicker {

    /**
     * Instantiates a new Chinese zodiac picker.
     *
     * @param activity the activity
     */
    public ChineseZodiacPicker(Activity activity) {
        super(activity, new String[]{
                "鼠",
                "牛",
                "虎",
                "兔",
                "龙",
                "蛇",
                "马",
                "羊",
                "猴",
                "鸡",
                "狗",
                "猪",
        });
    }

}
