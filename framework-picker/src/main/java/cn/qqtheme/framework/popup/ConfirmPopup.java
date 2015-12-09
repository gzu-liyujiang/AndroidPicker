package cn.qqtheme.framework.popup;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import cn.qqtheme.framework.picker.R;

/**
 * 带确定及取消按钮的
 *
 * @author 李玉江[QQ:1032694760]
 * @since 2015/10/21
 * Created By Android Studio
 */
public abstract class ConfirmPopup<V extends View> extends BasePopup<View> implements View.OnClickListener {
    protected static final String TAG_SUBMIT = "submit";
    protected static final String TAG_CANCEL = "cancel";
    private boolean cancelVisible = true;
    private CharSequence cancelText;
    private CharSequence submitText;
    private OnConfirmListener onConfirmListener;

    public ConfirmPopup(Activity activity) {
        super(activity);
        cancelText = activity.getString(android.R.string.cancel);
        submitText = activity.getString(android.R.string.ok);
    }

    protected abstract V initContentView();

    public void setCancelVisible(boolean cancelVisible) {
        this.cancelVisible = cancelVisible;
    }

    public void setCancelText(CharSequence cancelText) {
        this.cancelText = cancelText;
    }

    public void setSubmitText(CharSequence submitText) {
        this.submitText = submitText;
    }

    @Override
    protected View getView() {
//        LinearLayout rootLayout = new LinearLayout(activity);
//        rootLayout.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
//        rootLayout.setBackgroundColor(Color.WHITE);
//        rootLayout.setOrientation(LinearLayout.VERTICAL);
//        rootLayout.setGravity(Gravity.CENTER);
//        rootLayout.setPadding(0, 0, 0, 0);
//        rootLayout.setClipToPadding(false);
//        RelativeLayout topButtonLayout = new RelativeLayout(activity);
//        topButtonLayout.setLayoutParams(new RelativeLayout.LayoutParams(MATCH_PARENT, Common.toPx(activity, 40)));
//        topButtonLayout.setBackgroundColor(Color.WHITE);
//        topButtonLayout.setGravity(Gravity.CENTER_VERTICAL);
//        Button cancelButton = new Button(activity);
//        if (cancelVisible) {
//            cancelButton.setVisibility(View.VISIBLE);
//        } else {
//            cancelButton.setVisibility(View.GONE);
//        }
//        cancelButton.setTag(TAG_CANCEL);
//        RelativeLayout.LayoutParams cancelButtonLayoutParams = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
//        cancelButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
//        cancelButtonLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
//        cancelButton.setLayoutParams(cancelButtonLayoutParams);
//        cancelButton.setBackgroundColor(Color.TRANSPARENT);
//        cancelButton.setGravity(Gravity.CENTER);
//        cancelButton.setText(cancelText);
//        cancelButton.setTextColor(Common.toColorStateList(Color.BLACK, Color.BLUE));
//        cancelButton.setOnClickListener(this);
//        topButtonLayout.addView(cancelButton);
//        Button submitButton = new Button(activity);
//        submitButton.setTag(TAG_SUBMIT);
//        RelativeLayout.LayoutParams submitButtonLayoutParams = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
//        submitButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
//        submitButtonLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
//        submitButton.setLayoutParams(submitButtonLayoutParams);
//        submitButton.setBackgroundColor(Color.TRANSPARENT);
//        submitButton.setGravity(Gravity.CENTER);
//        submitButton.setText(submitText);
//        submitButton.setTextColor(Common.toColorStateList(Color.BLACK, Color.BLUE));
//        submitButton.setOnClickListener(this);
//        topButtonLayout.addView(submitButton);
//        rootLayout.addView(topButtonLayout);
//        View lineView = new View(activity);
//        lineView.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, Common.toPx(activity, 1)));
//        lineView.setBackgroundColor(0xDDDDDDDD);
//        rootLayout.addView(lineView);
//        rootLayout.addView(initContentView());
        View rootLayout = LayoutInflater.from(activity).inflate(R.layout.popup_confirm, null);
        Button cancelButton = (Button) rootLayout.findViewById(R.id.popup_cancel);
        cancelButton.setOnClickListener(this);
        if (cancelVisible) {
            cancelButton.setVisibility(View.VISIBLE);
        } else {
            cancelButton.setVisibility(View.GONE);
        }
        cancelButton.setText(cancelText);
        cancelButton.setTag(TAG_CANCEL);
        Button submitButton = (Button) rootLayout.findViewById(R.id.popup_ok);
        submitButton.setOnClickListener(this);
        submitButton.setText(submitText);
        submitButton.setTag(TAG_SUBMIT);
        FrameLayout layout = (FrameLayout) rootLayout.findViewById(R.id.popup_content);
        layout.addView(initContentView());
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

    public void setOnConfirmListener(OnConfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
    }

    public static abstract class OnConfirmListener {

        public abstract void onConfirm();

        public void onCancel() {

        }

    }

}
