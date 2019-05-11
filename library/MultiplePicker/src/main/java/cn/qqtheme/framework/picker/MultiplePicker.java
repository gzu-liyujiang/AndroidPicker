package cn.qqtheme.framework.picker;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import java.util.List;

import cn.qqtheme.framework.core.BasePopup;

/**
 * 多项选择器
 *
 * @author 李玉江[QQ:1032694760]
 * @since 2017/06/24
 */
public class MultiplePicker extends BasePopup<MultiplePicker> {

    public MultiplePicker(FragmentActivity activity) {
        super(activity);
    }

    @Override
    protected View createContentView(FragmentActivity activity) {
        return null;
    }

    @Override
    protected void onViewCreated(@NonNull View contentView) {

    }

    public interface OnItemPickListener {

        void onItemPicked(int count, List<String> items);

    }

}
