package cn.qqtheme.framework.wheelview.widget;

import android.content.Context;
import android.util.AttributeSet;

import java.util.Calendar;

/**
 * 日子滚轮控件
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/6/17 13:58
 * @since 2.0
 */
public class DayWheelView extends NumberWheelView<Integer> {

    public DayWheelView(Context context) {
        super(context);
    }

    public DayWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {
        super.init();
        Calendar calendar = Calendar.getInstance();
        int defaultDay = calendar.get(Calendar.DAY_OF_MONTH);
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        setRange(1, maxDay, 1, defaultDay);
    }

}
