package cn.qqtheme.framework.wheelview.widget;

import android.content.Context;
import android.util.AttributeSet;

import java.util.Calendar;

/**
 * 月份滚轮控件
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/6/17 13:50
 * @since 2.0
 */
public class MonthWheelView extends NumberWheelView<Integer> {

    public MonthWheelView(Context context) {
        super(context);
    }

    public MonthWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {
        super.init();
        int defaultMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        setRange(1, 12, 1, defaultMonth);
    }

}
