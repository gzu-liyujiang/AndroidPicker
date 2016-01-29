package cn.qqtheme.framework.popup;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.CallSuper;
import android.support.annotation.StyleRes;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import cn.qqtheme.framework.util.ScreenUtils;
import cn.qqtheme.framework.util.LogUtils;

/**
 * 底部弹窗基类
 *
 * @param <V> the type parameter
 * @author 李玉江[QQ :1023694760]
 * @version 2015/7/19
 */
public abstract class BottomPopup<V extends View> implements DialogInterface.OnKeyListener {
    /**
     * The constant MATCH_PARENT.
     */
    public static final int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;
    /**
     * The constant WRAP_CONTENT.
     */
    public static final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;
    protected Activity activity;
    protected int screenWidthPixels;
    protected int screenHeightPixels;
    private Popup popup;
    private int width = 0, height = 0;
    private boolean isFillScreen = false;
    private boolean isHalfScreen = false;

    /**
     * Instantiates a new Bottom popup.
     *
     * @param activity the activity
     */
    public BottomPopup(Activity activity) {
        this.activity = activity;
        DisplayMetrics displayMetrics = ScreenUtils.displayMetrics(activity);
        screenWidthPixels = displayMetrics.widthPixels;
        screenHeightPixels = displayMetrics.heightPixels;
        popup = new Popup(activity);
        popup.setOnKeyListener(this);
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
        setContentViewBefore();
        V view = makeContentView();
        popup.setContentView(view);// 设置弹出窗体的布局
        setContentViewAfter(view);
        LogUtils.debug("do something before popup show");
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
        }
        popup.setSize(width, height);
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
        popup.setAnimationStyle(animRes);
    }

    /**
     * Sets on dismiss listener.
     *
     * @param onDismissListener the on dismiss listener
     */
    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        popup.setOnDismissListener(onDismissListener);
        LogUtils.debug("popup setOnDismissListener");
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
        return popup.isShowing();
    }

    /**
     * Show.
     */
    @CallSuper
    public void show() {
        onShowPrepare();
        popup.show();
        LogUtils.debug("popup show");
    }

    /**
     * Dismiss.
     */
    public void dismiss() {
        popup.dismiss();
        LogUtils.debug("popup dismiss");
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
        return popup.getWindow();
    }

    /**
     * Gets root view.
     *
     * @return the root view
     */
    public ViewGroup getRootView() {
        return popup.getRootView();
    }

}
