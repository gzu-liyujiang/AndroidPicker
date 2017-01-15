package cn.qqtheme.framework.picker;

import android.app.Activity;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
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
    protected int offset = WheelView.ITEM_OFF_SET;
    protected boolean cycleDisable = true;
    protected WheelView.LineConfig lineConfig;
    private View contentView;

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
        if (null == lineConfig) {
            lineConfig = new WheelView.LineConfig();
        }
        lineConfig.setVisible(lineVisible);
    }

    /**
     * 设置分隔阴影是否可见
     */
    public void setShadowVisible(boolean shadowVisible) {
        if (null == lineConfig) {
            lineConfig = new WheelView.LineConfig();
        }
        lineConfig.setShadowVisible(shadowVisible);
    }

    /**
     * 设置分隔线颜色
     */
    public void setLineColor(@ColorInt int lineColor) {
        if (null == lineConfig) {
            lineConfig = new WheelView.LineConfig();
        }
        lineConfig.setVisible(true);
        lineConfig.setColor(lineColor);
    }

    /**
     * 设置分隔线配置项，设置null将隐藏分割线及阴影
     */
    public void setLineConfig(@Nullable WheelView.LineConfig config) {
        if (null == config) {
            lineConfig = new WheelView.LineConfig();
            lineConfig.setVisible(false);
            lineConfig.setShadowVisible(false);
        } else {
            lineConfig = config;
        }
    }

    /**
     * 设置选项偏移量，可用来要设置显示的条目数，范围为1-3。
     * 1显示3条、2显示5条、3显示7条
     */
    public void setOffset(@IntRange(from = 1, to = 3) int offset) {
        this.offset = offset;
    }

    /**
     * 设置是否禁用循环
     */
    public void setCycleDisable(boolean cycleDisable) {
        this.cycleDisable = cycleDisable;
    }

    /**
     * 得到选择器视图，可内嵌到其他视图容器
     */
    @Override
    public View getContentView() {
        if (null == contentView) {
            contentView = makeCenterView();
        }
        return contentView;
    }

}
