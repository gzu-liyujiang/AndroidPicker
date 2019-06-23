package cn.qqtheme.androidpicker.custom;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;

import cn.qqtheme.framework.wheelpicker.SexPicker;
import cn.qqtheme.framework.wheelview.widget.WheelView;

/**
 * QQ浏览器风格的单项选择器
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/6/23
 */
public class SingleLikeQqBrowserPicker extends SexPicker {

    public SingleLikeQqBrowserPicker(FragmentActivity activity) {
        super(activity, true);
        setShowMaskAlpha(true);
        setItemWidthInDp(120);
    }

    @Override
    public void onViewCreated(@NonNull View contentView) {
        super.onViewCreated(contentView);
        getHeaderDividerView().setVisibility(View.GONE);
        TextView cancelTextView = getCancelTextView();
        cancelTextView.setText("取消");
        cancelTextView.setTextColor(0xFF999999);
        TextView confirmTextView = getConfirmTextView();
        confirmTextView.setTextColor(0xFF3355E5);
        confirmTextView.setText("确定");
        WheelView<String> bodyView = getBodyView();
        bodyView.setVisibleItemCount(Math.min(7, getData().size()));
        bodyView.setAtmospheric(true);
        bodyView.setCurved(false);
        bodyView.setCurtain(false);
        bodyView.setCyclic(false);
        bodyView.setIndicator(true);
        bodyView.setIndicatorColor(0xFFDCDCDC);
        bodyView.setIndicatorSize(ConvertUtils.dp2px(0.6F));
        bodyView.setTextColor(0xFF999999);
        bodyView.setSelectedTextColor(0xFF333333);
    }

}

