package cn.qqtheme.androidpicker;

import android.app.Application;

import cn.qqtheme.framework.toolkit.CqrLog;

/**
 * global application state
 *
 * @author 李玉江[QQ:1032694760]
 * @date 2016/7/20 20:28
 */
public class MainApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CqrLog.DEBUG_ENABLE = true;
    }

}
