package cn.qqtheme.framework.picker;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.*;
import android.text.InputType;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import cn.qqtheme.framework.helper.Common;
import cn.qqtheme.framework.popup.ConfirmPopup;
import cn.qqtheme.framework.view.ColorPanelView;

import java.util.Locale;

/**
 * 颜色选择器。
 *
 * @author 李玉江[QQ:1032694760]
 * @since 2015/9/29
 * Created By Android Studio
 */
public class ColorPicker extends ConfirmPopup<LinearLayout> implements TextView.OnEditorActionListener {
    private static final int MULTI_ID = 0x1;
    private static final int BLACK_ID = 0x2;
    private int initColor = Color.WHITE;
    private ColorPanelView multiColorView, blackColorView;
    private EditText hexValView;
    private ColorStateList hexValDefaultColor;
    private OnColorPickListener onColorPickListener;

    public ColorPicker(Activity activity) {
        super(activity);
    }

    @Override
    protected LinearLayout initContentView() {
        LinearLayout rootLayout = new LinearLayout(activity);
        rootLayout.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        blackColorView = new ColorPanelView(activity);
        //noinspection ResourceType
        blackColorView.setId(BLACK_ID);
        blackColorView.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, Common.toPx(activity, 30)));
        blackColorView.setPointerDrawable(Common.getDrawable(activity, R.drawable.color_picker_cursor_bottom));
        blackColorView.setLockPointerInBounds(false);
        blackColorView.setOnColorChangedListener(new ColorPanelView.OnColorChangedListener() {
            @Override
            public void onColorChanged(ColorPanelView view, int color) {
                updateCurrentColor(color);
            }
        });
        rootLayout.addView(blackColorView);
        multiColorView = new ColorPanelView(activity);
        //noinspection ResourceType
        multiColorView.setId(MULTI_ID);
        multiColorView.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, 0, 1.0f));
        multiColorView.setPointerDrawable(Common.getDrawable(activity, R.drawable.color_picker_cursor_top));
        multiColorView.setLockPointerInBounds(true);
        multiColorView.setOnColorChangedListener(new ColorPanelView.OnColorChangedListener() {
            @Override
            public void onColorChanged(ColorPanelView view, int color) {
                updateCurrentColor(color);
            }
        });
        rootLayout.addView(multiColorView);
        LinearLayout previewLayout = new LinearLayout(activity);
        previewLayout.setOrientation(LinearLayout.HORIZONTAL);
        previewLayout.setGravity(Gravity.CENTER);
        previewLayout.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, Common.toPx(activity, 30)));
        hexValView = new EditText(activity);
        hexValView.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        hexValView.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        hexValView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        hexValView.setGravity(Gravity.CENTER);
        hexValView.setBackgroundColor(initColor);
        hexValView.setTextColor(Color.BLACK);
        hexValView.setShadowLayer(3, 0, 2, Color.WHITE);//设置阴影，以便背景色为黑色系列时仍然看得见
        hexValView.setMinEms(6);
        hexValView.setMaxEms(8);
        hexValView.setPadding(0, 0, 0, 0);
        hexValView.setSingleLine(true);
        hexValView.setOnEditorActionListener(this);
        hexValDefaultColor = hexValView.getTextColors();
        previewLayout.addView(hexValView);
        rootLayout.addView(previewLayout);
        return rootLayout;
    }

    @Override
    protected void setContentViewAfter(View contentView) {
        setHeight(screenHeight / 2);
        multiColorView.setColor(initColor);//将触发onColorChanged，故必须先待其他控件初始化完成后才能调用
        multiColorView.setBrightnessGradientView(blackColorView);
        if (onColorPickListener != null) {
            super.setOnConfirmListener(new OnConfirmListener() {
                @Override
                public void onConfirm() {
                    onColorPickListener.onColorPicked(Color.parseColor("#" + hexValView.getText()));
                }

                @Override
                public void onCancel() {
                    onColorPickListener.onCancel();
                }
            });
        }
    }

    private void updateCurrentColor(int color) {
        String hexColorString = Common.toColorString(color, false).toUpperCase(Locale.getDefault());
        hexValView.setText(hexColorString);
        hexValView.setTextColor(hexValDefaultColor);
        hexValView.setBackgroundColor(color);
    }

    public void setInitColor(int initColor) {
        this.initColor = initColor;
    }

    /**
     * Use {@link #setOnColorPickListener(OnColorPickListener)} instead
     */
    @Deprecated
    @Override
    public final void setOnConfirmListener(OnConfirmListener onConfirmListener) {
        throw new RuntimeException("Please use setOnColorPickListener(OnColorPickListener) instead");
    }

    public void setOnColorPickListener(OnColorPickListener onColorPickListener) {
        this.onColorPickListener = onColorPickListener;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            String hexString = hexValView.getText().toString();
            int length = hexString.length();
            if (length == 6 || length == 8) {
                try {
                    int color = Color.parseColor("#" + hexString);
                    multiColorView.setColor(color);
                    hexValView.setTextColor(hexValDefaultColor);
                } catch (IllegalArgumentException e) {
                    hexValView.setTextColor(Color.RED);
                }
            } else {
                hexValView.setTextColor(Color.RED);
            }
            return true;
        }
        return false;
    }

    public static abstract class OnColorPickListener extends OnConfirmListener {

        public abstract void onColorPicked(int pickedColor);

        /**
         * Use {@link #onColorPicked(int)} instead
         */
        @Deprecated
        @Override
        public void onConfirm() {
            throw new RuntimeException("Please use onColorPicked(int) instead");
        }

    }

}
