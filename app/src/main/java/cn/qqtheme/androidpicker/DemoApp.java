package cn.qqtheme.androidpicker;

import android.app.Application;

import cn.qqtheme.framework.popup.Popup;

/**
 * 描述
 *
 * @author 李玉江[QQ:1032694760]
 * @since 2015/12/29
 * Created By Android Studio
 */
public class DemoApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Popup.setAnimation(R.style.Animation_CustomPopup);
    }

}
