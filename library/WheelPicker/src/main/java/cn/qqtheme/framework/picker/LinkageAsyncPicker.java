package cn.qqtheme.framework.picker;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * 两级、三级联动选择器，数据通过异步获取。
 * <br />
 * Author:李玉江[QQ:1032694760]
 * DateTime:2017/01/01 00:00
 * Builder:Android Studio
 *
 * @see LinkagePicker
 */
public class LinkageAsyncPicker extends WheelPicker {

    public LinkageAsyncPicker(Activity activity) {
        super(activity);
    }

    @NonNull
    @Override
    protected View makeCenterView() {
        throw new RuntimeException("Stub");
    }

}
