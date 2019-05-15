package cn.qqtheme.framework.wheelview.widget;

import android.content.Context;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.qqtheme.framework.wheelview.interfaces.NumberFormatter;

/**
 * 数字滚轮控件，参见 https://github.com/gzu-liyujiang/AndroidPicker
 *
 * @author liyujiang
 * @date 2019/5/13 19:13
 */
public class NumberWheelView extends WheelView<String> {
    private float minValue;
    private float maxValue;
    private float stepValue;
    private float defaultValue;
    private NumberFormatter numberFormatter;
    private boolean isDecimal;

    public NumberWheelView(Context context) {
        super(context);
    }

    public NumberWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {
        minValue = 1F;
        maxValue = 100F;
        stepValue = 1F;
        defaultValue = minValue;
        isDecimal = false;
    }

    @Override
    protected String assignDefault() {
        return formatNumber(defaultValue);
    }

    @Override
    protected List<String> generateData() {
        final List<String> data = new ArrayList<>();
        for (float i = minValue; i <= maxValue; i = i + stepValue) {
            data.add(formatNumber(i));
        }
        return data;
    }

    private String formatNumber(float value) {
        if (numberFormatter != null) {
            return numberFormatter.format(value);
        }
        if (isDecimal) {
            return String.format(Locale.getDefault(), "%.1f", value);
        }
        return String.valueOf((int) value);
    }

    public void setRange(int minValue, int maxValue) {
        setRange(minValue, maxValue, minValue);
    }

    public void setRange(float minValue, float maxValue) {
        setRange(minValue, maxValue, minValue);
    }

    public void setRange(int minValue, int maxValue, int defaultValue) {
        setRange(minValue, maxValue, 1, defaultValue);
    }

    public void setRange(float minValue, float maxValue, float defaultValue) {
        setRange(minValue, maxValue, 1F, defaultValue);
    }

    public void setRange(int minValue, int maxValue, int stepValue, int defaultValue) {
        setRangeInner(minValue, maxValue, stepValue, defaultValue, false);
    }

    public void setRange(float minValue, float maxValue, float stepValue, float defaultValue) {
        setRangeInner(minValue, maxValue, stepValue, defaultValue, true);
    }

    private void setRangeInner(float minValue, float maxValue, float stepValue, float defaultValue, boolean isDecimal) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.stepValue = stepValue;
        this.defaultValue = defaultValue;
        this.isDecimal = isDecimal;
        refreshData();
    }

    public void setNumberFormatter(NumberFormatter numberFormatter) {
        this.numberFormatter = numberFormatter;
        refreshData();
    }

    private void refreshData() {
        setData(generateData());
        setDefaultItem(assignDefault());
    }

    public float getMaxValue() {
        return maxValue;
    }

    public float getMinValue() {
        return minValue;
    }

    @Override
    public String getCurrentItem() {
        return formatNumber(getCurrentValue());
    }

    public float getCurrentValue() {
        int position = super.getCurrentItemPosition();
        return minValue + position;
    }

}
