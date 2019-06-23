package cn.qqtheme.framework.wheelpicker;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;

import cn.qqtheme.framework.popup.AbstractConfirmPopup;
import cn.qqtheme.framework.wheelview.contract.OnItemSelectedListener;
import cn.qqtheme.framework.wheelview.contract.TextProvider;
import cn.qqtheme.framework.wheelview.util.DensityUtils;
import cn.qqtheme.framework.wheelview.widget.WheelView;

import java.util.List;

/**
 * 单项滚轮选择，参见 https://github.com/gzu-liyujiang/AndroidPicker
 *
 * @param <T> 泛型除了{@code CharSequence}及其子类，需要实现{@link TextProvider}
 *            或者重载{@code Object#toString}提供显示文本
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/5/8 10:04
 * @see TextProvider
 * @see OnItemSelectedListener
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class SinglePicker<T> extends AbstractConfirmPopup<WheelView<T>> {
    private OnItemSelectedListener<T> onItemSelectedListener;
    private List<T> data;
    private T defaultItem;
    private int defaultItemPosition;
    private int itemWidthInDp;

    public SinglePicker(FragmentActivity activity) {
        super(activity);
    }

    @NonNull
    @Override
    protected WheelView<T> createBodyView(Context context) {
        View view = View.inflate(context, R.layout.popup_wheel_single, null);
        WheelView<T> wheelView = view.findViewById(R.id.wheel_view);
        wheelView.setData(data);
        if (defaultItem == null) {
            defaultItem = data.get(defaultItemPosition);
        }
        wheelView.setDefaultItem(defaultItem);
        return wheelView;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener<T> onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public void setDefaultItem(T defaultItem) {
        this.defaultItem = defaultItem;
    }

    public void setDefaultItemPosition(int defaultItemPosition) {
        this.defaultItemPosition = defaultItemPosition;
    }

    public void setWrapContent(boolean wrapContent) {
        this.itemWidthInDp = wrapContent ? WRAP_CONTENT : MATCH_PARENT;
    }

    public void setItemWidthInDp(@IntRange(from = 50) int itemWidthInDp) {
        this.itemWidthInDp = itemWidthInDp;
    }

    public List<T> getData() {
        return getBodyView().getAdapter().getData();
    }

    @Override
    public void onViewCreated(@NonNull View contentView) {
        super.onViewCreated(contentView);
        WheelView bodyView = getBodyView();
        ViewGroup.LayoutParams bodyParams = bodyView.getLayoutParams();
        int width = WRAP_CONTENT;
        if (itemWidthInDp != WRAP_CONTENT && itemWidthInDp != MATCH_PARENT) {
            width = DensityUtils.dp2px(activity, itemWidthInDp);
        }
        bodyParams.width = width;
        bodyView.setLayoutParams(bodyParams);
    }

    @Override
    protected void onConfirm() {
        super.onConfirm();
        if (onItemSelectedListener != null) {
            onItemSelectedListener.onItemSelected(getBodyView().getCurrentItemPosition(),
                    getBodyView().getCurrentItem());
        }
    }

}
