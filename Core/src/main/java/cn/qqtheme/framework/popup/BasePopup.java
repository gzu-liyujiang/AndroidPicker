package cn.qqtheme.framework.popup;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import cn.qqtheme.framework.tool.ScreenHelper;
import cn.qqtheme.framework.util.LogUtils;
import cn.qqtheme.framework.util.ViewUtils;

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
    protected ScreenHelper.Screen screen;
    private V view;

    protected abstract V getView();

    public BasePopup(Activity activity) {
        super(activity);
        this.activity = activity;
        screen = ScreenHelper.getScreenPixels(activity);
        LogUtils.debug("screen width=" + screen.widthPixels + ", screen height=" + screen.heightPixels);
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
    }

    protected void setContentViewBefore() {
    }

    protected void setContentViewAfter(View contentView) {
    }

    protected void checkMaxHeight(View v) {
        checkMaxHeight(ViewUtils.calculateHeight(v));
    }

    protected void checkMaxHeight(int height) {
        int exceptedHeight = screen.heightPixels / 2;
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
    public void setOnDismissListener(OnDismissListener onDismissListener) {
        super.setOnDismissListener(onDismissListener);
        LogUtils.debug("popup window setOnDismissListener");
    }

    @Override
    public void dismiss() {
        super.dismiss();
        LogUtils.debug("popup window dismiss");
    }

    @Override
    public final void showAtLocation(View parent, int gravity, int x, int y) {
        onShowPrepare();
        if (parent == null) {
            parent = view;
        }
        super.showAtLocation(parent, gravity, x, y);
    }

    @Override
    public final void showAsDropDown(View anchor) {
        onShowPrepare();
        super.showAsDropDown(anchor);
    }

    @Override
    public final void showAsDropDown(View anchor, int xoff, int yoff) {
        onShowPrepare();
        super.showAsDropDown(anchor, xoff, yoff);
    }

    @Override
    public final void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        onShowPrepare();
        super.showAsDropDown(anchor, xoff, yoff, gravity);
    }

    /**
     * @see Gravity
     */
    public final void showAtGravity(int gravity) {
        onShowPrepare();
        View parent = activity.getWindow().getDecorView();
        super.showAtLocation(parent, gravity, 0, 0);
    }

}
