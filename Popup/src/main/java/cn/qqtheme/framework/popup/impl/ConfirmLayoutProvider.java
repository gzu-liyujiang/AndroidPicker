package cn.qqtheme.framework.popup.impl;


import cn.qqtheme.framework.popup.R;
import cn.qqtheme.framework.popup.contract.LayoutProvider;

/**
 * 确认选择弹窗的布局提供者
 *
 * @author liyujiang
 * @date 2019/5/8 10:04
 */
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
