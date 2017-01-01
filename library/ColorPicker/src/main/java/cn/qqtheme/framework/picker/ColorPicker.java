package cn.qqtheme.framework.picker;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

import cn.qqtheme.framework.popup.ConfirmPopup;
import cn.qqtheme.framework.util.AssetsUtils;
import cn.qqtheme.framework.util.ConvertUtils;
import cn.qqtheme.framework.widget.ColorPanelView;

/**
 * 颜色选择器。
 *
 * @author 李玉江[QQ:1032694760]
 * @since 2015/9/29
 */
public class ColorPicker extends ConfirmPopup<LinearLayout> {
    @IdRes
    private static final int MULTI_ID = 0x1;
    @IdRes
    private static final int BLACK_ID = 0x2;
    private int initColor = Color.WHITE;
    private ColorPanelView multiColorView, blackColorView;
    private TextView hexValView;
    private ColorStateList hexValDefaultColor;
    private OnColorPickListener onColorPickListener;

    public ColorPicker(Activity activity) {
        super(activity);
        setHalfScreen(true);
    }

    @Nullable
    @Override
    protected View makeHeaderView() {
        View headerView = super.makeHeaderView();
        hexValView = getTitleView();
        hexValView.getLayoutParams().height = ConvertUtils.toPx(activity, 30);
        hexValView.setGravity(Gravity.CENTER);
        hexValView.setBackgroundColor(initColor);
        hexValView.setTextColor(Color.BLACK);
        hexValView.setShadowLayer(3, 0, 2, Color.WHITE);//设置阴影，以便背景色为黑色系列时仍然看得见
        hexValView.setMinEms(6);
        hexValView.setMaxEms(8);
        hexValView.setPadding(0, 0, 0, 0);
        hexValView.setSingleLine(true);
        hexValView.setEnabled(false);
        hexValDefaultColor = hexValView.getTextColors();
        return headerView;
    }

    @Override
    @NonNull
    protected LinearLayout makeCenterView() {
        LinearLayout rootLayout = new LinearLayout(activity);
        rootLayout.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        rootLayout.setOrientation(LinearLayout.VERTICAL);

        multiColorView = new ColorPanelView(activity);
        multiColorView.setId(MULTI_ID);
        multiColorView.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, 0, 1.0f));
        Bitmap cursorTopBitmap = AssetsUtils.readBitmap(activity, "color_picker_cursor_top.png");
        multiColorView.setPointerDrawable(ConvertUtils.toDrawable(cursorTopBitmap));
        multiColorView.setLockPointerInBounds(true);
        multiColorView.setOnColorChangedListener(new ColorPanelView.OnColorChangedListener() {
            @Override
            public void onColorChanged(ColorPanelView view, int color) {
                updateCurrentColor(color);
            }
        });
        rootLayout.addView(multiColorView);

        blackColorView = new ColorPanelView(activity);
        blackColorView.setId(BLACK_ID);
        blackColorView.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, ConvertUtils.toPx(activity, 30)));
        Bitmap cursorBottomBitmap = AssetsUtils.readBitmap(activity, "color_picker_cursor_bottom.png");
        blackColorView.setPointerDrawable(ConvertUtils.toDrawable(cursorBottomBitmap));
        blackColorView.setLockPointerInBounds(false);
        blackColorView.setOnColorChangedListener(new ColorPanelView.OnColorChangedListener() {
            @Override
            public void onColorChanged(ColorPanelView view, int color) {
                updateCurrentColor(color);
            }
        });
        rootLayout.addView(blackColorView);

        return rootLayout;
    }

    @Override
    protected void setContentViewAfter(View contentView) {
        multiColorView.setColor(initColor);//将触发onColorChanged，故必须先待其他控件初始化完成后才能调用
        multiColorView.setBrightnessGradientView(blackColorView);
    }

    @Override
    protected void onSubmit() {
        if (onColorPickListener != null) {
            onColorPickListener.onColorPicked(getCurrentColor());
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        multiColorView.recycle();
        blackColorView.recycle();
    }

    @ColorInt
    public int getCurrentColor() {
        return Color.parseColor("#" + hexValView.getText());
    }

    private void updateCurrentColor(int color) {
        String hexColorString = ConvertUtils.toColorString(color, false).toUpperCase(Locale.getDefault());
        hexValView.setText(hexColorString);
        hexValView.setTextColor(hexValDefaultColor);
        hexValView.setBackgroundColor(color);
    }

    /**
     * 设置初始默认颜色
     *
     * @param initColor 颜色值，如：0xFFFF00FF
     */
    public void setInitColor(int initColor) {
        this.initColor = initColor;
    }

    public void setOnColorPickListener(OnColorPickListener onColorPickListener) {
        this.onColorPickListener = onColorPickListener;
    }

    /**
     * 颜色选择回调
     */
    public interface OnColorPickListener {

        void onColorPicked(@ColorInt int pickedColor);

    }

}
