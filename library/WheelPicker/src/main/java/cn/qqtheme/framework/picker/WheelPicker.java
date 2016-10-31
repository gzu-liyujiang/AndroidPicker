package cn.qqtheme.framework.picker;

import android.app.Activity;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.view.View;

import cn.qqtheme.framework.popup.ConfirmPopup;
import cn.qqtheme.framework.widget.WheelView;

/**
 * 滑轮选择器
 *
 * @author 李玉江[QQ:1032694760]
 * @since 2015/12/22
 */
public abstract class WheelPicker extends ConfirmPopup<View> {
    protected int textSize = WheelView.TEXT_SIZE;
    protected int textColorNormal = WheelView.TEXT_COLOR_NORMAL;
    protected int textColorFocus = WheelView.TEXT_COLOR_FOCUS;
    protected int lineColor = WheelView.LINE_COLOR;
    protected boolean lineVisible = true;
    protected int offset = WheelView.OFF_SET;

    public WheelPicker(Activity activity) {
        super(activity);
    }

    /**
     * 设置文字大小
     */
    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    /**
     * 设置文字颜色
     */
    public void setTextColor(@ColorInt int textColorFocus, @ColorInt int textColorNormal) {
        this.textColorFocus = textColorFocus;
        this.textColorNormal = textColorNormal;
    }

    /**
     * 设置文字颜色
     */
    public void setTextColor(@ColorInt int textColor) {
        this.textColorFocus = textColor;
    }

    /**
     * 设置分隔线是否可见
     */
    public void setLineVisible(boolean lineVisible) {
        this.lineVisible = lineVisible;
    }

    /**
     * 设置分隔线颜色
     */
    public void setLineColor(@ColorInt int lineColor) {
        this.lineColor = lineColor;
    }

    /**
     * 设置选项偏移量，默认为1，范围为1-4。
     * 1显示三条、2显示5条、3显示7条、4显示9条
     */
    public void setOffset(@IntRange(from = 1, to = 4) int offset) {
        this.offset = offset;
    }

}
