package cn.qqtheme.framework.wheelview.widget;

import android.content.Context;
import android.util.AttributeSet;

import cn.qqtheme.framework.wheelview.interfaces.NumberFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 数字滚轮控件
 *
 * @param <T> 泛型主要为{@link Integer}和{@link Float}
 * @author liyujiang
 * @date 2019/5/13 19:13
 */
@SuppressWarnings("unused")
public class NumberWheelView<T extends Number> extends WheelView<String> {
    private T minValue;
    private T maxValue;
    private T stepValue;
    private T defaultValue;
    private boolean isDecimal;
    private NumberFormatter<T> numberFormatter;

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
    protected String assignDefault() {
        return formatNumber(defaultValue);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected List<String> generateData() {
        final List<String> data = new ArrayList<>();
        if (isDecimal) {
            for (float i = minValue.floatValue(); i <= maxValue.floatValue();
                 i = i + stepValue.floatValue()) {
                data.add(formatNumber((T) Float.valueOf(i)));
            }
        } else {
            for (int i = minValue.intValue(); i <= maxValue.intValue();
                 i = i + stepValue.intValue()) {
                data.add(formatNumber((T) Integer.valueOf(i)));
            }
        }
        return data;
    }

    private String formatNumber(T value) {
        if (numberFormatter != null) {
            return numberFormatter.format(value);
        }
        if (isDecimal) {
            return String.format(Locale.getDefault(), "%.1f", value.floatValue());
        }
        return String.valueOf(value.intValue());
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

    public void setNumberFormatter(NumberFormatter<T> numberFormatter) {
        this.numberFormatter = numberFormatter;
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
    public String getCurrentItem() {
        return formatNumber(getCurrentValue());
    }

    public T getCurrentValue() {
        int position = super.getCurrentItemPosition();
        return getValueByPosition(position);
    }

    @SuppressWarnings("unchecked")
    public T getValueByPosition(int position) {
        if (isDecimal) {
            return (T) Float.valueOf(minValue.floatValue() + position);
        }
        return (T) Integer.valueOf(minValue.intValue() + position);
    }

}
