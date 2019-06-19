package cn.qqtheme.framework.popup.impl;


import cn.qqtheme.framework.popup.R;
import cn.qqtheme.framework.popup.contract.LayoutProvider;

public class ConfirmLayoutProvider implements LayoutProvider {

    @Override
    public int provideLayoutRes() {
        return R.layout.popup_confirm;
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
