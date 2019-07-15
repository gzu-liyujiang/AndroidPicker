package cn.qqtheme.androidpicker.ui;

import android.content.Intent;
import android.view.View;

import cn.qqtheme.androidpicker.BaseActivity;
import cn.qqtheme.androidpicker.R;

/**
 * 主页
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/6/23
 */
public class MainActivity extends BaseActivity {

    @Override
    protected int specifyLayoutRes() {
        return R.layout.activity_main;
    }

    private void startActivity(Class<?> cls) {
        startActivity(new Intent(this, cls));
    }

    public void onDateTime(View view) {
        startActivity(DateTimeWheelActivity.class);
    }

    public void onCalendarDate(View view) {
        startActivity(CalendarDateActivity.class);
    }

    public void onSingleWheel(View view) {
        startActivity(SingleWheelActivity.class);
    }

    public void onDoubleWheel(View view) {
        startActivity(DoubleWheelActivity.class);
    }

    public void onAddressCity(View view) {
        startActivity(AddressWheelActivity.class);
    }

}
