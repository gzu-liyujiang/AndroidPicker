package cn.qqtheme.framework.popup;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.qqtheme.framework.util.ConvertUtils;

/**
 * 带确定及取消按钮的
 *
 * @param <V> the type parameter
 * @author 李玉江[QQ:1032694760]
 * @since 2015/10/21
 */
public abstract class ConfirmPopup<V extends View> extends BottomPopup<View> {
    protected boolean topLineVisible = true;
    protected int topLineColor = 0xFFDDDDDD;
    protected int topBackgroundColor = Color.WHITE;
    protected boolean cancelVisible = true;
    protected CharSequence cancelText = "";
    protected CharSequence submitText = "";
    protected CharSequence titleText = "";
    protected int cancelTextColor = Color.BLACK;
    protected int submitTextColor = Color.BLACK;
    protected int titleTextColor = Color.BLACK;

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
     * Sets top line color.
     *
     * @param topLineColor the top line color
     */
    public void setTopLineColor(@ColorInt int topLineColor) {
        this.topLineColor = topLineColor;
    }

    /**
     * Sets top background color.
     *
     * @param topBackgroundColor the top background color
     */
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
     * Sets cancel text.
     *
     * @param textRes the text res
     */
    public void setCancelText(@StringRes int textRes) {
        this.cancelText = activity.getString(textRes);
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
     * Sets submit text.
     *
     * @param textRes the text res
     */
    public void setSubmitText(@StringRes int textRes) {
        this.submitText = activity.getString(textRes);
    }

    /**
     * Sets title text.
     *
     * @param titleText the title text
     */
    public void setTitleText(CharSequence titleText) {
        this.titleText = titleText;
    }

    /**
     * Sets title text.
     *
     * @param textRes the text res
     */
    public void setTitleText(@StringRes int textRes) {
        this.titleText = activity.getString(textRes);
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

    /**
     * Sets title text color.
     *
     * @param titleTextColor the title text color
     */
    public void setTitleTextColor(@ColorInt int titleTextColor) {
        this.titleTextColor = titleTextColor;
    }

    /**
     * @see #makeHeaderView()
     * @see #makeCenterView()
     * @see #makeFooterView()
     */
    @Override
    protected final View makeContentView() {
        LinearLayout rootLayout = new LinearLayout(activity);
        rootLayout.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        rootLayout.setBackgroundColor(Color.WHITE);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setGravity(Gravity.CENTER);
        rootLayout.setPadding(0, 0, 0, 0);
        rootLayout.setClipToPadding(false);
        View headerView = makeHeaderView();
        if (headerView != null) {
            rootLayout.addView(headerView);
        }
        if (topLineVisible) {
            View lineView = new View(activity);
            lineView.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, 1));
            lineView.setBackgroundColor(topLineColor);
            rootLayout.addView(lineView);
        }
        rootLayout.addView(makeCenterView(), new LinearLayout.LayoutParams(MATCH_PARENT, 0, 1.0f));
        View footerView = makeFooterView();
        if (footerView != null) {
            rootLayout.addView(footerView);
        }
        return rootLayout;
    }

    /**
     * Make header view view.
     *
     * @return the view
     */
    @Nullable
    protected View makeHeaderView() {
        RelativeLayout topButtonLayout = new RelativeLayout(activity);
        topButtonLayout.setLayoutParams(new RelativeLayout.LayoutParams(MATCH_PARENT, ConvertUtils.toPx(activity, 40)));
        topButtonLayout.setBackgroundColor(topBackgroundColor);
        topButtonLayout.setGravity(Gravity.CENTER_VERTICAL);

        Button cancelButton = new Button(activity);
        cancelButton.setVisibility(cancelVisible ? View.VISIBLE : View.GONE);
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
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                onCancel();
            }
        });
        topButtonLayout.addView(cancelButton);

        TextView titleView = new TextView(activity);
        RelativeLayout.LayoutParams titleLayoutParams = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        int margin = ConvertUtils.toPx(activity, 20);
        titleLayoutParams.leftMargin = margin;
        titleLayoutParams.rightMargin = margin;
        titleLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        titleLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        titleView.setLayoutParams(titleLayoutParams);
        titleView.setGravity(Gravity.CENTER);
        if (!TextUtils.isEmpty(titleText)) {
            titleView.setText(titleText);
        }
        titleView.setTextColor(titleTextColor);
        topButtonLayout.addView(titleView);

        Button submitButton = new Button(activity);
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
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                onSubmit();
            }
        });
        topButtonLayout.addView(submitButton);

        return topButtonLayout;
    }

    /**
     * Init center view v.
     *
     * @return the v
     */
    @NonNull
    protected abstract V makeCenterView();

    /**
     * Make footer view view.
     *
     * @return the view
     */
    @Nullable
    protected View makeFooterView() {
        return null;
    }

    /**
     * On submit.
     */
    protected void onSubmit() {

    }

    /**
     * On cancel.
     */
    protected void onCancel() {

    }

}
