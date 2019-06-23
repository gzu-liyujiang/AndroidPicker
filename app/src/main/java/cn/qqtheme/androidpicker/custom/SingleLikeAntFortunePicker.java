package cn.qqtheme.androidpicker.custom;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;

import cn.qqtheme.framework.wheelpicker.SinglePicker;
import cn.qqtheme.framework.wheelview.widget.WheelView;

/**
 * 蚂蚁财富风格的单项选择器
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/6/23
 */
public class SingleLikeAntFortunePicker extends SinglePicker<String> {
    private String titleText;

    public SingleLikeAntFortunePicker(FragmentActivity activity) {
        super(activity);
        setShowMaskAlpha(false);
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    @Override
    public void onViewCreated(@NonNull View contentView) {
        super.onViewCreated(contentView);
        getHeaderDividerView().setBackgroundColor(0xFFDCDCDC);
        TextView cancelTextView = getCancelTextView();
        cancelTextView.setText("取消");
        cancelTextView.setTextColor(0xFF3355E5);
        TextView confirmTextView = getConfirmTextView();
        confirmTextView.setTextColor(0xFF3355E5);
        confirmTextView.setText("确定");
        TextView titleTextView = getTitleTextView();
        titleTextView.setTextColor(0xFF333333);
        titleTextView.setText(titleText);
        WheelView<String> bodyView = getBodyView();
        bodyView.setAtmospheric(true);
        bodyView.setVisibleItemCount(7);
        bodyView.setCyclic(false);
        bodyView.setIndicator(true);
        bodyView.setIndicatorColor(0xFFDCDCDC);
        bodyView.setIndicatorSize(ConvertUtils.dp2px(0.6F));
        bodyView.setTextColor(0xFF999999);
        bodyView.setSelectedTextColor(0xFF333333);
        bodyView.setCurtain(false);
        bodyView.setCurved(false);
    }

}
