package cn.qqtheme.androidpicker.custom;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;

import cn.qqtheme.framework.wheelpicker.ConstellationPicker;
import cn.qqtheme.framework.wheelview.widget.WheelView;

/**
 * 手机QQ风格的单项选择器
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/6/23
 */
public class SingleLikeMobileQqPicker extends ConstellationPicker {

    public SingleLikeMobileQqPicker(FragmentActivity activity) {
        super(activity, true);
        setShowMaskAlpha(false);
    }

    @Override
    public void onViewCreated(@NonNull View contentView) {
        super.onViewCreated(contentView);
        contentView.setBackgroundColor(0xFFF2F2F2);

        View topBorderView = getTopBorderView();
        topBorderView.setVisibility(View.VISIBLE);
        topBorderView.setBackgroundColor(0xFFDCDCDC);
        ViewGroup.LayoutParams topBorderParams = topBorderView.getLayoutParams();
        topBorderParams.height = ConvertUtils.dp2px(1.2F);
        topBorderView.setLayoutParams(topBorderParams);

        View headerDividerView = getHeaderDividerView();
        headerDividerView.setVisibility(View.VISIBLE);
        headerDividerView.setBackgroundColor(0xFFDCDCDC);
        ViewGroup.LayoutParams headerDividerParams = headerDividerView.getLayoutParams();
        headerDividerParams.height = ConvertUtils.dp2px(1.2F);
        headerDividerView.setLayoutParams(headerDividerParams);

        getCancelTextView().setVisibility(View.GONE);

        TextView confirmTextView = getConfirmTextView();
        confirmTextView.setTextColor(0xFF33B5E5);
        confirmTextView.setText("完成");

        WheelView<String> bodyView = getBodyView();
        int padding = ConvertUtils.dp2px(10F);
        bodyView.setPadding(0, padding, 0, padding);
        bodyView.setAtmospheric(true);
        bodyView.setCurved(true);
        bodyView.setVisibleItemCount(7);
        bodyView.setCyclic(false);
        bodyView.setIndicator(true);
        bodyView.setIndicatorColor(0xFFDCDCDC);
        bodyView.setIndicatorSize(ConvertUtils.dp2px(1.2F));
        bodyView.setTextColor(0xFF666666);
        bodyView.setSelectedTextColor(0xFF333333);
        bodyView.setCurtain(false);
    }

}
