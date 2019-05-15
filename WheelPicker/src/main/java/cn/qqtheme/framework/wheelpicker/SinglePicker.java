package cn.qqtheme.framework.wheelpicker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import java.util.List;

import cn.qqtheme.framework.popup.AbstractConfirmPopup;
import cn.qqtheme.framework.wheelview.interfaces.OnSingleSelectedListener;
import cn.qqtheme.framework.wheelview.interfaces.TextProvider;
import cn.qqtheme.framework.wheelview.widget.WheelView;

/**
 * 单项滚轮选择，参见 https://github.com/gzu-liyujiang/AndroidPicker
 *
 * @param <T> 泛型除了{@link CharSequence}及其子类，需要实现{@link TextProvider}或者重载{@link Object#toString()}提供显示文本
 * @author liyujiang
 * @date 2019/5/8 10:04
 * @see WheelView
 * @see LayoutProvider
 * @see OnSingleSelectedListener
 */
public class SinglePicker<T> extends AbstractConfirmPopup<WheelView<T>> {
    private OnSingleSelectedListener<T> onSingleSelectedListener;
    private List<T> data;
    private T defaultItem;
    private int defaultItemPosition;

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

    public void setOnSingleSelectedListener(OnSingleSelectedListener<T> onSingleSelectedListener) {
        this.onSingleSelectedListener = onSingleSelectedListener;
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

    @Override
    protected void onConfirm() {
        super.onConfirm();
        if (onSingleSelectedListener != null) {
            onSingleSelectedListener.onSingleSelected(getBodyView().getCurrentItemPosition(),
                    getBodyView().getCurrentItem());
        }
    }

}
