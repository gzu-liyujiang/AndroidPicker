package cn.qqtheme.framework.wheelview.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import cn.qqtheme.framework.wheelview.interfaces.NumberFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * 数字滚轮控件
 *
 * @param <T> 泛型主要为{@code Integer}和{@code Float}
 * @author <a href="mailto:1032694760@qq.com">liyujiang</a>
 * @date 2019/5/13 19:13
 * @since 2.0
 */
@SuppressWarnings("unused")
public class NumberWheelView<T extends Number> extends WheelView<T> {
    private T minValue;
    private T maxValue;
    private T stepValue;
    private T defaultValue;
    private boolean isDecimal;
    private NumberFormatter<T> formatter;

    public NumberWheelView(Context context) {
        super(context);
    }

    public NumberWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void init() {
        minValue = (T) Integer.valueOf(1);
        maxValue = (T) Integer.valueOf(100);
        stepValue = (T) Integer.valueOf(1);
        defaultValue = minValue;
        isDecimal = false;
    }

    @Override
    protected T assignDefault() {
        return defaultValue;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected List<T> generateData() {
        final List<T> data = new ArrayList<>();
        if (isDecimal) {
            for (float i = minValue.floatValue(); i <= maxValue.floatValue();
                 i = i + stepValue.floatValue()) {
                data.add((T) Float.valueOf(i));
            }
        } else {
            for (int i = minValue.intValue(); i <= maxValue.intValue();
                 i = i + stepValue.intValue()) {
                data.add((T) Integer.valueOf(i));
            }
        }
        return data;
    }

    public void setFormatter(NumberFormatter<T> formatter) {
        this.formatter = formatter;
    }

    public void setRange(int minValue, int maxValue) {
        setRange(minValue, maxValue, minValue);
    }

    public void setRange(int minValue, int maxValue, int defaultValue) {
        setRange(minValue, maxValue, 1, defaultValue);
    }

    @SuppressWarnings("unchecked")
    public void setRange(int minValue, int maxValue, int stepValue, int defaultValue) {
        this.minValue = (T) Integer.valueOf(minValue);
        this.maxValue = (T) Integer.valueOf(maxValue);
        this.stepValue = (T) Integer.valueOf(stepValue);
        this.defaultValue = (T) Integer.valueOf(defaultValue);
        this.isDecimal = false;
        refreshData();
    }

    public void setRange(float minValue, float maxValue) {
        setRange(minValue, maxValue, minValue);
    }

    public void setRange(float minValue, float maxValue, float defaultValue) {
        setRange(minValue, maxValue, 1F, defaultValue);
    }

    @SuppressWarnings("unchecked")
    public void setRange(float minValue, float maxValue, float stepValue, float defaultValue) {
        this.minValue = (T) Float.valueOf(minValue);
        this.maxValue = (T) Float.valueOf(maxValue);
        this.stepValue = (T) Float.valueOf(stepValue);
        this.defaultValue = (T) Float.valueOf(defaultValue);
        this.isDecimal = true;
        refreshData();
    }

    private void refreshData() {
        setData(generateData());
        setDefaultItem(assignDefault());
    }

    public T getMaxValue() {
        return maxValue;
    }

    public T getMinValue() {
        return minValue;
    }

    @Override
    public String formatItemText(int position, @NonNull Object object) {
        if (formatter != null) {
            //noinspection unchecked
            return formatter.format((T) object);
        }
        return super.formatItemText(position, object);
    }

    public String getCurrentItemFormatString() {
        T currentItem = getCurrentItem();
        if (formatter != null) {
            return formatter.format(currentItem);
        }
        return super.formatItemText(getCurrentItemPosition(), currentItem);
    }

}
