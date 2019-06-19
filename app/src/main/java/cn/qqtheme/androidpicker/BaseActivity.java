package cn.qqtheme.androidpicker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

/**
 * Activity基类，包括通用方法、屏幕适配、状态栏适配、字体大小适配等
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/04/17 10:25
 */
public abstract class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

}
