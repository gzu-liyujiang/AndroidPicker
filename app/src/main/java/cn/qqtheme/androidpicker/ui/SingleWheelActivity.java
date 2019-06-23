package cn.qqtheme.androidpicker.ui;

import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.qqtheme.androidpicker.BaseActivity;
import cn.qqtheme.androidpicker.R;
import cn.qqtheme.androidpicker.bean.GoodsCategoryBean;
import cn.qqtheme.androidpicker.custom.SingleLikeAntFortunePicker;
import cn.qqtheme.androidpicker.custom.SingleLikeMobileQqPicker;
import cn.qqtheme.androidpicker.custom.SingleLikeQqBrowserPicker;
import cn.qqtheme.framework.wheelpicker.PhoneCodePicker;
import cn.qqtheme.framework.wheelpicker.SinglePicker;

/**
 * 单项滚轮选择器
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/6/23
 */
public class SingleWheelActivity extends BaseActivity {

    @Override
    protected int specifyLayoutRes() {
        return R.layout.activity_single_wheel;
    }

    public void onSingleText(View view) {
        PhoneCodePicker picker = new PhoneCodePicker(this);
        picker.setMaskAlphaIdRes(R.id.alpha_mask_for_popup);
        picker.setSelectCallback(new PhoneCodePicker.SelectCallback() {
            @Override
            public void onItemSelected(int position, String item) {
            }
        });
        picker.showAtBottom();
    }

    public void onSingleBean(View view) {
        List<GoodsCategoryBean> data = new ArrayList<>();
        data.add(new GoodsCategoryBean(1, "食品生鲜"));
        data.add(new GoodsCategoryBean(2, "家用电器"));
        data.add(new GoodsCategoryBean(3, "家居生活"));
        data.add(new GoodsCategoryBean(4, "医疗保健"));
        data.add(new GoodsCategoryBean(5, "酒水饮料"));
        data.add(new GoodsCategoryBean(6, "图书音像"));
        SinglePicker<GoodsCategoryBean> picker = new SinglePicker<>(this);
        picker.setMaskAlphaIdRes(R.id.alpha_mask_for_popup);
        picker.setData(data);
        picker.setDefaultItemPosition(1);
        picker.showAtBottom();
    }

    public void onLikeMobileQq(View view) {
        SingleLikeMobileQqPicker picker = new SingleLikeMobileQqPicker(this);
        picker.showAtBottom();
    }

    public void onLikeQqBrowser(View view) {
        SingleLikeQqBrowserPicker picker = new SingleLikeQqBrowserPicker(this);
        picker.setMaskAlphaIdRes(R.id.alpha_mask_for_popup);
        picker.showAtBottom();
    }

    public void onLikeAntFortune(View view) {
        SingleLikeAntFortunePicker picker = new SingleLikeAntFortunePicker(this);
        picker.setMaskAlphaIdRes(R.id.alpha_mask_for_popup);
        picker.setTitleText("定投周期");
        picker.setData(Arrays.asList("每周", "每两周", "每月"));
        picker.showAtBottom();
    }

}
