package cn.qqtheme.framework.popup;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import cn.qqtheme.framework.util.ConvertUtils;

/**
 * 带确定及取消按钮的
 *
 * @param <V> the type parameter
 * @author 李玉江[QQ :1032694760]
 * @version 2015 /10/21
 */
public abstract class ConfirmPopup<V extends View> extends BottomPopup<View> implements View.OnClickListener {
    private static final String TAG_SUBMIT = "submit";
    private static final String TAG_CANCEL = "cancel";
    private boolean topLineVisible = true;
    private int topLineColor = 0xFFDDDDDD;
    private int topBackgroundColor = Color.WHITE;
    private boolean cancelVisible = true;
    private CharSequence cancelText = "", submitText = "";
    private int cancelTextColor = Color.BLACK;
    private int submitTextColor = Color.BLACK;
    private OnConfirmListener onConfirmListener;

    /**
     * Instantiates a new Confirm popup.
     *
     * @param activity the activity
     */
    public ConfirmPopup(Activity activity) {
        super(activity);
        cancelText = activity.getString(android.R.string.cancel);
        submitText = activity.getString(android.R.string.ok);
    }

    /**
     * Init content view v.
     *
     * @return the v
     */
    protected abstract V initContentView();

    /**
     * Sets top line color.
     *
     * @param topLineColor the top line color
     */
    public void setTopLineColor(@ColorInt int topLineColor) {
        this.topLineColor = topLineColor;
    }

    public void setTopBackgroundColor(@ColorInt int topBackgroundColor) {
        this.topBackgroundColor = topBackgroundColor;
    }

    /**
     * Sets top line visible.
     *
     * @param topLineVisible the top line visible
     */
    public void setTopLineVisible(boolean topLineVisible) {
        this.topLineVisible = topLineVisible;
    }

    /**
     * Sets cancel visible.
     *
     * @param cancelVisible the cancel visible
     */
    public void setCancelVisible(boolean cancelVisible) {
        this.cancelVisible = cancelVisible;
    }

    /**
     * Sets cancel text.
     *
     * @param cancelText the cancel text
     */
    public void setCancelText(CharSequence cancelText) {
        this.cancelText = cancelText;
    }

    /**
     * Sets submit text.
     *
     * @param submitText the submit text
     */
    public void setSubmitText(CharSequence submitText) {
        this.submitText = submitText;
    }

    /**
     * Sets cancel text color.
     *
     * @param cancelTextColor the cancel text color
     */
    public void setCancelTextColor(@ColorInt int cancelTextColor) {
        this.cancelTextColor = cancelTextColor;
    }

    /**
     * Sets submit text color.
     *
     * @param submitTextColor the submit text color
     */
    public void setSubmitTextColor(@ColorInt int submitTextColor) {
        this.submitTextColor = submitTextColor;
    }

    @Override
    protected View getView() {
        LinearLayout rootLayout = new LinearLayout(activity);
        rootLayout.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        rootLayout.setBackgroundColor(Color.WHITE);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setGravity(Gravity.CENTER);
        rootLayout.setPadding(0, 0, 0, 0);
        rootLayout.setClipToPadding(false);
        RelativeLayout topButtonLayout = new RelativeLayout(activity);
        topButtonLayout.setLayoutParams(new RelativeLayout.LayoutParams(MATCH_PARENT, ConvertUtils.toPx(activity, 40)));
        topButtonLayout.setBackgroundColor(topBackgroundColor);
        topButtonLayout.setGravity(Gravity.CENTER_VERTICAL);
        Button cancelButton = new Button(activity);
        cancelButton.setVisibility(cancelVisible ? View.VISIBLE : View.GONE);
        cancelButton.setTag(TAG_CANCEL);
        RelativeLayout.LayoutParams cancelButtonLayoutParams = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        cancelButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        cancelButtonLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        cancelButton.setLayoutParams(cancelButtonLayoutParams);
        cancelButton.setBackgroundColor(Color.TRANSPARENT);
        cancelButton.setGravity(Gravity.CENTER);
        if (!TextUtils.isEmpty(cancelText)) {
            cancelButton.setText(cancelText);
        }
        cancelButton.setTextColor(cancelTextColor);
        cancelButton.setOnClickListener(this);
        topButtonLayout.addView(cancelButton);
        Button submitButton = new Button(activity);
        submitButton.setTag(TAG_SUBMIT);
        RelativeLayout.LayoutParams submitButtonLayoutParams = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        submitButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        submitButtonLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        submitButton.setLayoutParams(submitButtonLayoutParams);
        submitButton.setBackgroundColor(Color.TRANSPARENT);
        submitButton.setGravity(Gravity.CENTER);
        if (!TextUtils.isEmpty(submitText)) {
            submitButton.setText(submitText);
        }
        submitButton.setTextColor(submitTextColor);
        submitButton.setOnClickListener(this);
        topButtonLayout.addView(submitButton);
        rootLayout.addView(topButtonLayout);
        if (topLineVisible) {
            View lineView = new View(activity);
            lineView.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, 1));
            lineView.setBackgroundColor(topLineColor);
            rootLayout.addView(lineView);
        }
        rootLayout.addView(initContentView());
        return rootLayout;
    }

    @Override
    public void onClick(View v) {
        if (onConfirmListener != null) {
            String tag = v.getTag().toString();
            if (tag.equals(TAG_SUBMIT)) {
                onConfirmListener.onConfirm();
            } else {
                onConfirmListener.onCancel();
            }
        }
        dismiss();
    }

    /**
     * Sets on confirm listener.
     *
     * @param onConfirmListener the on confirm listener
     */
    protected void setOnConfirmListener(OnConfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
    }

    /**
     * The type On confirm listener.
     */
    protected static abstract class OnConfirmListener {

        /**
         * On confirm.
         */
        public abstract void onConfirm();

        /**
         * On cancel.
         */
        public void onCancel() {

        }

    }

}
