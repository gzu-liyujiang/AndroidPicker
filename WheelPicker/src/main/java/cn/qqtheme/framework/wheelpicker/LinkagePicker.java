package cn.qqtheme.framework.wheelpicker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import cn.qqtheme.framework.popup.AbstractConfirmPopup;
import cn.qqtheme.framework.wheelview.contract.LinkageDataProvider;
import cn.qqtheme.framework.wheelview.contract.LinkageTextProvider;
import cn.qqtheme.framework.wheelview.contract.OnLinkageSelectedListener;
import cn.qqtheme.framework.wheelview.contract.TextProvider;
import cn.qqtheme.framework.wheelview.widget.LinkageWheelLayout;

/**
 * 二三级联动滚轮选择，参见 https://github.com/gzu-liyujiang/AndroidPicker
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/6/17 11:21
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class LinkagePicker<F extends LinkageTextProvider, S extends LinkageTextProvider,
        T extends TextProvider> extends AbstractConfirmPopup<LinkageWheelLayout> {

    private int wheelStyleRes = R.style.WheelLinkage;

    private LinkageWheelLayout<F, S, T> wheelLayout;
    private LinkageDataProvider<F, S, T> dataProvider;
    private OnLinkageSelectedListener<F, S, T> onLinkageSelectedListener;

    public LinkagePicker(FragmentActivity activity) {
        super(activity);
    }

    public void setWheelStyle(@StyleRes int styleRes) {
        this.wheelStyleRes = styleRes;
    }

    public void setDataProvider(LinkageDataProvider<F, S, T> dataProvider) {
        this.dataProvider = dataProvider;
    }

    public void setOnLinkageSelectedListener(OnLinkageSelectedListener<F, S, T> listener) {
        this.onLinkageSelectedListener = listener;
    }

    @NonNull
    @Override
    protected LinkageWheelLayout createBodyView(final Context context) {
        View view = View.inflate(context, R.layout.popup_wheel_linkage, null);
        wheelLayout = view.findViewById(R.id.linkage_wheel_layout);
        return wheelLayout;
    }

    @Override
    public void onViewCreated(@NonNull View contentView) {
        super.onViewCreated(contentView);
        wheelLayout.setStyle(wheelStyleRes);
        if (dataProvider != null) {
            wheelLayout.setDataProvider(dataProvider);
        }
    }

    @Override
    protected void onConfirm() {
        super.onConfirm();
        if (onLinkageSelectedListener != null) {
            F first = getWheelLayout().getFirstWheelView().getCurrentItem();
            S second = getWheelLayout().getSecondWheelView().getCurrentItem();
            T third = getWheelLayout().getThirdWheelView().getCurrentItem();
            onLinkageSelectedListener.onItemSelected(first, second, third);
        }
    }

    public final LinkageWheelLayout<F, S, T> getWheelLayout() {
        return wheelLayout;
    }

}
