package cn.qqtheme.framework.wheelview.widget;

import android.content.Context;
import android.util.AttributeSet;

import java.util.Calendar;

/**
 * 年份滚轮控件
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/6/17 13:45
 * @since 2.0
 */
public class YearWheelView extends NumberWheelView<Integer> {
    private static final int YEAR_OFFSET = 50;

    public YearWheelView(Context context) {
        super(context);
    }

    public YearWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {
        super.init();
        int defaultYear = Calendar.getInstance().get(Calendar.YEAR);
        int minYear = defaultYear - YEAR_OFFSET;
        int maxYear = defaultYear + YEAR_OFFSET;
        setRange(minYear, maxYear, 1, defaultYear);
    }

}
