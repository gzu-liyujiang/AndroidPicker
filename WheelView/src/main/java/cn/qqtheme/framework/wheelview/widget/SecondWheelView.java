package cn.qqtheme.framework.wheelview.widget;

import android.content.Context;
import android.util.AttributeSet;

import java.util.Calendar;

/**
 * 秒钟滚轮控件
 *
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/6/17 14:04
 * @since 2.0
 */
public class SecondWheelView extends NumberWheelView<Integer> {

    public SecondWheelView(Context context) {
        super(context);
    }

    public SecondWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {
        super.init();
        int defaultSecond = Calendar.getInstance().get(Calendar.SECOND);
        setRange(0, 59, 1, defaultSecond);
    }

}
