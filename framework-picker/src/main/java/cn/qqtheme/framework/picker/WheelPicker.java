package cn.qqtheme.framework.picker;

import android.app.Activity;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import cn.qqtheme.framework.popup.ConfirmPopup;

/**
 * 滚动轮子选择器。
 *
 * @author 李玉江[QQ:1032694760]
 * @link https://github.com/saiwu-bigkoo/PickerView
 * @since 2015/9/29
 * Created By Android Studio
 */
public abstract class WheelPicker<T> extends ConfirmPopup<ScrollView> {
    private LinearLayout view;
    private OnWheelListener<T> onWheelListener;

    public WheelPicker(Activity activity) {
        super(activity);
        view = initWheelView();
    }

    @Override
    protected final ScrollView initContentView() {
        ScrollView scrollView = new ScrollView(activity);
        scrollView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        scrollView.addView(view);
        return scrollView;
    }

    @Override
    protected void onShowPrepare() {
        super.onShowPrepare();
        checkMaxHeight(view);
        if (onWheelListener != null) {
            super.setOnConfirmListener(new OnConfirmListener() {
                @Override
                public void onConfirm() {
                    onWheelListener.onSubmit(getCurrentSelected());
                }

                @Override
                public void onCancel() {
                    onWheelListener.onCancel();
                }
            });
        }
    }

    protected abstract LinearLayout initWheelView();

    public abstract T getCurrentSelected();

    @Deprecated
    @Override
    public final void setOnConfirmListener(OnConfirmListener onConfirmListener) {
        throw new RuntimeException("Please use setOnWheelListener instead");
    }

    public void setOnWheelListener(OnWheelListener<T> listener) {
        this.onWheelListener = listener;
    }

    public static abstract class OnWheelListener<R> extends OnConfirmListener {

        public abstract void onSubmit(R result);

        @Deprecated
        @Override
        public void onConfirm() {
            throw new RuntimeException("Please use onSubmit instead");
        }

    }

}
