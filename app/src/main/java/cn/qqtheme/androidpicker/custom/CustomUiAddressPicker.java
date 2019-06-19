package cn.qqtheme.androidpicker.custom;

import android.support.v4.app.FragmentActivity;
import android.view.View;

import java.util.List;

import cn.qqtheme.androidpicker.R;
import cn.qqtheme.framework.popup.AbstractConfirmPopup;
import cn.qqtheme.framework.wheelpicker.AddressPicker;
import cn.qqtheme.framework.wheelpicker.annotation.AddressMode;
import cn.qqtheme.framework.wheelpicker.entity.ProvinceEntity;

/**
 * 自定义地址选择器
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/6/17 19:12
 */
public class CustomUiAddressPicker extends AddressPicker {

    public CustomUiAddressPicker(FragmentActivity activity) {
        super(activity);
        setAddressMode(AddressMode.PROVINCE_CITY_COUNTY);
        setLayoutProvider(new AddressLayoutProvider());
    }

    @Override
    public void onDataLoadSuccess(List<ProvinceEntity> data) {
        super.onDataLoadSuccess(data);
        getWheelLayout().setCyclic(true);
        getWheelLayout().setIndicator(false);
        View countyHintView = contentView.findViewById(R.id.mine_address_county_hint);
        if (isOnlyTwoLevel()) {
            countyHintView.setVisibility(View.GONE);
        } else {
            countyHintView.setVisibility(View.VISIBLE);
        }
        getCancelTextView().setTextColor(0xFFF3816F);
    }

    static class AddressLayoutProvider implements AbstractConfirmPopup.LayoutProvider {

        @Override
        public int provideLayoutRes() {
            return R.layout.popup_custom_ui_wheel_address;
        }

        @Override
        public int specifyCancelIdRes() {
            return R.id.tv_cancel;
        }

        @Override
        public int specifyTitleIdRes() {
            return R.id.tv_title;
        }

        @Override
        public int specifyConfirmIdRes() {
            return R.id.tv_confirm;
        }

        @Override
        public int specifyBodyIdRes() {
            return R.id.fl_body;
        }

    }

}

