package cn.qqtheme.androidpicker;

import android.app.Application;

import cn.qqtheme.framework.AppConfig;

/**
 * 类描述
 * <p/>
 * Author:李玉江[QQ:1032694760]
 * DateTime:2016/7/20 20:28
 * Builder:Android Studio
 */
public class MyPickerApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppConfig.DEBUG_ENABLE = BuildConfig.DEBUG;
        if (!AppConfig.DEBUG_ENABLE) {
            android.util.Log.d(AppConfig.DEBUG_TAG, "logcat is disabled");
        }
    }

}
