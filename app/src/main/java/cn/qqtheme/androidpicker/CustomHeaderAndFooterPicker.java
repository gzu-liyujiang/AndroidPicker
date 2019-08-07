package cn.qqtheme.androidpicker;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.widget.WheelView;

/**
 * 自定义顶部及底部
 * <p>
 * Author:李玉江[QQ:1032694760]
 * Email:liyujiang_tk@yeah.net
 * DateTime:2016/1/29 14:47
 * Builder:Android Studio
 */
public class CustomHeaderAndFooterPicker extends OptionPicker implements OptionPicker.OnWheelListener {
    private TextView titleView;

    public CustomHeaderAndFooterPicker(Activity activity) {
        super(activity, new String[]{
                "Java/Android", "PHP/MySQL", "HTML/CSS/JS", "C/C++"
        });
        setSelectedIndex(1);
        setDividerRatio(WheelView.DividerConfig.FILL);
        setOnWheelListener(this);
    }

    @Override
    protected void showAfter() {
        View rootView = getRootView();
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(rootView, "alpha", 0, 1);
        ObjectAnimator translation = ObjectAnimator.ofFloat(rootView, "translationY", 300, 0);
        animatorSet.playTogether(alpha, translation);
        animatorSet.setDuration(1000);
        animatorSet.setInterpolator(new AccelerateInterpolator());
        animatorSet.start();
    }

    @Override
    public void dismiss() {
        View rootView = getRootView();
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(rootView, "alpha", 1, 0);
        ObjectAnimator translation = ObjectAnimator.ofFloat(rootView, "translationX", 0, rootView.getWidth());
        ObjectAnimator rotation = ObjectAnimator.ofFloat(rootView, "rotation", 0, 120);
        animatorSet.playTogether(alpha, translation, rotation);
        animatorSet.setDuration(1000);
        animatorSet.setInterpolator(new AccelerateInterpolator());
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                dismissImmediately();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }

    @Nullable
    @Override
    protected View makeHeaderView() {
        View view = LayoutInflater.from(activity).inflate(R.layout.picker_header, null);
        titleView = (TextView) view.findViewById(R.id.picker_title);
        titleView.setText(titleText);
        view.findViewById(R.id.picker_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }

    @Nullable
    @Override
    protected View makeFooterView() {
        View view = LayoutInflater.from(activity).inflate(R.layout.picker_footer, null);
        Button submitView = (Button) view.findViewById(R.id.picker_submit);
        submitView.setText(submitText);
        submitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                onSubmit();
            }
        });
        Button cancelView = (Button) view.findViewById(R.id.picker_cancel);
        cancelView.setText(cancelText);
        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                onCancel();
            }
        });
        return view;
    }

    @Override
    public void onWheeled(int index, String item) {
        if (titleView != null) {
            titleView.setText(item);
        }
    }

    @Override
    public void onSubmit() {
        super.onSubmit();
    }

    @Override
    protected void onCancel() {
        super.onCancel();
    }

}
