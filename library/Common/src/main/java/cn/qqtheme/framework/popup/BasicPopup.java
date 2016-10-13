package cn.qqtheme.framework.popup;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.CallSuper;
import android.support.annotation.StyleRes;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import cn.qqtheme.framework.util.ScreenUtils;
import cn.qqtheme.framework.util.LogUtils;

/**
 * 弹窗基类
 *
 * @param <V> 弹窗的内容视图类型
 * @author 李玉江[QQ:1023694760]
 * @since 2015/7/19
 */
public abstract class BasicPopup<V extends View> implements DialogInterface.OnKeyListener {
    public static final int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;
    public static final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;
    protected Activity activity;
    protected int screenWidthPixels;
    protected int screenHeightPixels;
    private PopupDialog popupDialog;
    private int width = 0, height = 0;
    private boolean isFillScreen = false;
    private boolean isHalfScreen = false;
    private boolean isPrepared = false;
    private int gravity = Gravity.BOTTOM;//默认位于屏幕底部

    public BasicPopup(Activity activity) {
        this.activity = activity;
        DisplayMetrics displayMetrics = ScreenUtils.displayMetrics(activity);
        screenWidthPixels = displayMetrics.widthPixels;
        screenHeightPixels = displayMetrics.heightPixels;
        popupDialog = new PopupDialog(activity);
        popupDialog.setOnKeyListener(this);
    }

    /**
     * 创建弹窗的内容视图
     *
     * @return the view
     */
    protected abstract V makeContentView();

    /**
     * 弹出窗显示之前调用
     */
    private void onShowPrepare() {
        if (isPrepared) {
            return;
        }
        popupDialog.getWindow().setGravity(gravity);
        setContentViewBefore();
        V view = makeContentView();
        popupDialog.setContentView(view);// 设置弹出窗体的布局
        setContentViewAfter(view);
        LogUtils.verbose("do something before popup show");
        if (width == 0 && height == 0) {
            //未明确指定宽高，优先考虑全屏再考虑半屏然后再考虑包裹内容
            width = screenWidthPixels;
            if (isFillScreen) {
                height = MATCH_PARENT;
            } else if (isHalfScreen) {
                height = screenHeightPixels / 2;
            } else {
                height = WRAP_CONTENT;
            }
        } else if (width == 0) {
            width = screenWidthPixels;
        } else if (height == 0) {
            height = WRAP_CONTENT;
        }
        popupDialog.setSize(width, height);
        isPrepared = true;
    }

    /**
     * 固定高度为屏幕的高
     *
     * @param fillScreen true为全屏
     */
    public void setFillScreen(boolean fillScreen) {
        isFillScreen = fillScreen;
    }

    /**
     * 固定高度为屏幕的一半
     *
     * @param halfScreen true为半屏
     */
    public void setHalfScreen(boolean halfScreen) {
        isHalfScreen = halfScreen;
    }

    /**
     * 位于屏幕何处
     *
     * @see Gravity
     */
    public void setGravity(int gravity) {
        this.gravity = gravity;
        if (gravity == Gravity.CENTER) {
            setWidth((int) (screenWidthPixels * 0.7));
        }
    }

    /**
     * 设置弹窗的内容视图之前执行
     */
    protected void setContentViewBefore() {
    }

    /**
     * 设置弹窗的内容视图之后执行
     *
     * @param contentView 弹窗的内容视图
     */
    protected void setContentViewAfter(V contentView) {
    }

    public void setAnimationStyle(@StyleRes int animRes) {
        popupDialog.setAnimationStyle(animRes);
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        popupDialog.setOnDismissListener(onDismissListener);
        LogUtils.verbose("popup setOnDismissListener");
    }

    /**
     * 设置弹窗的宽和高
     *
     * @param width  宽
     * @param height 高
     */
    public void setSize(int width, int height) {
        // fixed: 2016/1/26 修复显示之前设置宽高无效问题
        this.width = width;
        this.height = height;
    }

    /**
     * 设置弹窗的宽
     *
     * @param width 宽
     * @see #setSize(int, int)
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * 设置弹窗的高
     *
     * @param height 高
     * @see #setSize(int, int)
     */
    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isShowing() {
        return popupDialog.isShowing();
    }

    @CallSuper
    public void show() {
        onShowPrepare();
        popupDialog.show();
        LogUtils.verbose("popup show");
    }

    public void dismiss() {
        popupDialog.dismiss();
        LogUtils.verbose("popup dismiss");
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public final boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            return onKeyDown(keyCode, event);
        }
        return false;
    }

    public Context getContext() {
        return popupDialog.getContext();
    }

    public Window getWindow() {
        return popupDialog.getWindow();
    }

    /**
     * 弹框的内容视图
     */
    public View getContentView() {
        return popupDialog.getContentView();
    }

    /**
     * 弹框的根视图
     */
    public ViewGroup getRootView() {
        return popupDialog.getRootView();
    }

}
