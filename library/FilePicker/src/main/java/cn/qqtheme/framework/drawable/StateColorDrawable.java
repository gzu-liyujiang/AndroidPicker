package cn.qqtheme.framework.drawable;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.ColorInt;

/**
 * 按下状态与普通状态下显示不同的颜色
 * <br />
 * Author:李玉江[QQ:1032694760]
 * DateTime:2017/01/01 05:30
 * Builder:Android Studio
 */
public class StateColorDrawable extends StateListDrawable {

    public StateColorDrawable(@ColorInt int pressedColor) {
        this(Color.TRANSPARENT, pressedColor);
    }

    public StateColorDrawable(@ColorInt int normalColor, @ColorInt int pressedColor) {
        ColorDrawable normal = new ColorDrawable(normalColor);
        ColorDrawable pressed = new ColorDrawable(pressedColor);
        addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);
        addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_focused}, pressed);
        addState(new int[]{android.R.attr.state_enabled}, normal);
        addState(new int[]{android.R.attr.state_focused}, pressed);
        addState(new int[]{android.R.attr.state_window_focused}, normal);
        addState(new int[]{}, normal);
    }

}
