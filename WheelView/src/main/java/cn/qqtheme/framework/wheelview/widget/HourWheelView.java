package cn.qqtheme.framework.wheelview.widget;

import android.content.Context;
import android.util.AttributeSet;

import java.util.Calendar;

/**
 * 小时滚轮控件
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/6/17 14:01
 * @since 2.0
 */
public class HourWheelView extends NumberWheelView<Integer> {

    public HourWheelView(Context context) {
        super(context);
    }

    public HourWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {
        super.init();
        int defaultHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        setRange(0, 23, 1, defaultHour);
    }

}
