package cn.qqtheme.framework.picker;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * 双项选择器，选择两项，数据不联动。
 * <p>
 * Author:李玉江[QQ:1032694760]
 * DateTime:2017/5/1 8:34
 * Builder:Android Studio
 */
public class DoublePicker extends WheelPicker {

    public DoublePicker(Activity activity) {
        super(activity);
    }

    @NonNull
    @Override
    protected View makeCenterView() {
        throw new RuntimeException("Stub");
    }

}
