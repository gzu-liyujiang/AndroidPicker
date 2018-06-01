package cn.qqtheme.androidpicker;

import android.app.Application;

import cn.qqtheme.framework.AppConfig;
import cn.qqtheme.framework.util.LogUtils;

/**
 * Author:李玉江[QQ:1032694760]
 * DateTime:2016/7/20 20:28
 * Builder:Android Studio
 */
public class MyPickerApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            LogUtils.setIsDebug(true);
        } else {
            android.util.Log.d(AppConfig.DEBUG_TAG, "LogCat is disabled");
        }
    }

}
