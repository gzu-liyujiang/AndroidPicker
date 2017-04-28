package cn.qqtheme.framework.picker;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

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
    protected WheelView.DividerConfig dividerConfig;
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
     * 设置分隔阴影是否可见
     */
    public void setShadowVisible(boolean shadowVisible) {
        if (null == dividerConfig) {
            dividerConfig = new WheelView.DividerConfig();
        }
        dividerConfig.setShadowVisible(shadowVisible);
    }

    /**
     * 设置分隔线是否可见
     */
    public void setDividerVisible(boolean visible) {
        if (null == dividerConfig) {
            dividerConfig = new WheelView.DividerConfig();
        }
        dividerConfig.setVisible(visible);
    }

    /**
     * @deprecated use {@link #setDividerVisible(boolean)} instead
     */
    @Deprecated
    public void setLineVisible(boolean visible) {
        setDividerVisible(visible);
    }

    /**
     * @deprecated use {@link #setDividerColor(int)} instead
     */
    @Deprecated
    public void setLineColor(@ColorInt int color) {
        setDividerColor(color);
    }

    /**
     * 设置分隔线颜色
     */
    public void setDividerColor(@ColorInt int lineColor) {
        if (null == dividerConfig) {
            dividerConfig = new WheelView.DividerConfig();
        }
        dividerConfig.setVisible(true);
        dividerConfig.setColor(lineColor);
    }

    /**
     * 设置分隔线配置项，设置null将隐藏分割线及阴影
     */
    public void setDividerConfig(@Nullable WheelView.DividerConfig config) {
        if (null == config) {
            dividerConfig = new WheelView.DividerConfig();
            dividerConfig.setVisible(false);
            dividerConfig.setShadowVisible(false);
        } else {
            dividerConfig = config;
        }
    }

    /**
     * @deprecated use {@link #setDividerConfig(WheelView.DividerConfig)} instead
     */
    @Deprecated
    public void setLineConfig(WheelView.DividerConfig config) {
        setDividerConfig(config);
    }

    /**
     * 设置选项偏移量，可用来要设置显示的条目数，范围为1-5。
     * 1显示3条、2显示5条、3显示7条……
     */
    public void setOffset(@IntRange(from = 1, to = 5) int offset) {
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

    @Override
    protected void showAfter() {
        super.showAfter();
        View view = getRootView();
        //使用透明渐变位移动画，缓解选中项显示跳动问题
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 0, 1);
        ObjectAnimator translation = ObjectAnimator.ofFloat(view, "translationY", 300, 0);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(alpha, translation);
        animatorSet.setDuration(200);
        animatorSet.setInterpolator(new AccelerateInterpolator());
        animatorSet.start();
    }


    @Override
    public void dismiss() {
        View view = getRootView();
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 1, 0);
        ObjectAnimator translation = ObjectAnimator.ofFloat(view, "translationY", 0, 300);
        animatorSet.playTogether(alpha, translation);
        animatorSet.setDuration(200);
        animatorSet.setInterpolator(new AccelerateInterpolator());
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                dismissImmediately();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }

}
