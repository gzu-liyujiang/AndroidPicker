package cn.qqtheme.framework.popup;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import cn.qqtheme.framework.toolkit.CqrLog;

/**
 * 弹出层基类，用于实现弹窗、下拉菜单、底部弹出式菜单……
 * 执行顺序如下：
 * <code>
 * newXXXPopup
 * setXXXProperty
 * apply
 * onApply
 * createContentView
 * onViewCreated
 * show
 * dismiss
 * onDismiss
 * </code>
 *
 * @author liyujiang
 * @date 2018/9/22 19:46
 * @see OnApplyListener
 * @see OnDismissListener
 */
@SuppressWarnings({"WeakerAccess", "UnusedReturnValue"})
public abstract class BasePopup<P extends BasePopup> implements PopupWindow.OnDismissListener,
        LifecycleObserver, View.OnTouchListener, View.OnClickListener {
    protected static final int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;
    protected static final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;
    protected FragmentActivity activity;
    protected PopupWindow popupWindow;
    protected View contentView;
    private Drawable backgroundDrawable;
    private int animationStyle = -1;
    private OnApplyListener onApplyListener;
    private OnDismissListener onDismissListener;
    private boolean focusable = true;
    private int width = MATCH_PARENT;
    private int height = WRAP_CONTENT;
    private boolean showMaskAlpha = false;
    private View maskAlphaView;

    public BasePopup(FragmentActivity activity) {
        this.activity = activity;
        activity.getLifecycle().addObserver(this);
        popupWindow = new PopupWindow(activity);
        popupWindow.setTouchInterceptor(this);
    }

    protected P self() {
        //noinspection unchecked
        return (P) this;
    }

    public P setAnimationStyle(@StyleRes int animationStyle) {
        this.animationStyle = animationStyle;
        return self();
    }

    public P setBackgroundDrawable(Drawable drawable) {
        this.backgroundDrawable = drawable;
        return self();
    }

    public P setBackgroundColor(@ColorInt int color) {
        this.backgroundDrawable = new ColorDrawable(color);
        return self();
    }

    public P setBackgroundResource(@DrawableRes int drawableRes) {
        this.backgroundDrawable = ContextCompat.getDrawable(activity, drawableRes);
        return self();
    }

    public P setFocusable(boolean focusable) {
        this.focusable = focusable;
        return self();
    }

    public P setShowMaskAlpha(boolean showMaskAlpha) {
        this.showMaskAlpha = showMaskAlpha;
        return self();
    }

    public P setMaskAlphaIdRes(@IdRes int maskAlphaIdRes) {
        if (maskAlphaIdRes != -1) {
            maskAlphaView = activity.findViewById(maskAlphaIdRes);
        }
        return self();
    }

    public P setWidth(int width) {
        this.width = width;
        return self();
    }

    public P setHeight(int height) {
        this.height = height;
        return self();
    }

    public P setContentView(View contentView) {
        this.contentView = contentView;
        return self();
    }

    public P setContentView(@LayoutRes int layoutRes) {
        this.contentView = LayoutInflater.from(activity).inflate(layoutRes, null);
        return self();
    }

    public P setOnClickListener(@IdRes int idRes, View.OnClickListener listener) {
        if (contentView == null) {
            throw new RuntimeException("Please set content view at first");
        }
        contentView.findViewById(idRes).setOnClickListener(listener);
        return self();
    }

    public P setOnApplyListener(OnApplyListener onApplyListener) {
        this.onApplyListener = onApplyListener;
        return self();
    }

    public P setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return self();
    }

    public P apply() {
        if (onApplyListener != null) {
            onApplyListener.onApply();
        }
        popupWindow.setWidth(width);
        popupWindow.setHeight(height);
        if (animationStyle != -1) {
            popupWindow.setAnimationStyle(animationStyle);
        }
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setFocusable(focusable);
        popupWindow.setOnDismissListener(this);
        contentView = createContentView(activity);
        if (backgroundDrawable != null) {
            contentView.setBackground(backgroundDrawable);
        }
        popupWindow.setContentView(contentView);
        onViewCreated(contentView);
        return self();
    }

    protected abstract View createContentView(FragmentActivity activity);

    protected abstract void onViewCreated(@NonNull View contentView);

    public boolean isShowing() {
        return popupWindow.isShowing();
    }

    private void checkApply() {
        if (popupWindow.getContentView() != null) {
            return;
        }
        if (maskAlphaView != null) {
            maskAlphaView.setVisibility(View.VISIBLE);
        } else if (showMaskAlpha) {
            WindowManager.LayoutParams params = activity.getWindow().getAttributes();
            params.alpha = 0.5F;
            activity.getWindow().setAttributes(params);
        }
        apply();
    }

    public P showAsDropDown(View anchor) {
        return showAsDropDown(anchor, 0, 0);
    }

    public P showAsDropDown(final View anchor, final int xoff, final int yoff) {
        if (popupWindow.isShowing()) {
            return self();
        }
        checkApply();
        try {
            popupWindow.showAsDropDown(anchor, xoff, yoff);
        } catch (Throwable ignore) {
            //...not attached to window manager
            //...Unable to add window...is your activity running?
            //...Activity...has leaked window...that was originally added here
        }
        return self();
    }

    public P showAsAnchorLeftTop(final View anchor) {
        if (popupWindow.isShowing()) {
            return self();
        }
        checkApply();
        try {
            popupWindow.showAsDropDown(anchor, 0, -anchor.getMeasuredHeight());
        } catch (Throwable ignore) {
            //...not attached to window manager
        }
        return self();
    }

    public P showAsAnchorLeftBottom(final View anchor) {
        if (popupWindow.isShowing()) {
            return self();
        }
        checkApply();
        try {
            int contentPadding = 5;
            popupWindow.showAsDropDown(anchor, 0, -1 * contentPadding);
        } catch (Throwable ignore) {
            //...not attached to window manager
        }
        return self();
    }

    public P showAsAnchorRightTop(final View anchor) {
        if (popupWindow.isShowing()) {
            return self();
        }
        checkApply();
        try {
            popupWindow.showAsDropDown(anchor,
                    anchor.getMeasuredWidth() / 2 + getContentViewWidth() / 2,
                    -anchor.getMeasuredHeight());
        } catch (Throwable ignore) {
            //...not attached to window manager
        }
        return self();
    }

    public P showAsAnchorRightBottom(final View anchor) {
        if (popupWindow.isShowing()) {
            return self();
        }
        checkApply();
        try {
            int contentPadding = 5;
            popupWindow.showAsDropDown(anchor,
                    anchor.getMeasuredWidth() / 2 + getContentViewWidth() / 2,
                    -1 * contentPadding);
        } catch (Throwable ignore) {
            //...not attached to window manager
        }
        return self();
    }

    public P showAtCenter(final View anchor) {
        if (popupWindow.isShowing()) {
            return self();
        }
        checkApply();
        try {
            popupWindow.showAtLocation(anchor, Gravity.CENTER, 0, 0);
        } catch (Throwable ignore) {
            //...not attached to window manager
        }
        return self();
    }

    public P showAsAnchorCenter(final View anchor) {
        if (popupWindow.isShowing()) {
            return self();
        }
        checkApply();
        try {
            popupWindow.showAsDropDown(anchor,
                    anchor.getMeasuredWidth() / 2 - getContentViewWidth() / 2,
                    -anchor.getMeasuredHeight() / 2 - getContentViewHeight() / 2);
        } catch (Throwable ignore) {
            //...not attached to window manager
        }
        return self();
    }

    public P showAtBottom() {
        return showAtLocation(activity.findViewById(android.R.id.content), Gravity.BOTTOM, 0, 0);
    }

    public P showAtLocation(final View parent, final int gravity, final int x, final int y) {
        if (popupWindow.isShowing()) {
            return self();
        }
        checkApply();
        try {
            popupWindow.showAtLocation(parent, gravity, x, y);
        } catch (Throwable ignore) {
            //...not attached to window manager
        }
        return self();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    public void onAny(LifecycleOwner source, Lifecycle.Event event) {
        CqrLog.d("lifecycle: " + event.name());
        if (event == Lifecycle.Event.ON_DESTROY) {
            activity.getLifecycle().removeObserver(this);
            dismiss();
        }
    }

    public void dismiss() {
        if (!popupWindow.isShowing()) {
            return;
        }
        popupWindow.dismiss();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            popupWindow.dismiss();
            return true;
        }
        return false;
    }

    @Override
    public void onDismiss() {
        if (maskAlphaView != null) {
            maskAlphaView.setVisibility(View.GONE);
        } else if (showMaskAlpha) {
            WindowManager.LayoutParams params = activity.getWindow().getAttributes();
            params.alpha = 1.0F;
            activity.getWindow().setAttributes(params);
        }
        if (onDismissListener != null) {
            onDismissListener.onDismiss();
        }
    }

    @Override
    public void onClick(View v) {
    }

    public final FragmentActivity getActivity() {
        return activity;
    }

    public final PopupWindow getWindow() {
        return popupWindow;
    }

    public final View getContentView() {
        return contentView;
    }

    public final int getContentViewWidth() {
        View contentView = popupWindow.getContentView();
        int width = contentView.getWidth();
        if (width == 0) {
            contentView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            return contentView.getMeasuredWidth();
        } else {
            return width;
        }
    }

    public final int getContentViewHeight() {
        View contentView = popupWindow.getContentView();
        int height = contentView.getHeight();
        if (height == 0) {
            contentView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            return contentView.getMeasuredHeight();
        } else {
            return height;
        }
    }

    public interface OnApplyListener {
        void onApply();
    }

    public interface OnDismissListener extends android.widget.PopupWindow.OnDismissListener {
    }

}
