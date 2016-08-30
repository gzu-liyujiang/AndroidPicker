package cn.qqtheme.framework.popup;

import android.app.Activity;
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
 * @param <V> the type parameter
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

    /**
     * Instantiates a new Bottom popup.
     *
     * @param activity the activity
     */
    public BasicPopup(Activity activity) {
        this.activity = activity;
        DisplayMetrics displayMetrics = ScreenUtils.displayMetrics(activity);
        screenWidthPixels = displayMetrics.widthPixels;
        screenHeightPixels = displayMetrics.heightPixels;
        popupDialog = new PopupDialog(activity);
        popupDialog.setOnKeyListener(this);
    }

    /**
     * Gets view.
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
     * @param fillScreen the fill screen
     */
    public void setFillScreen(boolean fillScreen) {
        isFillScreen = fillScreen;
    }

    /**
     * 固定高度为屏幕的一半
     *
     * @param halfScreen the half screen
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
     * Sets content view before.
     */
    protected void setContentViewBefore() {
    }

    /**
     * Sets content view after.
     *
     * @param contentView the content view
     */
    protected void setContentViewAfter(V contentView) {
    }

    /**
     * Sets animation.
     *
     * @param animRes the anim res
     */
    public void setAnimationStyle(@StyleRes int animRes) {
        popupDialog.setAnimationStyle(animRes);
    }

    /**
     * Sets on dismiss listener.
     *
     * @param onDismissListener the on dismiss listener
     */
    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        popupDialog.setOnDismissListener(onDismissListener);
        LogUtils.verbose("popup setOnDismissListener");
    }

    /**
     * Sets size.
     *
     * @param width  the width
     * @param height the height
     */
    public void setSize(int width, int height) {
        // fixed: 2016/1/26 修复显示之前设置宽高无效问题
        this.width = width;
        this.height = height;
    }

    /**
     * Sets width.
     *
     * @param width the width
     * @see #setSize(int, int) #setSize(int, int)
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Sets height.
     *
     * @param height the height
     * @see #setSize(int, int) #setSize(int, int)
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Is showing boolean.
     *
     * @return the boolean
     */
    public boolean isShowing() {
        return popupDialog.isShowing();
    }

    /**
     * Show.
     */
    @CallSuper
    public void show() {
        onShowPrepare();
        popupDialog.show();
        LogUtils.verbose("popup show");
    }

    /**
     * Dismiss.
     */
    public void dismiss() {
        popupDialog.dismiss();
        LogUtils.verbose("popup dismiss");
    }

    /**
     * On key down boolean.
     *
     * @param keyCode the key code
     * @param event   the event
     * @return the boolean
     */
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

    /**
     * Gets window.
     *
     * @return the window
     */
    public Window getWindow() {
        return popupDialog.getWindow();
    }

    /**
     * Gets root view.
     *
     * @return the root view
     */
    public ViewGroup getRootView() {
        return popupDialog.getRootView();
    }

}
