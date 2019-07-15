package cn.qqtheme.framework.popup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.qqtheme.framework.popup.contract.LayoutProvider;
import cn.qqtheme.framework.popup.impl.ConfirmLayoutProvider;

/**
 * 确认选择弹窗
 *
 * @author liyujiang
 * @date 2019/5/8 10:04
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class AbstractConfirmPopup<V extends View> extends BasePopup<AbstractConfirmPopup> {
    private RelativeLayout rlHeader;
    private TextView tvCancel;
    private TextView tvTitle;
    private TextView tvConfirm;
    private View topBorder;
    private View headerDivider;
    private V bodyView;

    private LayoutProvider layoutProvider;

    public AbstractConfirmPopup(FragmentActivity activity) {
        super(activity);
    }

    @NonNull
    protected abstract V createBodyView(Context context);

    public void setLayoutProvider(LayoutProvider layoutProvider) {
        this.layoutProvider = layoutProvider;
    }

    @Override
    public View createContentView(FragmentActivity activity) {
        if (layoutProvider == null) {
            layoutProvider = new ConfirmLayoutProvider();
        }
        View view = View.inflate(activity, layoutProvider.provideLayoutRes(), null);
        topBorder = view.findViewById(R.id.top_border);
        rlHeader = view.findViewById(R.id.rl_header);
        tvCancel = view.findViewById(layoutProvider.specifyCancelIdRes());
        tvTitle = view.findViewById(layoutProvider.specifyTitleIdRes());
        tvConfirm = view.findViewById(layoutProvider.specifyConfirmIdRes());
        headerDivider = view.findViewById(R.id.header_divider);
        View bodyViewContainer = view.findViewById(layoutProvider.specifyBodyIdRes());
        if (!(bodyViewContainer instanceof ViewGroup)) {
            throw new NullPointerException("bodyViewContainer must be ViewGroup, such as FrameLayout");
        }
        ViewGroup bodyViewGroup = (ViewGroup) bodyViewContainer;
        if (bodyViewGroup.getChildCount() > 0) {
            bodyViewGroup.removeAllViews();
        }
        bodyView = createBodyView(activity);
        ViewGroup.LayoutParams bodyParams = bodyView.getLayoutParams();
        if (bodyViewGroup instanceof FrameLayout) {
            FrameLayout.LayoutParams layoutParams;
            if (bodyParams == null) {
                layoutParams = new FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
            } else {
                layoutParams = new FrameLayout.LayoutParams(bodyParams);
            }
            layoutParams.gravity = Gravity.CENTER;
            bodyParams = layoutParams;
        } else if (bodyViewGroup instanceof LinearLayout) {
            LinearLayout.LayoutParams layoutParams;
            if (bodyParams == null) {
                layoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
            } else {
                layoutParams = new LinearLayout.LayoutParams(bodyParams);
            }
            layoutParams.gravity = Gravity.CENTER;
            bodyParams = layoutParams;
        } else if (bodyViewGroup instanceof RelativeLayout) {
            RelativeLayout.LayoutParams layoutParams;
            if (bodyParams == null) {
                layoutParams = new RelativeLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
            } else {
                layoutParams = new RelativeLayout.LayoutParams(bodyParams);
            }
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            bodyParams = layoutParams;
        }
        bodyViewGroup.addView(bodyView, bodyParams);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View contentView) {
        if (tvCancel != null) {
            tvCancel.setOnClickListener(this);
        }
        if (tvConfirm != null) {
            tvConfirm.setOnClickListener(this);
        }
        if (bodyView == null) {
            throw new NullPointerException("bodyView == null");
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == layoutProvider.specifyCancelIdRes()) {
            onCancel();
        } else if (id == layoutProvider.specifyConfirmIdRes()) {
            onConfirm();
        }
    }

    protected void onCancel() {
        dismiss();
    }

    protected void onConfirm() {
        dismiss();
    }

    private void checkContentView() {
        if (contentView == null) {
            throw new RuntimeException("Please show popup at first");
        }
    }

    public final RelativeLayout getHeaderView() {
        checkContentView();
        return rlHeader;
    }

    public final TextView getCancelTextView() {
        checkContentView();
        return tvCancel;
    }

    public final TextView getTitleTextView() {
        checkContentView();
        return tvTitle;
    }

    public final TextView getConfirmTextView() {
        checkContentView();
        return tvConfirm;
    }

    public final View getTopBorderView() {
        checkContentView();
        return topBorder;
    }

    public final View getHeaderDividerView() {
        checkContentView();
        return headerDivider;
    }

    public final V getBodyView() {
        checkContentView();
        return bodyView;
    }

}
