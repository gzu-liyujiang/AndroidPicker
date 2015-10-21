package cn.qqtheme.framework.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

/**
 * 文本水平自动滚动（从右到左）。
 *
 * @author 李玉江[QQ:1023694760]
 * @version 2013-1-11
 * @link http://blog.sina.com.cn/s/blog_749d1a9f01012evf.html
 */
public class MarqueeTextView extends TextView {

    public MarqueeTextView(Context context) {
        this(context, null);
    }

    public MarqueeTextView(Context context, AttributeSet attr) {
        super(context, attr);
        setEllipsize(TextUtils.TruncateAt.MARQUEE);
        setMarqueeRepeatLimit(-1);
        setSingleLine(true);
        setHorizontallyScrolling(true);
        setClickable(true);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setGravity(Gravity.CENTER);
    }

    /**
     * 必须已获取到焦点，才能即显滚动
     */
    @Override
    public boolean isFocused() {
        return true;
    }

    @Override
    public boolean isPressed() {
        return true;
    }

}
