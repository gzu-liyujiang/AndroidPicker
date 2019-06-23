package cn.qqtheme.androidpicker.custom;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import cn.qqtheme.framework.wheelpicker.DateTimePicker;
import cn.qqtheme.framework.wheelview.annotation.DateMode;
import cn.qqtheme.framework.wheelview.annotation.TimeMode;

/**
 * 钉钉风格的时间选择器
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/6/23
 */
public class TimeLikeDingDingPicker extends DateTimePicker {

    public TimeLikeDingDingPicker(FragmentActivity activity) {
        super(activity, DateMode.NONE, TimeMode.HOUR_24);
    }

    @Override
    public void onViewCreated(@NonNull View contentView) {
        super.onViewCreated(contentView);
    }

}
