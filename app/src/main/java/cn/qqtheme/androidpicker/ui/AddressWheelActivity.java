package cn.qqtheme.androidpicker.ui;

import android.view.View;

import cn.qqtheme.androidpicker.BaseActivity;
import cn.qqtheme.androidpicker.R;
import cn.qqtheme.androidpicker.custom.CustomUiAddressPicker;

/**
 * 地址滚轮选择器
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/6/23
 */
public class AddressWheelActivity extends BaseActivity {

    @Override
    protected int specifyLayoutRes() {
        return R.layout.activity_address_wheel;
    }

    public void onCustomUi(View view) {
        CustomUiAddressPicker picker = new CustomUiAddressPicker(this);
        picker.showAtBottom();
    }

}
