package cn.qqtheme.framework.wheelview.widget;

import android.content.Context;
import android.util.AttributeSet;

import java.util.Calendar;

/**
 * 分钟滚轮控件
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/6/17 14:03
 * @since 2.0
 */
public class MinuteWheelView extends NumberWheelView<Integer> {

    public MinuteWheelView(Context context) {
        super(context);
    }

    public MinuteWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {
        super.init();
        int defaultMinute = Calendar.getInstance().get(Calendar.MINUTE);
        setRange(0, 59, 1, defaultMinute);
    }

}
