package cn.qqtheme.framework.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

import java.lang.reflect.Field;

import cn.qqtheme.framework.util.ConvertUtils;

/**
 * 文字描边效果，参见：http://blog.csdn.net/u013716863/article/details/37663325
 * <br />
 * Author:李玉江[QQ:1032694760]
 * DateTime:2017/01/04 00:23
 * Builder:Android Studio
 */
public class StrokeTextView extends TextView {
    private TextPaint textPaint;
    private boolean isStroke = false;
    private int borderWidth = 3;//dp
    private int textColor = Color.WHITE;
    private int borderColor = Color.BLACK;

    public StrokeTextView(Context context) {
        super(context);
        init();
    }

    public StrokeTextView(Context context, boolean isStroke) {
        super(context);
        init();
        this.isStroke = isStroke;
    }

    public StrokeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StrokeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        textPaint = getPaint();
    }

    public void setStroke(boolean stroke) {
        isStroke = stroke;
        invalidate();
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        invalidate();
    }

    public void setBorderColor(@ColorInt int borderColor) {
        this.borderColor = borderColor;
        invalidate();
    }

    @Override
    public void setTextColor(@ColorInt int textColor) {
        this.textColor = textColor;
        super.setTextColor(textColor);
    }

    @Override
    public void setTextColor(ColorStateList colors) {
        this.textColor = colors.getColorForState(getDrawableState(), textColor);
        super.setTextColor(colors);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!isStroke) {
            super.onDraw(canvas);
            return;
        }
        if (textColor == borderColor) {
            borderColor = ConvertUtils.toDarkenColor(borderColor, 0.7f);
        }

        // 描外层
        setTextColorUseReflection(borderColor);
        textPaint.setStrokeWidth(ConvertUtils.toPx(getContext(), borderWidth));
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setFakeBoldText(true); // 外层文字采用粗体
        super.onDraw(canvas);

        // 描内层
        setTextColorUseReflection(textColor);
        textPaint.setStrokeWidth(0);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setFakeBoldText(false); // 内层文字不采用粗体
        super.onDraw(canvas);
    }

    /**
     * 使用反射的方法进行字体颜色的设置
     */
    private void setTextColorUseReflection(@ColorInt int color) {
        //noinspection TryWithIdenticalCatches
        try {
            Field field = TextView.class.getDeclaredField("mCurTextColor");
            field.setAccessible(true);
            field.set(this, color);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        textPaint.setColor(color);
    }

}
