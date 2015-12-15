package cn.qqtheme.framework.picker;

import android.app.Activity;

/**
 * 星座选择器
 *
 * @author 李玉江[QQ:1032694760]
 * @since 2015/12/15
 * Created By Android Studio
 */
public class ConstellationPicker extends OptionPicker {

    public ConstellationPicker(Activity activity) {
        super(activity, new String[]{
                "水瓶座",
                "双鱼座",
                "白羊座",
                "金牛座",
                "双子座",
                "巨蟹座",
                "狮子座",
                "处女座",
                "天秤座",
                "天蝎座",
                "射手座",
                "摩羯座",
        });
    }

}
