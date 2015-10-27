package cn.qqtheme.framework.popup;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.*;

import cn.qqtheme.framework.helper.Common;
import cn.qqtheme.framework.helper.LogUtils;

/**
 * 弹窗基类
 *
 * @author 李玉江[QQ:1023694760]
 * @since 2015/7/19
 * Created by IntelliJ IDEA
 */
public abstract class BasePopup<V extends View> extends PopupWindow {
    protected static final int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;
    protected static final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;
    protected Activity activity;
    protected int screenWidth;
    protected int screenHeight;
    private LayoutParams layoutParams;
    private V view;

    protected abstract V getView();

    public BasePopup(Activity activity) {
        super(activity);
        this.activity = activity;
        layoutParams = activity.getWindow().getAttributes();
        int[] pixels = Common.getPixels(activity);
        screenWidth = pixels[0];
        screenHeight = pixels[1];
        LogUtils.debug("screen width=" + screenWidth + ", screen height=" + screenHeight);
        setWidth(MATCH_PARENT);
        setHeight(WRAP_CONTENT);
        setFocusable(true);
        setAnimationStyle(android.R.style.Animation_Dialog);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    /**
     * 弹出窗显示之前调用
     */
    private void onShowPrepare() {
        setContentViewBefore();
        view = getView();//在构造函数初始化之后且窗口显示之前调用，以便可以设置视图各种参数，如设置图片
        view.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        setContentView(view);// 设置弹出窗体的布局
        setContentViewAfter(view);
        LogUtils.debug("do something before popup window show");
        layoutParams.alpha = 0.7f;
        layoutParams.dimAmount = 0.7f;
        activity.getWindow().setAttributes(layoutParams);
    }

    protected void setContentViewBefore() {
    }

    protected void setContentViewAfter(View contentView) {
    }

    protected void checkMaxHeight(View v) {
        checkMaxHeight(Common.calculateHeight(v));
    }

    protected void checkMaxHeight(int height) {
        int exceptedHeight = screenHeight / 2;
        if (height > exceptedHeight) {
            setHeight(exceptedHeight);//高度不允许超过屏幕的一半
        }
    }

    @Override
    public void setWidth(int width) {
        LogUtils.debug("will set popup window width to: " + width);
        super.setWidth(width);
    }

    @Override
    public void setHeight(int height) {
        LogUtils.debug("will set popup window height to: " + height);
        super.setHeight(height);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        layoutParams.alpha = 1.0f;
        layoutParams.dimAmount = 0.0f;
        activity.getWindow().setAttributes(layoutParams);
    }

    /**
     * Use {@link #showAtBottom()} instead
     */
    @Deprecated
    @Override
    public final void showAtLocation(View parent, int gravity, int x, int y) {
        onShowPrepare();
        if (parent == null) {
            parent = view;
        }
        super.showAtLocation(parent, gravity, x, y);
    }

    /**
     * Use {@link #showAtBottom()} instead
     */
    @Deprecated
    @Override
    public final void showAsDropDown(View anchor) {
        onShowPrepare();
        super.showAsDropDown(anchor);
    }

    /**
     * Use {@link #showAtBottom()} instead
     */
    @Deprecated
    @Override
    public final void showAsDropDown(View anchor, int xoff, int yoff) {
        onShowPrepare();
        super.showAsDropDown(anchor, xoff, yoff);
    }

    /**
     * Use {@link #showAtBottom()} instead
     */
    @Deprecated
    @Override
    public final void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        onShowPrepare();
        super.showAsDropDown(anchor, xoff, yoff, gravity);
    }

    public final void showAtBottom() {
        onShowPrepare();
        View parent = activity.getWindow().getDecorView();
        super.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }

}
