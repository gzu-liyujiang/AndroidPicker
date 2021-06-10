/*
 * Copyright (c) 2016-present 贵州纳雍穿青人李裕江<1032694760@qq.com>
 *
 * The software is licensed under the Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *     http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 * PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package com.github.gzuliyujiang.basepicker;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.CallSuper;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

/**
 * 屏幕底部弹出对话框
 *
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2017/4/12
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class BottomPicker extends Dialog implements DialogInterface.OnShowListener, DialogInterface.OnDismissListener {
    public static final int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;
    public static final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;
    protected Activity activity;
    protected View contentView;
    protected View maskView;

    public BottomPicker(@NonNull Activity activity) {
        super(activity);
        init(activity);
    }

    public BottomPicker(@NonNull Activity activity, @StyleRes int themeResId) {
        super(activity, themeResId);
        init(activity);
    }

    private void init(Activity activity) {
        this.activity = activity;
        onInit(activity);
        PickerLog.print("dialog onInit");
        setOwnerActivity(activity);
        //触摸屏幕取消窗体
        setCanceledOnTouchOutside(false);
        //按返回键取消窗体
        setCancelable(false);
        super.setOnShowListener(this);
        super.setOnDismissListener(this);
        Window window = super.getWindow();
        if (window != null) {
            //requestFeature must be called before adding content
            window.requestFeature(Window.FEATURE_NO_TITLE);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(activity.getResources().getDisplayMetrics().widthPixels, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER);
            window.getDecorView().setPadding(0, 0, 0, 0);
        }
        // 调用create或show才能触发onCreate
        create();
    }

    protected void onInit(@NonNull Context context) {

    }

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PickerLog.print("dialog onCreate");
        contentView = createContentView(activity);
        contentView.setFocusable(true);
        contentView.setFocusableInTouchMode(true);
        setContentView(contentView);
        if (enableMaskView()) {
            addMaskView();
        }
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        setWidth(activity.getResources().getDisplayMetrics().widthPixels);
        setGravity(Gravity.BOTTOM);
        initView(contentView);
        PickerLog.print("dialog initView");
    }

    @NonNull
    protected abstract View createContentView(@NonNull Activity activity);

    protected void initView(@NonNull View contentView) {

    }

    protected boolean enableMaskView() {
        return true;
    }

    private void addMaskView() {
        try {
            // 取消弹窗遮罩效果，通过自定义遮罩层视图解决自带弹窗遮罩致使系统导航栏亮度突兀问题
            setDimAmount(0);
            WindowManager.LayoutParams params = new WindowManager.LayoutParams();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            Point screenRealSize = new Point();
            activity.getWindowManager().getDefaultDisplay().getRealSize(screenRealSize);
            int navBarIdentifier = activity.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            params.height = screenRealSize.y - activity.getResources().getDimensionPixelSize(navBarIdentifier);
            params.gravity = Gravity.TOP;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                // 取消弹窗遮罩效果后，异形屏的状态栏没法被自定义的遮罩试图挡住，需结合systemUiVisibility
                params.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            }
            params.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            params.flags = WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS;
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
            params.format = PixelFormat.TRANSLUCENT;
            params.token = activity.getWindow().getDecorView().getWindowToken();
            maskView = new View(activity);
            maskView.setBackgroundColor(0x7F000000);
            maskView.setFitsSystemWindows(false);
            maskView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        dismiss();
                        return true;
                    }
                    return false;
                }
            });
            activity.getWindowManager().addView(maskView, params);
        } catch (Exception ignore) {

        }
    }

    public final Activity getActivity() {
        return activity;
    }

    public final View getContentView() {
        return contentView;
    }

    public final void setBackground(Drawable background) {
        if (contentView == null) {
            return;
        }
        contentView.setBackground(background);
    }

    public final void setBackgroundColor(boolean round, @ColorInt int color) {
        if (contentView == null) {
            return;
        }
        if (!round) {
            contentView.setBackgroundColor(color);
            return;
        }
        float radiusInPX = contentView.getResources().getDisplayMetrics().density * 25;
        float[] outerR = new float[]{radiusInPX, radiusInPX, radiusInPX, radiusInPX, 0, 0, 0, 0};
        ShapeDrawable drawable = new ShapeDrawable(new RoundRectShape(outerR, null, null));
        Paint paint = drawable.getPaint();
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        contentView.setBackground(drawable);
    }

    public final void setBackgroundColor(@ColorInt int color) {
        if (contentView == null) {
            return;
        }
        contentView.setBackgroundColor(color);
    }

    public final void setBackgroundResource(@DrawableRes int resId) {
        if (contentView == null) {
            return;
        }
        contentView.setBackgroundResource(resId);
    }

    public final void setLayout(int width, int height) {
        getWindow().setLayout(width, height);
    }

    public final void setWidth(int width) {
        getWindow().setLayout(width, getWindow().getAttributes().height);
    }

    public final void setHeight(int height) {
        getWindow().setLayout(getWindow().getAttributes().width, height);
    }

    public final void setGravity(int gravity) {
        getWindow().setGravity(gravity);
    }

    public final void setDimAmount(@FloatRange(from = 0, to = 1) float amount) {
        getWindow().setDimAmount(amount);
    }

    public final void setAnimationStyle(@StyleRes int animRes) {
        getWindow().setWindowAnimations(animRes);
    }

    @Override
    public void setOnShowListener(@Nullable OnShowListener listener) {
        if (listener == null) {
            return;
        }
        final OnShowListener current = this;
        super.setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                current.onShow(dialog);
                listener.onShow(dialog);
            }
        });
    }

    @Override
    public void setOnDismissListener(@Nullable OnDismissListener listener) {
        if (listener == null) {
            return;
        }
        final OnDismissListener current = this;
        super.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                current.onDismiss(dialog);
                listener.onDismiss(dialog);
            }
        });
    }

    @CallSuper
    @Override
    public void show() {
        if (isShowing()) {
            return;
        }
        try {
            super.show();
            PickerLog.print("dialog show");
        } catch (Exception e) {
            //...not attached to window manager
            //...Unable to add window...is your activity running?
            //...Activity...has leaked window...that was originally added here
            PickerLog.print(e);
        }
    }

    @CallSuper
    @Override
    public void dismiss() {
        if (!isShowing()) {
            return;
        }
        try {
            super.dismiss();
            PickerLog.print("dialog dismiss");
        } catch (Exception e) {
            //...not attached to window manager
            //...Activity...has leaked window...that was originally added here
            PickerLog.print(e);
        }
    }

    @CallSuper
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        initData();
        PickerLog.print("dialog initData");
    }

    protected void initData() {

    }

    @CallSuper
    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    /**
     * @see #onAttachedToWindow()
     */
    @Override
    public void onShow(DialogInterface dialog) {

    }

    /**
     * @see #onDetachedFromWindow()
     */
    @Override
    public void onDismiss(DialogInterface dialog) {
        removeMaskView();
    }

    private void removeMaskView() {
        if (maskView == null) {
            return;
        }
        try {
            activity.getWindowManager().removeViewImmediate(maskView);
        } catch (Exception e) {
            PickerLog.print(e);
        }
    }

}
